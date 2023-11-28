package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.*;

import DAO.OrderDAO;
import DAO.UserDAO;
import exception.DatabaseException;
import helper.UserSession;
import model.Address;
import model.Order;
import model.Product;
import model.User;
import service.AddressService;
import service.OrderService;
import service.UserService;

public class OrderDetailPage extends JFrame {
    private Order order;

    public OrderDetailPage(Order order) {
        this.order = order;
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 600));
        setMinimumSize(new Dimension(600, 600));
    
        JPanel titlePanel = createTitlePanel();
    
        JPanel leftPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        leftPanel.add(createOrderDetailsPanel(), gbc);
        gbc.gridy++;
        if ("Cancelled".equals(order.getStatusString())){
            JButton seeCancelReasonButton = createCancelReasonButton();
            leftPanel.add(seeCancelReasonButton, gbc);
            gbc.gridy++;
        }
        leftPanel.add(createUserPanel(), gbc);
        gbc.gridy++;
        leftPanel.add(createAddressPanel(), gbc);
    
        JPanel rightPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        addOrderItemsToWindow(rightPanel, order);
    
        JScrollPane scrollPane = new JScrollPane(rightPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
    
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, scrollPane);
        splitPane.setResizeWeight(0.4); 
    
        add(titlePanel, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
    }
    
    private JPanel createTitlePanel() {
        JPanel panel = new JPanel();
        JLabel titleLabel = new JLabel("Order Details");
        setTextStyle(titleLabel, true, 18);
        panel.add(titleLabel);
        return panel;
    }
    
    private JPanel createOrderDetailsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.anchor = GridBagConstraints.WEST;
        
        gbc.gridy = 0;
        addLabelToPanel(panel, gbc, "Overview", "");
        gbc.gridy++;
        addLabelToPanel(panel, gbc, "Order ID:", String.valueOf(order.getOrderID()));
        gbc.gridy++;
        addLabelToPanel(panel, gbc, "Status:", order.getStatus().getStatus());
        gbc.gridy++;
        String bankDetailState = "";
        if (order.getBankDetailState()) {
            bankDetailState = "Offered";
        } else {
            bankDetailState = "Not Offered";
        }
        addLabelToPanel(panel, gbc, "Bank Detail:", bankDetailState);
        gbc.gridy++;
        addLabelToPanel(panel, gbc, "Create At:", order.getFormattedCreateTime());
        gbc.gridy++;
        addLabelToPanel(panel, gbc, "Update At:", order.getFormattedUpdateTime());
        gbc.gridy++;
        addLabelToPanel(panel, gbc, "Total Cost:", String.format("\u00A3%.2f", order.getTotalCost()));
        
        return panel;
    }

    private JPanel createUserPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridy = 0;
        addLabelToPanel(panel, gbc, "Customer: ", "");
        gbc.gridy++;
        User user = UserService.getUserInfo(order.getUserID()); 
        if (user.getForename() != null)
            addLabelToPanel(panel, gbc, "Forename:", user.getForename());
        else
            addLabelToPanel(panel, gbc, "Forename:","User Not Set");
        gbc.gridy++;
        if (user.getSurname() != null)
            addLabelToPanel(panel, gbc, "Surname:", user.getSurname());
        else
            addLabelToPanel(panel, gbc, "Surname:","User Not Set");
        gbc.gridy++;
        addLabelToPanel(panel, gbc, "Email:", user.getEmail());
    
        return panel;
    }
    
    private JPanel createAddressPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridy = 0;
        addLabelToPanel(panel, gbc, "Address:", "");
        gbc.gridy++;
        Address address = AddressService.getAddressDetailByID(order.getAddressID());
        addLabelToPanel(panel, gbc, "House Number:", address.getHouseNumber());
        gbc.gridy++;
        addLabelToPanel(panel, gbc, "Road Name:", address.getRoadName());
        gbc.gridy++;
        addLabelToPanel(panel, gbc, "City:", address.getCity());
        gbc.gridy++;
        addLabelToPanel(panel, gbc, "Post Code:", address.getPostcode());

        return panel;
    }

    private JButton createCancelReasonButton() {
        JButton seeButton = new JButton("See Cancel Reason");
        seeButton.setBackground(Color.WHITE);
        seeButton.setForeground(Color.BLACK);
        seeButton.addActionListener(e -> seeCancelReasonButtonClicked(e));
        return seeButton;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        Color darkGreen = new Color(0, 100, 0);
        JButton fulfillButton = new JButton("Fulfill");
        fulfillButton.setBackground(darkGreen);
        fulfillButton.setForeground(Color.WHITE);
        fulfillButton.addActionListener(e -> fulfillButtonMouseClicked(e));

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(Color.RED);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> cancelButtonMouseClicked(e));

        if ("Confirmed".equals(order.getStatus().getStatus())){
            buttonPanel.add(fulfillButton);
            buttonPanel.add(cancelButton);
        }

        JButton returnButton = new JButton("Return");
        returnButton.setBackground(Color.WHITE);
        returnButton.setForeground(Color.BLACK);
        buttonPanel.add(returnButton);
        returnButton.addActionListener(e -> {dispose();});
        
        return buttonPanel;
    }

    private JPanel createOrderItemPanel(Product product, int quantity) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // 设置边距
    
        String htmlContent = String.format(
            "<html><body style='text-align: left;'>"
            + "%s<br>"
            + "%s | %s<br>"
            + "\u00A3%.2f x %d<br>"
            + "<b>Line Cost:</b> \u00A3%.2f"
            + "</body></html>",
            product.getProductName(),
            product.getBrand(),
            product.getProductCode(),
            product.getRetailPrice(),
            quantity,
            product.getRetailPrice() * quantity
        );
    
        JLabel contentLabel = new JLabel(htmlContent);
        contentLabel.setFont(new Font(contentLabel.getFont().getName(), Font.PLAIN, 14));
        panel.add(contentLabel, BorderLayout.WEST);
    
        return panel;
    }

    private void addLabelToPanel(JPanel panel, GridBagConstraints gbc, String label, String value) {
        String formattedLabel = "<html><b>" + label + "</b></html>";
        String formattedValue = "<html>" + value + "</html>";
    
        JLabel labelComponent = new JLabel(formattedLabel);
        labelComponent.setFont(new Font(labelComponent.getFont().getName(), Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(labelComponent, gbc);
    
        gbc.insets.left = 20;
    
        JLabel valueComponent = new JLabel(formattedValue);
        valueComponent.setFont(new Font(valueComponent.getFont().getName(), Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(valueComponent, gbc);
    
        gbc.insets.left = 2;
    }
    
    private void addOrderItemsToWindow(JPanel rightPanel, Order order) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
    
        JLabel itemsTitleLabel = new JLabel("Order Items");
        setTextStyle(itemsTitleLabel, true, 16);
        rightPanel.add(itemsTitleLabel, gbc);
        gbc.gridy++;
    
        for (Map.Entry<Product, Integer> entry : order.getOrderItems().entrySet()) {
            JPanel itemPanel = createOrderItemPanel(entry.getKey(), entry.getValue());
            rightPanel.add(itemPanel, gbc);
            gbc.gridy++;
        }
    }
    

    private void setTextStyle(JLabel label, boolean isHeader, int fontSize) {
        label.setFont(new Font(label.getFont().getName(), isHeader ? Font.BOLD : Font.PLAIN, fontSize));
    }

    private void fulfillButtonMouseClicked(ActionEvent e) {
        if (OrderService.fulfillOrder(order)){
            JOptionPane.showMessageDialog(null, "Fulfill Order: "+ order.getOrderID() +" success!", "Success", JOptionPane.INFORMATION_MESSAGE);
            Order updateOrder = OrderService.findOrderByID(order.getOrderID());
            SwingUtilities.invokeLater(() -> {
                OrderDetailPage frame = new OrderDetailPage(updateOrder);
                frame.setVisible(true);
            });
            dispose();
        }
    }

    private void cancelButtonMouseClicked(ActionEvent e) {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        CancelOrderDialog cancelOrderDialog = new CancelOrderDialog(parentFrame, order,true);
        cancelOrderDialog.setVisible(true);

        if (cancelOrderDialog.isInputValid()) {
            Order updateOrder = OrderService.findOrderByID(order.getOrderID());
            SwingUtilities.invokeLater(() -> {
                OrderDetailPage frame = new OrderDetailPage(updateOrder);
                frame.setVisible(true);
            });
            dispose();
        }
    }

    private void seeCancelReasonButtonClicked(ActionEvent e) {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        CancelOrderDialog cancelOrderDialog = new CancelOrderDialog(parentFrame, order,false);
        cancelOrderDialog.setVisible(true);
    }

    public static void main(String[] args) {
        User user = UserDAO.findUserByEmail("manager@manager.com");
        UserSession.getInstance().setCurrentUser(user);
        Order order;
        try {
            order = OrderDAO.findOrderByID(4);

            SwingUtilities.invokeLater(() -> {
                OrderDetailPage frame = new OrderDetailPage(order);
                frame.setVisible(true);
            });
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

}
