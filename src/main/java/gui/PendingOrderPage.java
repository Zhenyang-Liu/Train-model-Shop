/*
 * Created by JFormDesigner on Sun Nov 26 15:56:31 GMT 2023
 */

package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

import DAO.OrderDAO;
import DAO.UserDAO;
import exception.DatabaseException;
import helper.UserSession;
import model.Address;
import model.BankDetail;
import model.Order;
import model.Product;
import model.User;
import service.AddressService;
import service.BankDetailService;
import service.CartService;
import service.OrderService;

/**
 * @author Zhenyang Liu
 * @author Jiawei Jiang
 */
public class PendingOrderPage extends JFrame {
    public PendingOrderPage(Order order) {
        this.order = order;
        initComponents();
        addProductCards();
        updateAddressPanel();
        updatePaymentPanel();
        updateCVVPanel();
        totalPrice.setText(String.format("\u00A3%.2f", order.getTotalCost()));
    }

    private void confirmButtonMouseClicked(MouseEvent e) {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (order.getOrderItems().isEmpty()) {
            this.dispose();
            int confirmCancel = JOptionPane.showConfirmDialog(this,"This order is empty. Do you want to cancel the order?",
                "Confirm Cancellation",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
            if (confirmCancel == JOptionPane.YES_OPTION){
                OrderService.cancelOrder(order, "Empty Order");
            }
            dispose();
            return;
        }
        if (!isAddressExist) {
            JOptionPane.showMessageDialog(parentFrame, "Please add an address", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!isPaymentExist) {
            JOptionPane.showMessageDialog(parentFrame, "Please add a payment card", "Error", JOptionPane.ERROR_MESSAGE);
        } else if(!isCVVProvided){
            JOptionPane.showMessageDialog(parentFrame, "Please enter security code", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            order.setAddressID(address.getID());
            order.setBankDetailState(isPaymentExist);
            order.nextStatus();
            if (OrderService.confirmOrder(order)){
                this.dispose();
                JOptionPane.showMessageDialog(parentFrame,
                        "Your order has been successfully placed.",
                        "Order Confirmed",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parentFrame,
                        "Meet an error when confirming the order. Please try again later.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void cancelButtonMouseClicked(MouseEvent e) {
        int confirmCancel = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to cancel the order?",
            "Confirm Cancellation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
    
        if (confirmCancel == JOptionPane.YES_OPTION) {
            CartService.restoreStock(order.getOrderItems());

            int confirmReturnToCart = JOptionPane.showConfirmDialog(
                this,
                "Do you want to move the items back to the shopping cart?",
                "Return to Cart",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
    
            if (confirmReturnToCart == JOptionPane.YES_OPTION) {
                OrderService.returnToCart(order);
            } 
            OrderService.cancelOrder(order, "Self Cancelled");
            dispose();
        }
    }
    
    private void addressAddButtonMouseClicked(MouseEvent e) {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        AddressDialog addressDialog = new AddressDialog(parentFrame, null,false);
        addressDialog.setVisible(true);

        if (addressDialog.isInputValid()) {
            updateAddressPanel();
        }
    }

    private void addressEditButtonMouseClicked(MouseEvent e) {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        AddressDialog addressDialog = new AddressDialog(parentFrame,address,true);
        addressDialog.setVisible(true);

        if (addressDialog.isInputValid()) {
            updateAddressPanel();
        }
    }

    private void paymentAddButtonMouseClicked(MouseEvent e) {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        BankDetailDialog bankDetailDialog = new BankDetailDialog(parentFrame,bankDetail,false);
        bankDetailDialog.setVisible(true);

        if (bankDetailDialog.isInputValid()) {
            updatePaymentPanel();
        }
    }

    private void paymentEditButtonMouseClicked(MouseEvent e) {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        BankDetailDialog bankDetailDialog = new BankDetailDialog(parentFrame,bankDetail,true);
        bankDetailDialog.setVisible(true);

        if (bankDetailDialog.isInputValid()) {
            isCVVProvided = false;
            updatePaymentPanel();
            updateCVVPanel();
        }
    }

    private void cvvAddButtonMouseClicked(MouseEvent e) {
        String cvv = JOptionPane.showInputDialog(this, "Enter CVV:");
        if (cvv == null || cvv.isEmpty()) {
            JOptionPane.showMessageDialog(this, "CVV is required", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!isValidCVV(cvv)) {
            JOptionPane.showMessageDialog(this, "Invalid CVV", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            isCVVProvided = true;
            updateCVVPanel();
        }
    }

    private boolean isValidCVV(String cvv) {
        return cvv.matches("\\d{3}");
    }
    
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        ResourceBundle bundle = ResourceBundle.getBundle("gui.form");
        pendingOrderTitleLabel = new JLabel();
        pendingOrderContentPanel = new JPanel();
        infoPanel = new JPanel();
        customerInfoPanel = new JPanel();
        addressPanel = new JPanel();
        addressLabel = new JLabel();
        addressText = new JLabel();
        addressAddButton = new JButton();
        addressEditButton = new JButton();
        paymentPanel = new JPanel();
        paymentLabel = new JLabel();
        paymentText = new JLabel();
        paymentAddButton = new JButton();
        paymentEditButton = new JButton();
        cvvPanel = new JPanel();
        cvvLabel = new JLabel();
        cvvText = new JLabel();
        cvvEnterButton = new JButton();
        pendingOrderScrollPanel = new JScrollPane();
        pendingOrderItemsPanel = new JPanel();
        pendingOrderCardPanel = new JPanel();
        itemImage1 = new JLabel();
        itemName1 = new JLabel();
        itemPrice1 = new JLabel();
        itemNumLabel1 = new JLabel();
        bottomPanel = new JPanel();
        paymentAmountPanel = new JPanel();
        subtotalLabel = new JLabel();
        totalPrice = new JLabel();
        pendingOrderButtonPanel = new JPanel();
        cancelButton = new JButton();
        confirmButton = new JButton();

        //======== this ========
        setPreferredSize(new Dimension(900, 700));
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //---- pendingOrderTitleLabel ----
        pendingOrderTitleLabel.setText(bundle.getString("pendingOrderPage.pendingOrderTitleLabel.text"));
        pendingOrderTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pendingOrderTitleLabel.setFont(pendingOrderTitleLabel.getFont().deriveFont(pendingOrderTitleLabel.getFont().getStyle() | Font.BOLD, pendingOrderTitleLabel.getFont().getSize() + 13f));
        pendingOrderTitleLabel.setForeground(new Color(0x003366));
        pendingOrderTitleLabel.setBorder(new EmptyBorder(10, 5, 10, 5));
        contentPane.add(pendingOrderTitleLabel, BorderLayout.NORTH);

        //======== pendingOrderContentPanel ========
        {
            pendingOrderContentPanel.setLayout(new GridBagLayout());
            ((GridBagLayout)pendingOrderContentPanel.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0};
            ((GridBagLayout)pendingOrderContentPanel.getLayout()).rowHeights = new int[] {0, 0};
            ((GridBagLayout)pendingOrderContentPanel.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0E-4};
            ((GridBagLayout)pendingOrderContentPanel.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

            //======== infoPanel ========
            {
                infoPanel.setLayout(new GridBagLayout());
                ((GridBagLayout)infoPanel.getLayout()).columnWidths = new int[] {0, 0};
                ((GridBagLayout)infoPanel.getLayout()).rowHeights = new int[] {0, 0};
                ((GridBagLayout)infoPanel.getLayout()).columnWeights = new double[] {0.0, 1.0E-4};
                ((GridBagLayout)infoPanel.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

                //======== customerInfoPanel ========
                {
                    customerInfoPanel.setLayout(new GridBagLayout());
                    ((GridBagLayout)customerInfoPanel.getLayout()).columnWidths = new int[] {0, 0};
                    ((GridBagLayout)customerInfoPanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
                    ((GridBagLayout)customerInfoPanel.getLayout()).columnWeights = new double[] {0.0, 1.0E-4};
                    ((GridBagLayout)customerInfoPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

                    //======== addressPanel ========
                    {
                        addressPanel.setMaximumSize(new Dimension(300, 200));
                        addressPanel.setMinimumSize(new Dimension(260, 150));
                        addressPanel.setLayout(new BoxLayout(addressPanel, BoxLayout.Y_AXIS));

                        //---- addressLabel ----
                        addressLabel.setText("Address");
                        addressLabel.setForeground(new Color(0x003366));
                        addressLabel.setFont(addressLabel.getFont().deriveFont(addressLabel.getFont().getStyle() | Font.BOLD, addressLabel.getFont().getSize() + 5f));
                        addressPanel.add(addressLabel);

                        //---- addressText ----
                        addressText.setMinimumSize(new Dimension(100, 200));
                        addressPanel.add(addressText);

                        //---- addressAddButton ----
                        addressAddButton.setText("Add a new address");
                        addressAddButton.setForeground(new Color(0xe9e4e3));
                        addressAddButton.setBackground(new Color(0x55a15a));
                        addressAddButton.setFont(addressAddButton.getFont().deriveFont(addressAddButton.getFont().getStyle() & ~Font.BOLD));
                        addressAddButton.setPreferredSize(new Dimension(140, 23));
                        addressAddButton.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                addressAddButtonMouseClicked(e);
                            }
                        });
                        addressPanel.add(addressAddButton);

                        //---- addressEditButton ----
                        addressEditButton.setText("Edit the address");
                        addressEditButton.setBackground(new Color(0x00a5f3));
                        addressEditButton.setForeground(new Color(0xe9e4e3));
                        addressEditButton.setPreferredSize(new Dimension(140, 23));
                        addressEditButton.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                addressEditButtonMouseClicked(e);
                            }
                        });
                        addressPanel.add(addressEditButton);
                    }
                    customerInfoPanel.add(addressPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                        new Insets(0, 0, 5, 0), 0, 0));

                    //======== paymentPanel ========
                    {
                        paymentPanel.setMaximumSize(new Dimension(300, 200));
                        paymentPanel.setMinimumSize(new Dimension(260, 150));
                        paymentPanel.setLayout(new BoxLayout(paymentPanel, BoxLayout.Y_AXIS));

                        //---- paymentLabel ----
                        paymentLabel.setText("Payment method");
                        paymentLabel.setForeground(new Color(0x003366));
                        paymentLabel.setFont(paymentLabel.getFont().deriveFont(paymentLabel.getFont().getStyle() | Font.BOLD, paymentLabel.getFont().getSize() + 5f));
                        paymentPanel.add(paymentLabel);

                        //---- paymentText ----
                        paymentText.setMinimumSize(new Dimension(100, 100));
                        paymentText.setMaximumSize(null);
                        paymentPanel.add(paymentText);

                        //---- paymentAddButton ----
                        paymentAddButton.setText("Add a new card");
                        paymentAddButton.setForeground(new Color(0xe9e4e3));
                        paymentAddButton.setBackground(new Color(0x55a15a));
                        paymentAddButton.setFont(paymentAddButton.getFont().deriveFont(paymentAddButton.getFont().getStyle() & ~Font.BOLD));
                        paymentAddButton.setPreferredSize(new Dimension(140, 23));
                        paymentAddButton.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                paymentAddButtonMouseClicked(e);
                            }
                        });
                        paymentPanel.add(paymentAddButton);

                        //---- paymentEditButton ----
                        paymentEditButton.setText("Edit the payment");
                        paymentEditButton.setBackground(new Color(0x00a5f3));
                        paymentEditButton.setForeground(new Color(0xe9e4e3));
                        paymentEditButton.setPreferredSize(new Dimension(140, 23));
                        paymentEditButton.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                paymentEditButtonMouseClicked(e);
                            }
                        });
                        paymentPanel.add(paymentEditButton);
                    }
                    customerInfoPanel.add(paymentPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                        new Insets(20, 0, 5, 0), 0, 0));

                    //======== cvvPanel ========
                    {
                        cvvPanel.setMaximumSize(new Dimension(300, 200));
                        cvvPanel.setMinimumSize(new Dimension(260, 150));
                        cvvPanel.setLayout(new BoxLayout(cvvPanel, BoxLayout.Y_AXIS));

                        //---- cvvLabel ----
                        cvvLabel.setText("CVV");
                        cvvLabel.setForeground(new Color(0x003366));
                        cvvLabel.setFont(cvvLabel.getFont().deriveFont(cvvLabel.getFont().getStyle() | Font.BOLD, cvvLabel.getFont().getSize() + 5f));
                        cvvPanel.add(cvvLabel);

                        //---- cvvText ----
                        cvvText.setMinimumSize(new Dimension(100, 100));
                        cvvText.setMaximumSize(null);
                        cvvPanel.add(cvvText);

                        //---- cvvEnterButton ----
                        cvvEnterButton.setText("Enter CVV");
                        cvvEnterButton.setForeground(new Color(0xe9e4e3));
                        cvvEnterButton.setBackground(new Color(0x55a15a));
                        cvvEnterButton.setFont(cvvEnterButton.getFont().deriveFont(cvvEnterButton.getFont().getStyle() & ~Font.BOLD));
                        cvvEnterButton.setPreferredSize(new Dimension(140, 23));
                        cvvEnterButton.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseClicked(MouseEvent e) {
                                cvvAddButtonMouseClicked(e);
                            }
                        });
                        cvvPanel.add(cvvEnterButton);
                    }
                    customerInfoPanel.add(cvvPanel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                        new Insets(20, 0, 0, 0), 0, 0));
                }
                infoPanel.add(customerInfoPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 10, 0, 20), 0, 0));
            }
            pendingOrderContentPanel.add(infoPanel, new GridBagConstraints(0, 0, 1, 1, 0.8, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 5), 0, 0));

            //======== pendingOrderScrollPanel ========
            {
                pendingOrderScrollPanel.setPreferredSize(new Dimension(620, 200));
                pendingOrderScrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                pendingOrderScrollPanel.setMaximumSize(new Dimension(620, 32767));
                pendingOrderScrollPanel.setMinimumSize(new Dimension(620, 6));

                //======== pendingOrderItemsPanel ========
                {
                    pendingOrderItemsPanel.setMaximumSize(new Dimension(610, 32767));
                    pendingOrderItemsPanel.setMinimumSize(new Dimension(610, 1096));
                    pendingOrderItemsPanel.setLayout(new GridBagLayout());
                    ((GridBagLayout)pendingOrderItemsPanel.getLayout()).columnWidths = new int[] {0, 0};
                    ((GridBagLayout)pendingOrderItemsPanel.getLayout()).rowHeights = new int[] {0, 0};
                    ((GridBagLayout)pendingOrderItemsPanel.getLayout()).columnWeights = new double[] {0.0, 1.0E-4};
                    ((GridBagLayout)pendingOrderItemsPanel.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

                    //======== pendingOrderCardPanel ========
                    {
                        pendingOrderCardPanel.setBorder(new MatteBorder(3, 3, 3, 3, new Color(0x003762)));
                        pendingOrderCardPanel.setForeground(new Color(0x003366));
                        pendingOrderCardPanel.setPreferredSize(new Dimension(600, 120));
                        pendingOrderCardPanel.setMaximumSize(new Dimension(2147483647, 100));
                        pendingOrderCardPanel.setLayout(new BoxLayout(pendingOrderCardPanel, BoxLayout.X_AXIS));

                        //---- itemImage1 ----
                        itemImage1.setText(bundle.getString("pendingOrderPage.itemImage1.text"));
                        itemImage1.setIcon(new ImageIcon(getClass().getResource("/images/tgv.jpeg")));
                        itemImage1.setPreferredSize(new Dimension(150, 120));
                        itemImage1.setMaximumSize(new Dimension(160, 120));
                        itemImage1.setMinimumSize(new Dimension(160, 120));
                        pendingOrderCardPanel.add(itemImage1);

                        //---- itemName1 ----
                        itemName1.setText(bundle.getString("pendingOrderPage.itemName1.text"));
                        itemName1.setHorizontalAlignment(SwingConstants.CENTER);
                        itemName1.setPreferredSize(new Dimension(200, 17));
                        itemName1.setFont(itemName1.getFont().deriveFont(itemName1.getFont().getSize() + 2f));
                        itemName1.setMinimumSize(new Dimension(200, 19));
                        itemName1.setMaximumSize(new Dimension(200, 19));
                        pendingOrderCardPanel.add(itemName1);

                        //---- itemPrice1 ----
                        itemPrice1.setText(bundle.getString("pendingOrderPage.itemPrice1.text"));
                        itemPrice1.setHorizontalAlignment(SwingConstants.CENTER);
                        itemPrice1.setFont(itemPrice1.getFont().deriveFont(itemPrice1.getFont().getSize() + 4f));
                        itemPrice1.setPreferredSize(new Dimension(100, 22));
                        itemPrice1.setMaximumSize(new Dimension(150, 22));
                        itemPrice1.setMinimumSize(new Dimension(150, 22));
                        pendingOrderCardPanel.add(itemPrice1);

                        //---- itemNumLabel1 ----
                        itemNumLabel1.setText("1");
                        itemNumLabel1.setHorizontalAlignment(SwingConstants.CENTER);
                        itemNumLabel1.setMaximumSize(new Dimension(100, 17));
                        itemNumLabel1.setMinimumSize(new Dimension(100, 17));
                        itemNumLabel1.setPreferredSize(new Dimension(100, 17));
                        pendingOrderCardPanel.add(itemNumLabel1);
                    }
                    pendingOrderItemsPanel.add(pendingOrderCardPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                        new Insets(0, 0, 0, 0), 0, 0));
                }
                pendingOrderScrollPanel.setViewportView(pendingOrderItemsPanel);
            }
            pendingOrderContentPanel.add(pendingOrderScrollPanel, new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 0, 5), 0, 0));
        }
        contentPane.add(pendingOrderContentPanel, BorderLayout.CENTER);

        //======== bottomPanel ========
        {
            bottomPanel.setLayout(new GridBagLayout());
            ((GridBagLayout)bottomPanel.getLayout()).columnWidths = new int[] {0, 0};
            ((GridBagLayout)bottomPanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
            ((GridBagLayout)bottomPanel.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
            ((GridBagLayout)bottomPanel.getLayout()).rowWeights = new double[] {1.0, 0.0, 0.0, 1.0E-4};

            //======== paymentAmountPanel ========
            {
                paymentAmountPanel.setLayout(new FlowLayout());

                //---- subtotalLabel ----
                subtotalLabel.setText("Subtotal:");
                subtotalLabel.setFont(subtotalLabel.getFont().deriveFont(subtotalLabel.getFont().getSize() + 3f));
                paymentAmountPanel.add(subtotalLabel);

                //---- totalPrice ----
                totalPrice.setText("100");
                totalPrice.setFont(totalPrice.getFont().deriveFont(totalPrice.getFont().getStyle() | Font.BOLD, totalPrice.getFont().getSize() + 4f));
                paymentAmountPanel.add(totalPrice);
            }
            bottomPanel.add(paymentAmountPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

            //======== pendingOrderButtonPanel ========
            {
                pendingOrderButtonPanel.setLayout(new FlowLayout());

                //---- cancelButton ----
                cancelButton.setText("Cancel order");
                cancelButton.setPreferredSize(new Dimension(150, 40));
                cancelButton.setBackground(new Color(0xd54945));
                cancelButton.setForeground(new Color(0xe9e4e3));
                cancelButton.setFont(cancelButton.getFont().deriveFont(cancelButton.getFont().getStyle() | Font.BOLD, cancelButton.getFont().getSize() + 2f));
                cancelButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        cancelButtonMouseClicked(e);
                    }
                });
                pendingOrderButtonPanel.add(cancelButton);

                //---- confirmButton ----
                confirmButton.setText("Confirm order");
                confirmButton.setPreferredSize(new Dimension(150, 40));
                confirmButton.setBackground(new Color(0x55a15a));
                confirmButton.setForeground(new Color(0xe9e4e3));
                confirmButton.setFont(confirmButton.getFont().deriveFont(confirmButton.getFont().getStyle() | Font.BOLD, confirmButton.getFont().getSize() + 2f));
                confirmButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        confirmButtonMouseClicked(e);
                    }
                });
                pendingOrderButtonPanel.add(confirmButton);
            }
            bottomPanel.add(pendingOrderButtonPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        }
        contentPane.add(bottomPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    private String formatCardNumber(String cardNumber) {
        return cardNumber.replaceAll("....", "$0 ");
    }

    private void updatePaymentPanel() {
        this.bankDetail = BankDetailService.findBankDetail();
        if (bankDetail != null){
            String formattedText = "<html><body>"
                + bankDetail.getCardName() + "</b><br>"
                + formatCardNumber(bankDetail.getCardNumber()) + "</b><br>"
                + bankDetail.getCardHolderName() + "</b><br>"
                + bankDetail.getExpiryDate() + "   " 
                + "</b><br>"
                + "</b><br>"+"</body></html>";
            paymentText.setText(formattedText);
            isPaymentExist = true;
        } else {
            paymentEditButton.setVisible(false);
            paymentAddButton.setText("Add a Card");
            isPaymentExist = false;
        }

        if (isPaymentExist) {
            paymentEditButton.setVisible(true);
            paymentAddButton.setVisible(false);
        }
    }

    private void updateAddressPanel() {
        
        //---- AddressTextField ----
        this.address = AddressService.getAddressByUser();
        if (!AddressService.isAddressEmpty(address)){
            String formattedText = "<html><body>"
                + address.getHouseNumber() + "</b><br>"
                + address.getRoadName() + "</b><br>"
                + address.getCity()+ "</b><br>"
                + address.getPostcode()+ "</b><br>"
                + "</b><br>"+"</body></html>";
            addressText.setText(formattedText);
            isAddressExist = true;
        } else {
            //---- addressAddButton ----
            addressAddButton.setVisible(true);
            addressEditButton.setVisible(false);
            isAddressExist = false;
        }
        //======== addressEditPanel ========
        if (isAddressExist) {
            //---- addressEditButton ----
            addressAddButton.setVisible(false);
            addressEditButton.setVisible(true);
        }
    }
    
    private void updateCVVPanel() {
        if (!isPaymentExist){
            isCVVProvided = false;
        }
        if (!isCVVProvided){
            cvvText.setText("CVV: Empty");
            cvvText.setForeground(Color.RED);
        } else{
            cvvText.setText("CVV: Entered");
            cvvText.setForeground(new Color(0x008000)); // Green color
        }
        cvvEnterButton.setVisible(!isCVVProvided);
    }
    

    private JPanel createProductCard(Product product, int quantity) {
        JPanel cardPanel = new JPanel();
        cardPanel.setBorder(new MatteBorder(3, 3, 3, 3, new Color(0x003762)));
        cardPanel.setPreferredSize(new Dimension(590, 120));
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.X_AXIS));

        JLabel itemImage = new JLabel();
        itemImage.setIcon(new ImageIcon(getClass().getResource("/images/tgv.jpeg")));
        itemImage.setPreferredSize(new Dimension(160, 120));
        itemImage.setMaximumSize(new Dimension(160, 120));
        cardPanel.add(itemImage);

        JLabel itemName = new JLabel(product.getProductName());
        itemName.setHorizontalAlignment(SwingConstants.CENTER);
        itemName.setPreferredSize(new Dimension(200, 17));
        itemName.setMinimumSize(new Dimension(200, 19));
        itemName.setMaximumSize(new Dimension(200, 19));
        itemName.setFont(new Font("Arial", Font.PLAIN, 12));
        cardPanel.add(itemName);

        JLabel itemPrice = new JLabel(String.format("\u00A3%.2f", product.getRetailPrice()));
        itemPrice.setHorizontalAlignment(SwingConstants.CENTER);
        itemPrice.setPreferredSize(new Dimension(100, 22));
        itemPrice.setMinimumSize(new Dimension(150, 22));
        itemPrice.setMaximumSize(new Dimension(150, 22));
        itemPrice.setFont(new Font("Arial", Font.PLAIN, 14));
        cardPanel.add(itemPrice);

        JLabel itemNumLabel = new JLabel(String.valueOf(quantity));
        itemNumLabel.setHorizontalAlignment(SwingConstants.CENTER);
        itemNumLabel.setPreferredSize(new Dimension(100, 17));
        itemNumLabel.setMinimumSize(new Dimension(100, 17));
        itemNumLabel.setMaximumSize(new Dimension(100, 17));
        cardPanel.add(itemNumLabel);

        return cardPanel;
    }


    private void addProductCards() {
        pendingOrderItemsPanel.removeAll();
        Map<Product, Integer> products = order.getOrderItems();
        pendingOrderItemsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
    
        int row = 0;
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            JPanel cardPanel = createProductCard(entry.getKey(), entry.getValue());
            gbc.gridy = row++;
            gbc.weighty = row < products.size() ? 0 : 1;
            gbc.insets = new Insets(3, 5, 0, 5);
            pendingOrderItemsPanel.add(cardPanel, gbc);
        }
    }
    public static void main(String[] args) throws DatabaseException {
         User user = UserDAO.findUserByEmail("manager@manager.com");
        // User user = UserDAO.findUserByEmail("staff@gmail.com");
        //User user = UserDAO.findUserByEmail("testemail@gmail.com");
        UserSession.getInstance().setCurrentUser(user);
        Order order = OrderDAO.findOrderByID(13);

        SwingUtilities.invokeLater(() -> {
            PendingOrderPage frame = new PendingOrderPage(order);
            frame.setVisible(true);
        });
    }
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JLabel pendingOrderTitleLabel;
    private JPanel pendingOrderContentPanel;
    private JPanel infoPanel;
    private JPanel customerInfoPanel;
    private JPanel addressPanel;
    private JLabel addressLabel;
    private JLabel addressText;
    private JButton addressAddButton;
    private JButton addressEditButton;
    private JPanel paymentPanel;
    private JLabel paymentLabel;
    private JLabel paymentText;
    private JButton paymentAddButton;
    private JButton paymentEditButton;
    private JPanel cvvPanel;
    private JLabel cvvLabel;
    private JLabel cvvText;
    private JButton cvvEnterButton;
    private JScrollPane pendingOrderScrollPanel;
    private JPanel pendingOrderItemsPanel;
    private JPanel pendingOrderCardPanel;
    private JLabel itemImage1;
    private JLabel itemName1;
    private JLabel itemPrice1;
    private JLabel itemNumLabel1;
    private JPanel bottomPanel;
    private JPanel paymentAmountPanel;
    private JLabel subtotalLabel;
    private JLabel totalPrice;
    private JPanel pendingOrderButtonPanel;
    private JButton cancelButton;
    private JButton confirmButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on

    private BankDetail bankDetail;
    private Address address;
    private Order order;
    private boolean isPaymentExist;
    private boolean isAddressExist;
    private boolean isCVVProvided;
}
