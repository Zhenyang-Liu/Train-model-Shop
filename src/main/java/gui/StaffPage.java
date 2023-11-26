package gui;

import javax.swing.*;

import DAO.BoxedSetDAO;
import DAO.ControllerDAO;
import DAO.LocomotiveDAO;
import DAO.ProductDAO;
import DAO.RollingStockDAO;
import DAO.TrackDAO;
import DAO.UserDAO;
import exception.DatabaseException;
import helper.UserSession;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    // ------------------------------------------------------------------
    // Top Panel
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
    
    private void showProductPanel() {
        cardLayout.show(mainPanel, "Product");
    }
    
    private void showOrderPanel() {
        cardLayout.show(mainPanel, "Order");
    }
    
    // ------------------------------------------------------------------
    // Product Panel
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
            String productCode = productCodeField.getText();
            String brandName = brandNameField.getText();
            String retailPrice = retailPriceField.getText();
            String stockQuantity = stockQuantityField.getText();
            String description = descriptionField.getText();

            String validationResult = ProductService.validateProductInput(productName, productCode, brandName, retailPrice, stockQuantity);
            if (validationResult != null) {
                errorLabel.setText(validationResult); 
                errorLabel.setVisible(true);
            } else {
                errorLabel.setVisible(false);
                Product product = new Product(brandName, productName, productCode, Double.parseDouble(retailPrice), description, Integer.parseInt(stockQuantityField.getText()));
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
    
    // TODO: ADD BoxedSet Unfinished
    private void showAdditionalOptions(JPanel additionalPanel, String productType, JDialog dialog, Product product) {
        additionalPanel.removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        String[] gaugeTypes = {"OO", "TT", "N"};
        JComboBox<String> gaugeTypeComboBox = new JComboBox<>(gaugeTypes);

        Map<String, String> tooltips = new HashMap<>();
        tooltips.put("OO", "1/76th scale");
        tooltips.put("TT", "1/120th scale");
        tooltips.put("N", "1/148th scale");
        gaugeTypeComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JComponent comp = (JComponent) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                // Set the tooltip based on the selected option
                String tooltipText = tooltips.get(value);
                comp.setToolTipText(tooltipText);

                return comp;
            }
        });

        String[] dccTypes = {"Analogue", "Ready", "Fitted", "Sound"};
        JComboBox<String> dccTypeComboBox = new JComboBox<>(dccTypes);

        String[] digitalTypes = {"Digital", "Analogue"};
        JComboBox<String> digitalTypeComboBox = new JComboBox<>(digitalTypes);

        String[] compartmentTypes = {"Wagon", "Carriage"};
        JComboBox<String> compartmentTypeComboBox = new JComboBox<>(compartmentTypes);

        additionalPanel.add(new JLabel("Product Type: "+productType), gbc);

        switch (productType) {
            case "Track":
                additionalPanel.add(new JLabel("Gauge:"), gbc);
                additionalPanel.add(gaugeTypeComboBox, gbc);
                break;
            case "Controller":
                additionalPanel.add(new JLabel("Digital Type: "), gbc);
                additionalPanel.add(digitalTypeComboBox, gbc);
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
            case "Train Set":
                break;
            case "Track Pack":
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
                    int[] era = new int[]{1,3};
                    Locomotive locomotive = new Locomotive(product, selectedGaugeForLoco,selectedDccType,era);
                    LocomotiveDAO.insertLocomotive(locomotive);
                    break;
                case "Rolling Stock":
                    String selectedCompartmentType = compartmentTypeComboBox.getSelectedItem().toString();
                    String selectedGaugeForRoll = gaugeTypeComboBox.getSelectedItem().toString();
                    int[] era1 = new int[]{1,3};
                    RollingStock rollingStock = new RollingStock(product,selectedCompartmentType,selectedGaugeForRoll,era1);
                    RollingStockDAO.insertRollingStock(rollingStock);
                    break;
                case "Train Set":
                    BoxedSet trainSet = new BoxedSet(product, productType);
                    BoxedSetDAO.insertBoxedSet(trainSet);
                    break;
                case "Track Pack":
                    BoxedSet trackPack = new BoxedSet(product, productType);
                    BoxedSetDAO.insertBoxedSet(trackPack);
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

    // TODO: wait for a nicer look
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
    

    private void showProductDetails(Product product) {
        // TODO: Unfinished
    }


    // ------------------------------------------------------------------
    // Order Panel
    private JPanel createOrderPanel() {
        JPanel orderPanel = new JPanel(new BorderLayout());

        JPanel leftPanel = createLeftOrderPanel();
        JPanel rightPanel = createRightOrderPanel();
    
        JScrollPane scrollPane = new JScrollPane(rightPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, scrollPane);
        splitPane.setDividerLocation(150);
        orderPanel.add(splitPane, BorderLayout.CENTER);
    
        return orderPanel;
    }

    private JPanel createLeftOrderPanel(){
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

    private JPanel createRightOrderPanel() {
        JPanel rightPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        ArrayList<Order> orderList = OrderService.getAllOrders();
        // TODO: loading order data and create a card for each order
        for (Order order : orderList) {
            rightPanel.add(createOrderCard(order), gbc);
        }
        return rightPanel;
    }
    
    // TODO: wait for a nicer look
    private JPanel createOrderCard(Order order) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        card.add(new JLabel("OrderID: " + order.getOrderID()));
        card.add(new JLabel("UserID: " + order.getUserID()));
        card.add(new JLabel("AddressID: " + order.getAddressID()));
        card.add(new JLabel("Create Time: " + order.getCreateTime()));
        card.add(new JLabel("Update Time: " + order.getUpdateTime()));
        card.add(new JLabel("Total Cost: " + order.getTotalCost()));
        card.add(new JLabel("Status: " + order.getStatus()));
    
        // Show the itemList in the Order
        if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
            card.add(new JLabel("Order List:"));
            for (Map.Entry<Product, Integer> entry : order.getOrderItems().entrySet()) {
                Product product = entry.getKey();
                Integer quantity = entry.getValue();
                card.add(new JLabel("- " + product.getProductName() + " (Quantity: " + quantity + ")"));
            }
        }
    
        // Detail Button
        JButton detailsButton = new JButton("Detail");
        detailsButton.addActionListener(e -> showOrderDetails(order));
        card.add(detailsButton);
    
        return card;
    }
    
    private void showOrderDetails(Order order) {
        // TODO：Unfinished
    }

    private void loadOrderData(String roleFilter) {

    }

    // Method for creating Buttons
    private JButton createButton(String text, Dimension size, ActionListener listener) {
        JButton button = new JButton(text);
        button.setMinimumSize(size);
        button.setMaximumSize(size);
        button.setPreferredSize(size);
        button.addActionListener(listener);
        return button;
    }

    public static void main(String[] args) {
        User user = UserDAO.findUserByEmail("manager@manager.com");
        UserSession.getInstance().setCurrentUser(user);

        SwingUtilities.invokeLater(() -> {
            StaffPage frame = new StaffPage();
            frame.setVisible(true);
        });
    }
}
