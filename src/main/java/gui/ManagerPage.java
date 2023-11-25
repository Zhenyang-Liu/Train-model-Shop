package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import DAO.UserDAO;
import helper.UserSession;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import model.User;
import service.RoleService;

public class ManagerPage extends JFrame {
    private JTable table;

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

        // Left Panel with filter
        // TODO：Button with filter Unfinished
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(Box.createVerticalGlue());

        Dimension buttonSize = new Dimension(100, 30);

        JButton button1 = new JButton("Customer");
        button1.setMinimumSize(buttonSize);
        button1.setMaximumSize(buttonSize);
        button1.setPreferredSize(buttonSize);
        leftPanel.add(button1);

        JButton button2 = new JButton("Staff");
        button2.setMinimumSize(buttonSize);
        button2.setMaximumSize(buttonSize);
        button2.setPreferredSize(buttonSize);
        leftPanel.add(button2);

        JButton button3 = new JButton("All");
        button3.setMinimumSize(buttonSize);
        button3.setMaximumSize(buttonSize);
        button3.setPreferredSize(buttonSize);
        leftPanel.add(button3);

        leftPanel.add(Box.createVerticalGlue());

        // Display the User table
        initializeTable();

        // Right Panel with JTable
        JScrollPane rightScrollPane = new JScrollPane(table);

        // Dividing line
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightScrollPane);
        splitPane.setDividerLocation(0.3);
        add(splitPane, BorderLayout.CENTER);
    }

    private void initializeTable() {
        String[] columnNames = {"UserID", "Email Address", "Forename", "Surname", "Role", "Action"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 5 ? JButton.class : Object.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        loadTableData(model);
        table = new JTable(model);
        table.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));
    }

    private void loadTableData(DefaultTableModel model) {
        Map<User, String> userList = RoleService.getAllUserWithRole();
        if (userList != null && !userList.isEmpty()) {
            for (Map.Entry<User, String> entry : userList.entrySet()) {
                User user = entry.getKey();
                String role = entry.getValue();
                Object actionButton = null;
            
                if ("customer".equalsIgnoreCase(role)) {
                    actionButton = new JButton("Assign");
                } else if ("staff".equalsIgnoreCase(role)) {
                    actionButton = new JButton("Dismiss");
                } else {
                    actionButton = null;
                }
                
                model.addRow(new Object[]{
                    user.getUserID(),
                    user.getEmail(),
                    user.getForename(),
                    user.getSurname(),
                    role,
                    actionButton
                });
            }            
        } else {
            model.addRow(new Object[]{"There is no user", "", "", "", "", ""});
        }
    }

    // TODO：the button can't be shown refresh the line.
    private void refreshLine(int userID) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            int currentUserId = Integer.parseInt(model.getValueAt(i, 0).toString());
            if (currentUserId == userID) {
                // Get Updated User
                Map<User, String> result = RoleService.getUserWithRole(currentUserId);
                User updatedUser = null;
                String role = null;
    
                if (!result.isEmpty()) {
                    Map.Entry<User, String> entry = result.entrySet().iterator().next();
                    updatedUser = entry.getKey();
                    role = entry.getValue();
                }
                model.setValueAt(updatedUser.getUserID(), i, 0);
                model.setValueAt(updatedUser.getEmail(), i, 1);
                model.setValueAt(updatedUser.getForename(), i, 2);
                model.setValueAt(updatedUser.getSurname(), i, 3);
                model.setValueAt(role, i, 4);

                JButton actionButton = null;
                if ("customer".equalsIgnoreCase(role)) {
                    actionButton = new JButton("Assign");
                } else if ("staff".equalsIgnoreCase(role)) {
                    actionButton = new JButton("Dismiss");
                }
                model.setValueAt(actionButton, i, 5);
    
                break;
            }
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
    
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
    
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof JButton) {
            return (JButton) value;
            }
            return new JLabel(""); 
            }

    }

    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private int editingRow;
    
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    boolean success = false;
                    int userId = getUserID(editingRow);
                    if (button.getText().equals("Assign")) {
                        success = assignStaff(userId);
                    } else if (button.getText().equals("Dismiss")) {
                        success = dismissStaff(userId);
                    }
                    if (!success) {
                        JOptionPane.showMessageDialog(button, "Operation failed!");
                    } else {
                        ManagerPage.this.refreshLine(userId);
                    }
                    fireEditingStopped();
                }             
            });
        }
    
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            if (value instanceof JButton) {
                JButton btn = (JButton) value;
                button.setText(btn.getText());
                editingRow = row;
                return button;
            }
            return new JLabel("");
        }
    
        public boolean assignStaff(int userID) {
            return RoleService.assignStaff(userID);
        }
        
        public boolean dismissStaff(int userID) {
            return RoleService.dismissStaff(userID);
        }

        private int getUserID(int row) {
            return Integer.parseInt(ManagerPage.this.table.getModel().getValueAt(row, 0).toString());
        }

        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
}
