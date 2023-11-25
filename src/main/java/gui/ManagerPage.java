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

        JPanel leftPanel = createLeftPanel();

        // Display the User table
        initializeTable();

        // Right Panel with JTable
        JScrollPane rightScrollPane = new JScrollPane(table);

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

        JButton customerButton = new JButton("Customer");
        customerButton.setMinimumSize(buttonSize);
        customerButton.setMaximumSize(buttonSize);
        customerButton.setPreferredSize(buttonSize);
        leftPanel.add(customerButton);

        JButton staffButton = new JButton("Staff");
        staffButton.setMinimumSize(buttonSize);
        staffButton.setMaximumSize(buttonSize);
        staffButton.setPreferredSize(buttonSize);
        leftPanel.add(staffButton);

        JButton allButton = new JButton("All");
        allButton.setMinimumSize(buttonSize);
        allButton.setMaximumSize(buttonSize);
        allButton.setPreferredSize(buttonSize);
        leftPanel.add(allButton);

        leftPanel.add(Box.createVerticalGlue());

        // Add button listener for left panel
        customerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filterTableData("customer");
            }
        });

        staffButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filterTableData("staff");
            }
        });

        allButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                filterTableData(null);
            }
        });
        return leftPanel;
    }

    // Section for rightPanel
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

    private void filterTableData(String roleFilter) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); 
    
        Map<User, String> userList = RoleService.getAllUserWithRole();
        if (userList != null && !userList.isEmpty()) {
            for (Map.Entry<User, String> entry : userList.entrySet()) {
                User user = entry.getKey();
                String role = entry.getValue();
    
                if (role != null && (roleFilter == null || role.equalsIgnoreCase(roleFilter))) {
                    Object actionButton = getActionButtonByRole(role);
                    model.addRow(new Object[]{
                        user.getUserID(),
                        user.getEmail(),
                        user.getForename(),
                        user.getSurname(),
                        role,
                        actionButton
                    });
                }
            }
        } else {
            model.addRow(new Object[]{"There is no user", "", "", "", "", ""});
        }
    }    

    private JButton getActionButtonByRole(String role) {
        if ("customer".equalsIgnoreCase(role)) {
            return new JButton("Assign");
        } else if ("staff".equalsIgnoreCase(role)) {
            return new JButton("Dismiss");
        }
        return null;
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
                Map<User, String> result = RoleService.getUserWithRole(currentUserId);
                User updatedUser = null;
                String role = null;
                
                if (!result.isEmpty()) {
                    Map.Entry<User, String> entry = result.entrySet().iterator().next();
                    updatedUser = entry.getKey();
                    role = entry.getValue();
                }
    
                // 更新用户信息
                model.setValueAt(updatedUser.getUserID(), i, 0);
                model.setValueAt(updatedUser.getEmail(), i, 1);
                model.setValueAt(updatedUser.getForename(), i, 2);
                model.setValueAt(updatedUser.getSurname(), i, 3);
                model.setValueAt(role, i, 4);
    
                // 根据新角色更新按钮
                JButton newButton = getActionButtonByRole(role);
                model.setValueAt(newButton, i, 5);
    
                // 通知模型该行数据已更改
                model.fireTableRowsUpdated(i, i);

                // 触发表格视图的重新验证和重绘
                table.revalidate();
                table.repaint();
            }
        }
    }
     
      
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
    
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof JButton) {
                JButton button = (JButton) value;
                setText(button.getText());
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
                    }
                    fireEditingStopped();
                }
            });
        }

        private void handleButtonClick() {
            int userId = getUserID(editingRow);
            if ("Assign".equals(label)) {
                if (RoleService.assignStaff(userId)) {
                    ManagerPage.this.refreshLine(userId);
                }
            } else if ("Dismiss".equals(label)) {
                if (RoleService.dismissStaff(userId)) {
                    ManagerPage.this.refreshLine(userId);
                }
            }
        }
    
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            if (value instanceof JButton) {
                JButton btn = (JButton) value;
                label = btn.getText();
                button.setText(label);
                isPushed = true;
                editingRow = row;
                return button;
            }
            return new JLabel("");
        }
    
        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                int userId = getUserID(editingRow);
                if ("Assign".equals(label)) {
                    assignStaff(userId);
                } else if ("Dismiss".equals(label)) {
                    dismissStaff(userId);
                }
                isPushed = false;
            }
            return new JButton(label);
        }
    
        private int getUserID(int row) {
            return Integer.parseInt(ManagerPage.this.table.getModel().getValueAt(row, 0).toString());
        }
    
        public boolean assignStaff(int userID) {
            return RoleService.assignStaff(userID);
        }
    
        public boolean dismissStaff(int userID) {
            return RoleService.dismissStaff(userID);
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
