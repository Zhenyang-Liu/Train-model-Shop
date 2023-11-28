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
        updateAddressPanel();
        updatePaymentPanel();
    }

    private void confirmButtonMouseClicked(MouseEvent e) {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
    
        if (!isAddressExist) {
            JOptionPane.showMessageDialog(parentFrame, "Please add an address", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!isPaymentExist) {
            JOptionPane.showMessageDialog(parentFrame, "Please add a payment card", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            order.setAddressID(address.getID());
            order.setBankDetailState(isPaymentExist);
            order.nextStatus();
            if (OrderService.confirmOrder(order)){
                dispose();
            } else {
                JOptionPane.showMessageDialog(parentFrame, "Meet an error when confirming the order. Please try again later.", "Error", JOptionPane.ERROR_MESSAGE);
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
            updatePaymentPanel();
        }
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
        addressEditPanel = new JPanel();
        addressAddButton = new JButton();
        addressEditButton = new JButton();
        paymentPanel = new JPanel();
        paymentLabel = new JLabel();
        paymentText = new JLabel();
        paymentEditPanel = new JPanel();
        paymentAddButton = new JButton();
        paymentEditButton = new JButton();
        pendingOrderScrollPanel = new JScrollPane();
        pendingOrderItemsPanel = new JPanel();
        pendingOrderCardPanel = new JPanel();
        itemImage1 = new JLabel();
        itemName1 = new JLabel();
        itemPrice1 = new JLabel();
        itemNumLabel1 = new JLabel();
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
                    ((GridBagLayout)customerInfoPanel.getLayout()).rowHeights = new int[] {0, 0, 0};
                    ((GridBagLayout)customerInfoPanel.getLayout()).columnWeights = new double[] {0.0, 1.0E-4};
                    ((GridBagLayout)customerInfoPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};

                    //======== addressPanel ========
                    {
                        addressPanel.setMaximumSize(new Dimension(300, 200));
                        addressPanel.setLayout(new GridLayout(3, 1, 5, 5));

                        //---- addressLabel ----
                        addressLabel.setText("Address");
                        addressLabel.setForeground(new Color(0x003366));
                        addressLabel.setFont(addressLabel.getFont().deriveFont(addressLabel.getFont().getStyle() | Font.BOLD, addressLabel.getFont().getSize() + 5f));
                        addressLabel.setHorizontalAlignment(SwingConstants.LEFT);
                        addressPanel.add(addressLabel);
                        addressPanel.add(addressText);

                        //======== addressEditPanel ========
                        {
                            addressEditPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 5));

                            //---- addressAddButton ----
                            addressAddButton.setText("Add a new address");
                            addressAddButton.setForeground(new Color(0xe9e4e3));
                            addressAddButton.setBackground(new Color(0x55a15a));
                            addressAddButton.setFont(addressAddButton.getFont().deriveFont(addressAddButton.getFont().getStyle() & ~Font.BOLD));
                            addressAddButton.setPreferredSize(new Dimension(150, 23));
                            addressAddButton.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    addressAddButtonMouseClicked(e);
                                }
                            });
                            addressEditPanel.add(addressAddButton);

                            //---- addressEditButton ----
                            addressEditButton.setText("Edit the address");
                            addressEditButton.setBackground(new Color(0x00a5f3));
                            addressEditButton.setForeground(new Color(0xe9e4e3));
                            addressEditButton.setPreferredSize(new Dimension(150, 23));
                            addressEditButton.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    addressEditButtonMouseClicked(e);
                                }
                            });
                            addressEditPanel.add(addressEditButton);
                        }
                        addressPanel.add(addressEditPanel);
                    }
                    customerInfoPanel.add(addressPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                        new Insets(0, 0, 5, 0), 0, 0));

                    //======== paymentPanel ========
                    {
                        paymentPanel.setMaximumSize(new Dimension(300, 200));
                        paymentPanel.setLayout(new GridLayout(3, 1, 5, 5));

                        //---- paymentLabel ----
                        paymentLabel.setText("Payment method");
                        paymentLabel.setForeground(new Color(0x003366));
                        paymentLabel.setFont(paymentLabel.getFont().deriveFont(paymentLabel.getFont().getStyle() | Font.BOLD, paymentLabel.getFont().getSize() + 5f));
                        paymentPanel.add(paymentLabel);
                        paymentPanel.add(paymentText);

                        //======== paymentEditPanel ========
                        {
                            paymentEditPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 5));

                            //---- paymentAddButton ----
                            paymentAddButton.setText("Add a new card");
                            paymentAddButton.setForeground(new Color(0xe9e4e3));
                            paymentAddButton.setBackground(new Color(0x55a15a));
                            paymentAddButton.setFont(paymentAddButton.getFont().deriveFont(paymentAddButton.getFont().getStyle() & ~Font.BOLD));
                            paymentAddButton.setPreferredSize(new Dimension(150, 23));
                            paymentAddButton.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    paymentAddButtonMouseClicked(e);
                                }
                            });
                            paymentEditPanel.add(paymentAddButton);

                            //---- paymentEditButton ----
                            paymentEditButton.setText("Edit the payment");
                            paymentEditButton.setBackground(new Color(0x00a5f3));
                            paymentEditButton.setForeground(new Color(0xe9e4e3));
                            paymentEditButton.setPreferredSize(new Dimension(150, 23));
                            paymentEditButton.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    paymentEditButtonMouseClicked(e);
                                }
                            });
                            paymentEditPanel.add(paymentEditButton);
                        }
                        paymentPanel.add(paymentEditPanel);
                    }
                    customerInfoPanel.add(paymentPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                        new Insets(20, 0, 0, 0), 0, 0));
                }
                infoPanel.add(customerInfoPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 10, 0, 20), 0, 0));
            }
            pendingOrderContentPanel.add(infoPanel, new GridBagConstraints(0, 0, 1, 1, 0.8, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 5), 0, 0));

            //======== pendingOrderScrollPanel ========
            {
                pendingOrderScrollPanel.setPreferredSize(new Dimension(615, 200));
                pendingOrderScrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                pendingOrderScrollPanel.setMaximumSize(new Dimension(600, 32767));
                pendingOrderScrollPanel.setMinimumSize(new Dimension(600, 6));

                //======== pendingOrderItemsPanel ========
                {
                    pendingOrderItemsPanel.setMaximumSize(new Dimension(600, 32767));
                    pendingOrderItemsPanel.setMinimumSize(new Dimension(600, 1096));
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
                        pendingOrderCardPanel.setLayout(new GridBagLayout());
                        ((GridBagLayout)pendingOrderCardPanel.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0};
                        ((GridBagLayout)pendingOrderCardPanel.getLayout()).rowHeights = new int[] {0, 0};
                        ((GridBagLayout)pendingOrderCardPanel.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0E-4};
                        ((GridBagLayout)pendingOrderCardPanel.getLayout()).rowWeights = new double[] {1.0, 1.0E-4};

                        //---- itemImage1 ----
                        itemImage1.setText(bundle.getString("pendingOrderPage.itemImage1.text"));
                        itemImage1.setIcon(new ImageIcon(getClass().getResource("/images/tgv.jpeg")));
                        itemImage1.setPreferredSize(new Dimension(150, 120));
                        itemImage1.setMaximumSize(new Dimension(160, 120));
                        itemImage1.setMinimumSize(new Dimension(160, 120));
                        pendingOrderCardPanel.add(itemImage1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 0, 0, 0), 0, 0));

                        //---- itemName1 ----
                        itemName1.setText(bundle.getString("pendingOrderPage.itemName1.text"));
                        itemName1.setHorizontalAlignment(SwingConstants.CENTER);
                        itemName1.setPreferredSize(new Dimension(200, 17));
                        itemName1.setFont(itemName1.getFont().deriveFont(itemName1.getFont().getSize() + 2f));
                        pendingOrderCardPanel.add(itemName1, new GridBagConstraints(1, 0, 1, 1, 0.3, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 0, 0, 0), 0, 0));

                        //---- itemPrice1 ----
                        itemPrice1.setText(bundle.getString("pendingOrderPage.itemPrice1.text"));
                        itemPrice1.setHorizontalAlignment(SwingConstants.CENTER);
                        itemPrice1.setFont(itemPrice1.getFont().deriveFont(itemPrice1.getFont().getSize() + 4f));
                        itemPrice1.setPreferredSize(new Dimension(100, 22));
                        pendingOrderCardPanel.add(itemPrice1, new GridBagConstraints(2, 0, 1, 1, 0.2, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 0, 0, 0), 0, 0));

                        //---- itemNumLabel1 ----
                        itemNumLabel1.setText("1");
                        itemNumLabel1.setHorizontalAlignment(SwingConstants.CENTER);
                        pendingOrderCardPanel.add(itemNumLabel1, new GridBagConstraints(3, 0, 1, 1, 0.2, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 0, 0, 0), 0, 0));
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
        contentPane.add(pendingOrderButtonPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    private String formatCardNumber(String cardNumber) {
        return cardNumber.replaceAll("....", "$0 ");
    }

    private void updatePaymentPanel() {
        //---- paymentTextField ----
        this.bankDetail = BankDetailService.findBankDetail();
        if (bankDetail != null){
            String formattedText = "<html><body>"
                + bankDetail.getCardName() + "</b><br>"
                + formatCardNumber(bankDetail.getCardNumber()) + "</b><br>"
                + bankDetail.getCardHolderName() + "</b><br>"
                + bankDetail.getExpiryDate() + "   " 
                + "CVV: " + bankDetail.getSecurityCode() + "</b><br>"
                + "</b><br>"+"</body></html>";
            paymentText.setText(formattedText);
            isPaymentExist = true;
        } else {
            //---- paymentAddButton ----
            paymentEditButton.setVisible(false);
            paymentAddButton.setText("Add a Card");
            isPaymentExist = false;
        }

        //======== paymentEditPanel ========
        if (isPaymentExist) {
            //---- paymentEditButton ----
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
                + address.getPostcode()
                + "</b><br>"+"</body></html>";
            addressText.setText(formattedText);
            isAddressExist = true;
        } else {
            //---- addressAddButton ----
            addressAddButton.setVisible(true);
            addressEditButton.setVisible(false);
            addressAddButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    addressAddButtonMouseClicked(e);
                }
            });
            isAddressExist = false;
        }
        //======== addressEditPanel ========
        if (isAddressExist) {
            //---- addressEditButton ----
            addressAddButton.setVisible(false);
            addressEditButton.setVisible(true);
            addressEditButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    addressEditButtonMouseClicked(e);
                }
            });
        }
    }

    private JPanel createProductCard(Product product, int quantity) {
        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBorder(new MatteBorder(3, 3, 3, 3, new Color(0x003762)));
        cardPanel.setPreferredSize(new Dimension(600, 120));
    
        JLabel itemImage = new JLabel();
        itemImage.setText("pendingOrderPage.itemImage1.text");
        itemImage.setIcon(new ImageIcon(getClass().getResource("/images/tgv.jpeg")));
        itemImage.setPreferredSize(new Dimension(150, 120));
        itemImage.setMaximumSize(new Dimension(160, 120));
        itemImage.setMinimumSize(new Dimension(160, 120));
        cardPanel.add(itemImage, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
             GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
             new Insets(0, 0, 0, 0), 0, 0));
    
        JLabel itemName = new JLabel(product.getProductName());
        itemName.setHorizontalAlignment(SwingConstants.CENTER);
        itemName.setPreferredSize(new Dimension(200, 17));
        itemName.setFont(itemName.getFont().deriveFont(itemName.getFont().getSize() + 2f));
        cardPanel.add(itemName, new GridBagConstraints(1, 0, 1, 1, 0.3, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets(0, 0, 0, 0), 0, 0));


        JLabel itemPrice = new JLabel(String.format("\u00A3%.2f", product.getRetailPrice()));
        itemPrice.setHorizontalAlignment(SwingConstants.RIGHT);
        itemPrice.setFont(itemPrice.getFont().deriveFont(itemPrice.getFont().getSize() + 4f));
        itemPrice.setPreferredSize(new Dimension(100, 22));
        cardPanel.add(itemPrice, new GridBagConstraints(2, 0, 1, 1, 0.2, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets(0, 0, 0, 0), 0, 0));
    
        JLabel itemNumLabel = new JLabel(String.valueOf(quantity));
        itemNumLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cardPanel.add(itemNumLabel, new GridBagConstraints(3, 0, 1, 1, 0.2, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
            new Insets(0, 0, 0, 0), 0, 0));   
        return cardPanel;
    }

    private void addProductCards() {
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
            pendingOrderItemsPanel.add(cardPanel, gbc);
        }
    }
    
    

    // TODO: Test method
    public static void main(String[] args) throws DatabaseException {
        // User user = UserDAO.findUserByEmail("manager@manager.com");
        // User user = UserDAO.findUserByEmail("staff@gmail.com");
        User user = UserDAO.findUserByEmail("testemail@gmail.com");
        UserSession.getInstance().setCurrentUser(user);
        Order order = OrderDAO.findOrderByID(3);

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
    private JPanel addressEditPanel;
    private JButton addressAddButton;
    private JButton addressEditButton;
    private JPanel paymentPanel;
    private JLabel paymentLabel;
    private JLabel paymentText;
    private JPanel paymentEditPanel;
    private JButton paymentAddButton;
    private JButton paymentEditButton;
    private JScrollPane pendingOrderScrollPanel;
    private JPanel pendingOrderItemsPanel;
    private JPanel pendingOrderCardPanel;
    private JLabel itemImage1;
    private JLabel itemName1;
    private JLabel itemPrice1;
    private JLabel itemNumLabel1;
    private JPanel pendingOrderButtonPanel;
    private JButton cancelButton;
    private JButton confirmButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on

    private BankDetail bankDetail;
    private Address address;
    private Order order;
    private boolean isPaymentExist;
    private boolean isAddressExist;
}
