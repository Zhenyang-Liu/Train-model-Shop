package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import DAO.UserDAO;
import helper.UserSession;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;

import model.User;
import service.RoleService;

public class ManagerPage extends JFrame {
    private JTable table;
    private String currentRoleFilter = null;

    public ManagerPage() {
        setTitle("Manager Board");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // top panel with title and search box
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Manager Board", JLabel.CENTER);
        topPanel.add(titleLabel, BorderLayout.NORTH);

        // Search Box
        // TODO：Searchbox unfinished
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JTextField(20));
        searchPanel.add(new JButton("button1"));
        searchPanel.add(new JButton("button2"));
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        JPanel leftPanel = createLeftPanel();

        // Display the User table
        initializeTable();

        // Right Panel with JTable
        JScrollPane rightScrollPane = new JScrollPane(table);
        rightScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rightScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Dividing line
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightScrollPane);
        splitPane.setDividerLocation(0.3);
        add(splitPane, BorderLayout.CENTER);
    }

    // Section for leftPanel
    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(Box.createVerticalGlue());
    
        Dimension buttonSize = new Dimension(100, 30);
    
        JButton customerButton = createButton("Customer", buttonSize, e -> loadTableData("customer"));
        JButton staffButton = createButton("Staff", buttonSize, e -> loadTableData("staff"));
        JButton allButton = createButton("All", buttonSize, e -> loadTableData(null));
    
        leftPanel.add(customerButton);
        leftPanel.add(staffButton);
        leftPanel.add(allButton);
        leftPanel.add(Box.createVerticalGlue());
    
        return leftPanel;
    }
    
    private JButton createButton(String text, Dimension size, ActionListener listener) {
        JButton button = new JButton(text);
        button.setMinimumSize(size);
        button.setMaximumSize(size);
        button.setPreferredSize(size);
        button.addActionListener(listener);
        return button;
    }
    // -------------------------------------------------------

    // Section for rightPanel
    private void initializeTable() {
        String[] columnNames = {"UserID", "Email Address", "Forename", "Surname", "Role", "Action"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return String.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };
    
        table = new JTable(model);
        table.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));
    
        loadTableData(currentRoleFilter);
    }   

    private void loadTableData(String roleFilter) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        currentRoleFilter = roleFilter;
    
        Map<User, String> userList = new LinkedHashMap<>();
        userList.putAll(RoleService.getAllUserWithRole());

        if (userList != null && !userList.isEmpty()) {
            for (Map.Entry<User, String> entry : userList.entrySet()) {
                User user = entry.getKey();
                String role = entry.getValue();
                if (role != null && (roleFilter == null || role.equalsIgnoreCase(roleFilter))) {
                    model.addRow(new Object[]{
                        user.getUserID(),
                        user.getEmail(),
                        user.getForename(),
                        user.getSurname(),
                        role,
                        getActionButtonLabelByRole(role)
                    });
                }
            }
        } else {
            model.addRow(new Object[]{"There is no user", "", "", "", "", ""});
        }
    }

    private void reloadTableData() {
        loadTableData(currentRoleFilter);
    } 

    private String getActionButtonLabelByRole(String role) {
        if ("customer".equalsIgnoreCase(role)) {
            return "Assign";
        } else if ("staff".equalsIgnoreCase(role)) {
            return "Dismiss";
        }
        return "";
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
    
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof String) {
                setText((String) value);
                return this;
            }
            return new JLabel("");
        }
    }
    
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private int editingRow;
    
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (isPushed) {
                        handleButtonClick();
                        isPushed = false;
                    }
                    fireEditingStopped(); 
                }
            });
        }     

        private void handleButtonClick() {
            int userId = getUserID(editingRow);
            boolean success = false;
            String newRole = "";
            String newActionLabel = "";
        
            if ("Assign".equals(label)) {
                success = RoleService.assignStaff(userId);
                newRole = "STAFF";
                newActionLabel = "Dismiss";
            } else if ("Dismiss".equals(label)) {
                success = RoleService.dismissStaff(userId);
                newRole = "CUSTOMER";
                newActionLabel = "Assign";
            }
        
            if (success) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.setValueAt(newRole, editingRow, 4);
                model.setValueAt(newActionLabel, editingRow, 5);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        reloadTableData();
                    }
                });
            }
            isPushed = false;
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            if (value instanceof String) {
                label = (String) value;
                button.setText(label);
                isPushed = true;
                editingRow = row;
                return button;
            }
            return new JLabel("");
        }
    
        @Override
        public Object getCellEditorValue() {
            return label;
        }
    
        private int getUserID(int row) {
            return Integer.parseInt(ManagerPage.this.table.getModel().getValueAt(row, 0).toString());
        }
    
        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    
        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }    

    public static void main(String[] args) {
        User user = UserDAO.findUserByEmail("manager@manager.com");
        UserSession.getInstance().setCurrentUser(user);

        SwingUtilities.invokeLater(() -> {
            ManagerPage frame = new ManagerPage();
            frame.setVisible(true);
        });
    }
}
