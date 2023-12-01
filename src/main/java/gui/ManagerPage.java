package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import model.User;
import service.RoleService;

public class ManagerPage extends JFrame {
    private JTable table;
    private String currentRoleFilter = null;

    public ManagerPage() {
        setTitle("Manager Board");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        JPanel filterPanel = createFilterPanel();
        add(filterPanel, BorderLayout.SOUTH);

        initializeTable();

        JScrollPane rightScrollPane = new JScrollPane(table);
        rightScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        rightScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(rightScrollPane, BorderLayout.CENTER);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Manager Board", JLabel.CENTER);
        titleLabel.setFont(new Font(titleLabel.getFont().getFontName(), Font.BOLD, 26));
        titleLabel.setForeground(new Color(0x003366));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        String[] searchOptions = {"Name", "Email"};
        JComboBox<String> searchTypeComboBox = new JComboBox<>(searchOptions);

        JPanel searchPanel = new JPanel();
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(0x204688));
        searchButton.setForeground(Color.WHITE);
        searchButton.addActionListener(e -> performSearch(
            (String) searchTypeComboBox.getSelectedItem(),
            searchField.getText()
        ));

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            searchField.setText("");
            loadTableData(currentRoleFilter);
        });
    
        searchPanel.add(searchTypeComboBox);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(clearButton);
        topPanel.add(searchPanel, BorderLayout.SOUTH);
        return topPanel;
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
    
        Dimension buttonSize = new Dimension(100, 30);
    
        JButton customerButton = createButton("Customer", buttonSize, e -> loadTableData("customer"));
        JButton staffButton = createButton("Staff", buttonSize, e -> loadTableData("staff"));
        JButton allButton = createButton("All", buttonSize, e -> loadTableData(null));
    
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        filterPanel.add(customerButton, gbc);
    
        gbc.gridx = 1;
        filterPanel.add(staffButton, gbc);
    
        gbc.gridx = 2;
        filterPanel.add(allButton, gbc);
    
        return filterPanel;
    }
    
    private JButton createButton(String text, Dimension size, ActionListener listener) {
        JButton button = new JButton(text);
        button.setMinimumSize(size);
        button.setMaximumSize(size);
        button.setPreferredSize(size);
        button.addActionListener(listener);
        return button;
    } 

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

    private void performSearch(String searchType, String searchTerm) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        Map<User, String> userList = RoleService.getAllUserWithRole();

        // Filter the list with currentRoleFilter
        Stream<Map.Entry<User, String>> filteredStream = userList.entrySet().stream()
            .filter(entry -> currentRoleFilter == null || entry.getValue().equalsIgnoreCase(currentRoleFilter));

        filteredStream.filter(entry -> matchSearchCriteria(entry, searchType, searchTerm))
            .forEach(entry -> {
                User user = entry.getKey();
                model.addRow(new Object[]{
                    user.getUserID(),
                    user.getEmail(),
                    user.getForename(),
                    user.getSurname(),
                    entry.getValue(),
                    getActionButtonLabelByRole(entry.getValue())
                });
            });
    }
    
    private boolean matchSearchCriteria(Map.Entry<User, String> entry, String searchType, String searchTerm) {
        User user = entry.getKey();
        switch (searchType) {
            case "Name":
                return user.getForename().contains(searchTerm) || user.getSurname().contains(searchTerm);
            case "Email":
                return user.getEmail().contains(searchTerm);
            default:
                return false;
        }
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
}
