package gui;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import DAO.UserDAO;
import helper.UserSession;

import java.awt.*;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import model.Order;
import model.User;
import service.OrderService;

public class OrderManagePage extends JFrame {
    private JTable orderTable;
    private String currentStatusFilter;

    public OrderManagePage() {
        setTitle("Order Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        JPanel orderPanel = createOrderPanel();
        add(orderPanel, BorderLayout.CENTER);

        JPanel filterPanel = createOrderFilterPanel();
        add(filterPanel, BorderLayout.SOUTH);
        
        setLocationRelativeTo(null);
        setVisible(true);

        loadOrderData("all");
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
    
        JLabel titleLabel = new JLabel("Order Management", JLabel.CENTER);
        titleLabel.setFont(new Font(titleLabel.getFont().getFontName(), Font.BOLD, 26));
        titleLabel.setForeground(new Color(0x003366));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        topPanel.add(titleLabel, gbc);
    
        String[] searchOptions = {"Email", "OrderID"};
        JComboBox<String> searchComboBox = new JComboBox<>(searchOptions);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        topPanel.add(searchComboBox, gbc);
    
        JTextField searchField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        topPanel.add(searchField, gbc);
    
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(0x204688));
        searchButton.setForeground(Color.WHITE);
        gbc.gridx = 2;
        gbc.gridy = 1;
        topPanel.add(searchButton, gbc);
    
        searchButton.addActionListener(e -> performSearch((String)searchComboBox.getSelectedItem(), searchField.getText()));
    
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            searchField.setText("");
            searchComboBox.setSelectedIndex(0);
            loadOrderData(currentStatusFilter);
        });
        gbc.gridx = 3;
        gbc.gridy = 1;
        topPanel.add(clearButton, gbc);

        return topPanel;
    }

    private JPanel createOrderPanel() {
        JPanel orderPanel = new JPanel();
        orderPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
    
        OrderTableModel tableModel = new OrderTableModel();
        orderTable = new JTable(tableModel);
    
        ButtonColumnListener listener = new ButtonColumnListener() {
            @Override
            public void onButtonClicked(int row, int column) {
                Order selectedOrder = tableModel.getOrderAt(row);
                SwingUtilities.invokeLater(() -> {
                    OrderDetailPage frame = new OrderDetailPage(selectedOrder, () -> {
                        loadOrderData(currentStatusFilter);
                    });
                    frame.setVisible(true);
                });
            }
        };
    
        orderTable.getColumn("Details").setCellRenderer(new ButtonRenderer());
        orderTable.getColumn("Details").setCellEditor(new ButtonEditor(new JCheckBox(), listener));
    
        JScrollPane scrollPane = new JScrollPane(orderTable);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    
        orderPanel.add(scrollPane, gbc);
        return orderPanel;
    }
    
    private JPanel createOrderFilterPanel() {
        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        Dimension buttonSize = new Dimension(100, 30);
        JButton allButton = createButton("All", buttonSize, e -> loadOrderData("all"));
        JButton confirmedButton = createButton("Confirmed", buttonSize, e -> loadOrderData("confirmed"));
        JButton fulfilledButton = createButton("Fulfilled", buttonSize, e -> loadOrderData("fulfilled"));
        JButton cancelledButton = createButton("Cancelled", buttonSize, e -> loadOrderData("cancelled"));

        gbc.gridx = 0;
        filterPanel.add(allButton, gbc);

        gbc.gridx = 1;
        filterPanel.add(confirmedButton, gbc);

        gbc.gridx = 2;
        filterPanel.add(fulfilledButton, gbc);

        gbc.gridx = 3;
        filterPanel.add(cancelledButton, gbc);

        return filterPanel;
    }

    private JButton createButton(String text, Dimension size, ActionListener listener) {
        JButton button = new JButton(text);
        button.setPreferredSize(size);
        button.addActionListener(listener);
        return button;
    }

    private void performSearch(String searchType, String searchTerm) {
        ArrayList<Order> filteredOrders = new ArrayList<>();
        ArrayList<Order> currentList;
        if ("all".equalsIgnoreCase(currentStatusFilter)) {
            currentList = OrderService.getAllOrders();
        } else {
            currentList = OrderService.getOrdersByStatus(currentStatusFilter);
        }
    
        if ("Email".equals(searchType)) {
            for (Order order : currentList) {
                User user = OrderService.getUserInfo(order.getUserID());
                if (user != null && user.getEmail() != null && user.getEmail().contains(searchTerm)) {
                    filteredOrders.add(order);
                }
            }
        } else {
            for (Order order : currentList) {
                if (String.valueOf(order.getOrderID()).contains(searchTerm)) {
                    filteredOrders.add(order);
                }
            }
        }
        updateTableModel(filteredOrders);
    }    

    private void loadOrderData(String statusFilter) {
        ArrayList<Order> filteredOrders;
        if ("all".equalsIgnoreCase(statusFilter)) {
            filteredOrders = OrderService.getAllOrders();
        } else {
            filteredOrders = OrderService.getOrdersByStatus(statusFilter);
        }
        currentStatusFilter = statusFilter;
        updateTableModel(filteredOrders);
    }
    
    private void updateTableModel(ArrayList<Order> orders) {
        OrderTableModel model = (OrderTableModel) orderTable.getModel();
        model.setOrders(orders);
        model.fireTableDataChanged();
    }   

    class OrderTableModel extends AbstractTableModel {
        private final String[] columnNames = {"OrderID", "Email","Create Time", "Update Time", "Total Cost", "Status", "Details"};
        private ArrayList<Order> orders = new ArrayList<>();
    
        public OrderTableModel() {
        }
    
        @Override
        public int getRowCount() {
            return orders.size();
        }
    
        @Override
        public int getColumnCount() {
            return columnNames.length;
        }
    
        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }
    
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Order order = orders.get(rowIndex);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
            switch (columnIndex) {
                case 0: return order.getOrderID();
                case 1: return OrderService.getUserInfo(order.getUserID()).getEmail();
                case 2: return dateFormat.format(order.getCreateTime());
                case 3: return dateFormat.format(order.getUpdateTime());
                case 4: return String.format("\u00A3%.2f", order.getTotalCost());
                case 5: return order.getStatus();
                case 6: return "Details";
                default: return null;
            }
        }
        
        public Order getOrderAt(int rowIndex) {
            return orders.get(rowIndex);
        }

        public void setOrders(ArrayList<Order> newOrders) {
            if (newOrders != null) {
                orders = newOrders;
            } else {
                orders.clear();
            }
            fireTableDataChanged();
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 6;
        }
    }        

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
    
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private boolean isPushed;
        private ButtonColumnListener listener;
        private int editingRow;
        private int editingColumn;
    
        public ButtonEditor(JCheckBox checkBox, ButtonColumnListener listener) {
            super(checkBox);
            this.button = new JButton();
            this.button.setOpaque(true);
            this.listener = listener;
            this.button.addActionListener(e -> {
                isPushed = true;
                fireEditingStopped();
            });
        }
    
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.editingRow = row;
            this.editingColumn = column;
            button.setText((value == null) ? "" : value.toString());
            return button;
        }
    
        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                if (listener != null) {
                    listener.onButtonClicked(editingRow, editingColumn);
                }
                isPushed = false;
            }
            return button.getText();
        }
    
        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
    
    public interface ButtonColumnListener {
        void onButtonClicked(int row, int column);
    }

    public static void main(String[] args) {
        User user = UserDAO.findUserByEmail("manager@manager.com");
        UserSession.getInstance().setCurrentUser(user);

        SwingUtilities.invokeLater(() -> {
            OrderManagePage frame = new OrderManagePage();
            frame.setVisible(true);
        });
    }
}
