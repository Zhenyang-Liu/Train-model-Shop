package gui;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import DAO.ControllerDAO;
import DAO.LocomotiveDAO;
import DAO.OrderDAO;
import DAO.RollingStockDAO;
import DAO.TrackDAO;
import DAO.UserDAO;
import helper.UserSession;

import java.awt.*;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import model.*;
import service.OrderService;
import service.ProductService;

public class StaffPage extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;

    public StaffPage() {
        setTitle("Staff Page");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createTopPanel(), BorderLayout.NORTH);

        mainPanel = new JPanel();
        cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);

        mainPanel.add(createProductPanel(), "Product");
        mainPanel.add(createOrderPanel(), "Order");

        add(mainPanel, BorderLayout.CENTER);
    }

    // Main Panel Constructor
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("Staff Page", JLabel.CENTER);
        topPanel.add(titleLabel, BorderLayout.NORTH);
    
        // Create Search Panel
        // TODO: Unfinished
        JPanel searchPanel = new JPanel();
        JTextField searchField = new JTextField(20);
        JButton searchButton1 = new JButton("Butto1");
        JButton searchButton2 = new JButton("Button2");
    
        // Button to switch the specific panel
        JButton productButton = new JButton("Product");
        JButton orderButton = new JButton("Order");
        productButton.addActionListener(e -> showProductPanel());
        orderButton.addActionListener(e -> showOrderPanel());
    
        // Search box
        searchPanel.add(searchField);
        searchPanel.add(searchButton1);
        searchPanel.add(searchButton2);
        searchPanel.add(productButton);
        searchPanel.add(orderButton);
    
        topPanel.add(searchPanel, BorderLayout.SOUTH);
    
        return topPanel;
    }
    
    private JPanel createProductPanel() {
        JPanel productPanel = new JPanel(new BorderLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        JButton addProductButton = new JButton("Add Product");
        // TODO: Add filter
        leftPanel.add(addProductButton);
        // TODO: filter plugin
        addProductButton.addActionListener(e -> addProduct());

        // Right Panel with Product Card
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // TODO: Load product data and create cards for each product
        ArrayList<Product> productList = ProductService.getAllProducts();
        for (Product product : productList) {
            cardPanel.add(createProductCard(product), gbc);
        }

        // Wrap the card panel with a scrolling panel
        JScrollPane scrollPane = new JScrollPane(cardPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        /// Add the left panel and scroll panel to the product panel
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, scrollPane);
        splitPane.setDividerLocation(150);
        productPanel.add(splitPane, BorderLayout.CENTER);

        return productPanel;
    }

    private JPanel createOrderPanel() {
        JPanel orderPanel = new JPanel(new BorderLayout());
        JPanel leftPanel = createOrderFilterPanel();
    
        OrderTableModel tableModel = new OrderTableModel(OrderService.getAllOrders());
        JTable orderTable = new JTable(tableModel);
    
        ButtonColumnListener listener = (row, column) -> {
            Order selectedOrder = tableModel.getOrderAt(row);

            SwingUtilities.invokeLater(() -> {
                OrderDetailPage frame = new OrderDetailPage(selectedOrder);
                frame.setVisible(true);
            });
        };
    
        orderTable.getColumn("Details").setCellRenderer(new ButtonRenderer());
        orderTable.getColumn("Details").setCellEditor(new ButtonEditor(new JCheckBox(), listener));
    
        JScrollPane scrollPane = new JScrollPane(orderTable);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, scrollPane);
        splitPane.setDividerLocation(150);
        orderPanel.add(splitPane, BorderLayout.CENTER);
    
        return orderPanel;
    }   

    //
    private JPanel createOrderFilterPanel(){
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(Box.createVerticalGlue());
        
        Dimension buttonSize = new Dimension(100, 30);
        JButton allButton = createButton("All", buttonSize, e -> loadOrderData("all"));
        JButton confirmedButton = createButton("Confirmed", buttonSize, e -> loadOrderData("confirmed"));
        JButton fulfilledButton = createButton("Fulfilled", buttonSize, e -> loadOrderData("fulfilled"));
    
        leftPanel.add(allButton);
        leftPanel.add(confirmedButton);
        leftPanel.add(fulfilledButton);
        leftPanel.add(Box.createVerticalGlue());

        return leftPanel;
    }

    private JPanel createProductCard(Product product) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
    
        card.add(new JLabel("ProductID: " + product.getProductID()));
        card.add(new JLabel("Brand: " + (product.getBrand() != null ? product.getBrand() : "Unknown")));
        card.add(new JLabel("Product Name: " + product.getProductName()));
        card.add(new JLabel("Product Code: " + product.getProductCode()));
        card.add(new JLabel("Retail Price: " + product.getRetailPrice()));
        card.add(new JLabel("Stock Quantity: " + product.getStockQuantity()));
    
        JButton detailsButton = new JButton("Detail");
        detailsButton.addActionListener(e -> showProductDetails(product));
        card.add(detailsButton);
    
        return card;
    }

    // Method about Product
    private void addProduct() {
        JDialog dialog = new JDialog(this, "Add Product", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST; 

        // Error Message
        JLabel errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        errorLabel.setVisible(false);
    
        // Panel 1 - Initial Input
        JPanel initialPanel = new JPanel(new GridBagLayout());
    
        JTextField productNameField = new JTextField(20);
        JTextField productCodeField = new JTextField(20);
        JTextField brandNameField = new JTextField(20);
        JTextField retailPriceField = new JTextField(20);
        JTextField stockQuantityField = new JTextField(20);
        JTextField descriptionField = new JTextField(20);
    
        initialPanel.add(new JLabel("Product Name:"), gbc);
        initialPanel.add(productNameField, gbc);
        initialPanel.add(new JLabel("Product Code:"), gbc);
        initialPanel.add(productCodeField, gbc);
        initialPanel.add(new JLabel("Brand:"), gbc);
        initialPanel.add(brandNameField, gbc);
        initialPanel.add(new JLabel("Retail Price:"), gbc);
        initialPanel.add(retailPriceField, gbc);
        initialPanel.add(new JLabel("Stock Quantity:"), gbc);
        initialPanel.add(stockQuantityField, gbc);
        initialPanel.add(new JLabel("Description:"), gbc);
        initialPanel.add(descriptionField, gbc);
    
        JButton nextButton = new JButton("Next");
        initialPanel.add(nextButton, gbc);
        initialPanel.add(errorLabel, gbc);
    
        // Second Panel - Additional Input (Initially empty)
        JPanel additionalPanel = new JPanel(new GridBagLayout());
        additionalPanel.setVisible(false);
    
        dialog.add(initialPanel, gbc);
        dialog.add(additionalPanel, gbc);
    
        // Event listener for the "Next" button
        nextButton.addActionListener(e -> {
            String productName = productNameField.getText();
            String productCode = productCodeField.getText().toUpperCase();
            String brandName = brandNameField.getText();
            String retailPrice = retailPriceField.getText();
            String stockQuantity = stockQuantityField.getText();
            String description = descriptionField.getText();
            char productTypeChar = productCode.charAt(0);

            String validationResult = ProductService.validateProductInput(productName, productCode, brandName, retailPrice, stockQuantity);
            if (validationResult != null) {
                errorLabel.setText(validationResult); 
                errorLabel.setVisible(true);
            } else if (productTypeChar == 'T' || productTypeChar == 'M'){
                // TODO: call the Add Boxset Panel
            } else {
                errorLabel.setVisible(false);
                Product product = new Product(brandName, productName, productCode, Double.parseDouble(retailPrice), description, Integer.parseInt(stockQuantityField.getText()), "");
                String productType = product.getProductType();

                showAdditionalOptions(additionalPanel, productType, dialog, product);
                nextButton.setVisible(false); 
                additionalPanel.setVisible(true);
            }
        });
    
        // Set dialog size
        dialog.pack(); 
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
        
    private void showProductDetails(Product product) {
        // TODO: Unfinished
    }

    private void showAdditionalOptions(JPanel additionalPanel, String productType, JDialog dialog, Product product) {
        additionalPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
    
        JComboBox<String> gaugeTypeComboBox = new JComboBox<>(new String[]{"OO", "TT", "N"});
        JComboBox<String> dccTypeComboBox = new JComboBox<>(new String[]{"Analogue", "Ready", "Fitted", "Sound"});
        JComboBox<String> digitalTypeComboBox = new JComboBox<>(new String[]{"Digital", "Analogue"});
        JComboBox<String> compartmentTypeComboBox = new JComboBox<>(new String[]{"Wagon", "Carriage"});
    
        additionalPanel.add(new JLabel("Product Type: " + productType), gbc);
    
        switch (productType) {
            case "Track":
                additionalPanel.add(new JLabel("Gauge:"), gbc);
                additionalPanel.add(gaugeTypeComboBox, gbc);
                break;
            case "Controller":
                additionalPanel.add(new JLabel("Digital Type: "), gbc);
                additionalPanel.add(digitalTypeComboBox, gbc);
                break;
            case "Locomotive":
                additionalPanel.add(new JLabel("Gauge:"), gbc);
                additionalPanel.add(gaugeTypeComboBox, gbc);
                additionalPanel.add(new JLabel("DCC Type: "), gbc);
                additionalPanel.add(dccTypeComboBox, gbc);
                break;
            case "Rolling Stock":
                additionalPanel.add(new JLabel("Gauge:"), gbc);
                additionalPanel.add(gaugeTypeComboBox, gbc);
                additionalPanel.add(new JLabel("Compartment Type: "), gbc);
                additionalPanel.add(compartmentTypeComboBox, gbc);
                break;
        }
    
        // Add a "Submit" button
        JButton finishButton = new JButton("Submit");
        finishButton.addActionListener(e -> {
            try{
                switch (productType) {
                    case "Track":
                        String selectedGauge = gaugeTypeComboBox.getSelectedItem().toString();
                        Track track = new Track(product, selectedGauge);
                        TrackDAO.insertTrack(track);
                        break;
                    case "Controller":
                        boolean selectedDigitalType = digitalTypeComboBox.getSelectedItem().equals("Digital");
                        Controller controller = new Controller(product, selectedDigitalType);
                        ControllerDAO.insertController(controller);
                        break;
                    case "Locomotive":
                        String selectedDccType = dccTypeComboBox.getSelectedItem().toString();
                        String selectedGaugeForLoco = gaugeTypeComboBox.getSelectedItem().toString();
                        int[] era = new int[]{1,3}; // TODO: era selection is unfinished
                        Locomotive locomotive = new Locomotive(product, selectedGaugeForLoco,selectedDccType,era);
                        LocomotiveDAO.insertLocomotive(locomotive);
                        break;
                    case "Rolling Stock":
                        String selectedCompartmentType = compartmentTypeComboBox.getSelectedItem().toString();
                        String selectedGaugeForRoll = gaugeTypeComboBox.getSelectedItem().toString();
                        int[] era1 = new int[]{1,3};// TODO: era selection is unfinished
                        RollingStock rollingStock = new RollingStock(product,selectedCompartmentType,selectedGaugeForRoll,era1);
                        RollingStockDAO.insertRollingStock(rollingStock);
                        break;
                }

            additionalPanel.getTopLevelAncestor().setVisible(false);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Failed to add product: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        additionalPanel.add(finishButton, gbc);
    
        additionalPanel.revalidate();
        additionalPanel.repaint();
    
        // Update dialog size
        dialog.pack();
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }    
   
    // Display Specific Panel
    private void showProductPanel() {
        cardLayout.show(mainPanel, "Product");
    }
    
    private void showOrderPanel() {
        cardLayout.show(mainPanel, "Order");
    }
    
    // Assistance Method
     private JButton createButton(String text, Dimension size, ActionListener listener) {
        JButton button = new JButton(text);
        button.setMinimumSize(size);
        button.setMaximumSize(size);
        button.setPreferredSize(size);
        button.addActionListener(listener);
        return button;
    }
   
    // Inner Class
    class OrderTableModel extends AbstractTableModel {
        private final String[] columnNames = {"OrderID", "UserID", "Create Time", "Update Time", "Total Cost", "Status", "Details"};
        private ArrayList<Order> orders;
    
        public OrderTableModel(ArrayList<Order> orders) {
            this.orders = orders;
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
                case 1: return order.getUserID();
                case 2: return dateFormat.format(order.getCreateTime());
                case 3: return dateFormat.format(order.getUpdateTime());
                case 4: return String.format("%.2f", order.getTotalCost());
                case 5: return order.getStatus();
                case 6: return "Details";
                default: return null;
            }
        }
        
        public Order getOrderAt(int rowIndex) {
            return orders.get(rowIndex);
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
        private String label;
        private boolean isPushed;
        private ButtonColumnListener listener;
        private int row, column;
    
        public ButtonEditor(JCheckBox checkBox, ButtonColumnListener listener) {
            super(checkBox);
            this.button = new JButton();
            this.button.setOpaque(true);
            this.listener = listener;
            this.button.addActionListener(e -> fireEditingStopped());
        }
    
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            this.row = row;
            this.column = column;
            return button;
        }
    
        public Object getCellEditorValue() {
            if (isPushed) {
                listener.onButtonClicked(row, column);
            }
            isPushed = false;
            return label;
        }
    
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
    
    public interface ButtonColumnListener {
        void onButtonClicked(int row, int column);
    }
    

    private void loadOrderData(String roleFilter) {

    }

    // Method for creating Buttons

    public static void main(String[] args) {
        User user = UserDAO.findUserByEmail("manager@manager.com");
        UserSession.getInstance().setCurrentUser(user);

        SwingUtilities.invokeLater(() -> {
            StaffPage frame = new StaffPage();
            frame.setVisible(true);
        });
    }
}
