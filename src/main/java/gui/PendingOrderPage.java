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
 */
public class PendingOrderPage extends JFrame {
    public PendingOrderPage(Order order) {
        this.order = order;
        initComponents();
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
        customerInfoPanel = new JPanel();
        addressPanel = new JPanel();
        addressLabel = new JLabel();
        addressEditPanel = new JPanel();
        addressAddButton = new JButton();
        addressEditButton = new JButton();
        paymentPanel = new JPanel();
        paymentLabel = new JLabel();
        paymentEditPanel = new JPanel();
        paymentAddButton = new JButton();
        paymentEditButton = new JButton();
        pendingOrderScrollPanel = new JScrollPane();
        pendingOrderItemsPanel = new JPanel();
        pendingOrderButtonPanel = new JPanel();
        cancelButton = new JButton();
        confirmButton = new JButton();
        isAddressExist = false;
        isPaymentExist = false;

        bankDetail = new BankDetail();
        address = new Address();
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
                    addressPanel.add(addressLabel);

                    updateAddressPanel();
                }
                customerInfoPanel.add(addressPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
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

                    updatePaymentPanel();
                }
                customerInfoPanel.add(paymentPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(20, 0, 0, 0), 0, 0));
            }
            pendingOrderContentPanel.add(customerInfoPanel, new GridBagConstraints(0, 0, 1, 1, 0.8, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 10, 0, 25), 0, 0));

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

                    addProductCards();
                }
                pendingOrderScrollPanel.setViewportView(pendingOrderItemsPanel);
            }
            pendingOrderContentPanel.add(pendingOrderScrollPanel, new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 0, 5), 0, 0));
        }
        contentPane.add(pendingOrderContentPanel, BorderLayout.CENTER);

        //======== pendingOrderButtonPanel ========
        if(order.getStatus().getStatus().equals("Pending")){
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
        paymentPanel.removeAll();
        paymentEditPanel.removeAll(); 

        paymentPanel.setLayout(new GridLayout(3, 1, 5, 5));
        paymentPanel.add(paymentLabel);
        
        //---- paymentTextField ----
        this.bankDetail = BankDetailService.findBankDetail();
        if (bankDetail != null){
            JLabel paymentLabel = new JLabel();
            String formattedText = "<html><body>"
                + bankDetail.getCardName() + "</b><br>"
                + formatCardNumber(bankDetail.getCardNumber()) + "</b><br>"
                + bankDetail.getCardHolderName() + "</b><br>"
                + bankDetail.getExpiryDate() + "   " 
                + "CVV: " + bankDetail.getSecurityCode() + "</b><br>"
                + "</b><br>"+"</body></html>";
            paymentLabel.setText(formattedText);
            paymentPanel.add(paymentLabel);
            isPaymentExist = true;
        } else {
            //---- paymentAddButton ----
            JLabel addPaymentLabel = new JLabel();
            addPaymentLabel.setText("Pleases add a payment card.");
            paymentPanel.add(addPaymentLabel);
            paymentAddButton.setText("Add a Card");
            paymentAddButton.setForeground(new Color(0xe9e4e3));
            paymentAddButton.setBackground(new Color(0x55a15a));
            paymentAddButton.setFont(paymentAddButton.getFont().deriveFont(paymentAddButton.getFont().getStyle() & ~Font.BOLD));
            paymentAddButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    paymentAddButtonMouseClicked(e);
                }
            });
            isPaymentExist = false;
            paymentEditPanel.add(paymentAddButton);
        }

        //======== paymentEditPanel ========
        if (isPaymentExist) {
            //---- paymentEditButton ----
            paymentEditButton.setText("Edit");
            paymentEditButton.setBackground(new Color(0x00a5f3));
            paymentEditButton.setForeground(new Color(0xe9e4e3));
            paymentEditButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    paymentEditButtonMouseClicked(e);
                }
            });
            paymentEditPanel.add(paymentEditButton);
        }
        paymentPanel.add(paymentEditPanel);
        paymentPanel.revalidate();
        paymentPanel.repaint();
    }

    private void updateAddressPanel() {
        addressPanel.removeAll();
        addressEditPanel.removeAll(); 

        addressPanel.setLayout(new GridLayout(3, 1, 5, 5));
        addressPanel.add(addressLabel);
        
        //---- AddressTextField ----
        this.address = AddressService.getAddressByUser();
        if (!AddressService.isAddressEmpty(address)){
            JLabel addressLabel = new JLabel();
            String formattedText = "<html><body>"
                + address.getHouseNumber() + "</b><br>"
                + address.getRoadName() + "</b><br>"
                + address.getCity()+ "</b><br>"
                + address.getPostcode()
                + "</b><br>"+"</body></html>";
            addressLabel.setText(formattedText);
            addressPanel.add(addressLabel);
            isAddressExist = true;
        } else {
            //---- addressAddButton ----
            JLabel addAddressLabel = new JLabel();
            addAddressLabel.setText("Pleases add an address.");
            addressPanel.add(addAddressLabel);
            addressAddButton.setText("Add an Address");
            addressAddButton.setForeground(new Color(0xe9e4e3));
            addressAddButton.setBackground(new Color(0x55a15a));
            addressAddButton.setFont(addressAddButton.getFont().deriveFont(addressAddButton.getFont().getStyle() & ~Font.BOLD));
            addressAddButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    addressAddButtonMouseClicked(e);
                }
            });
            isAddressExist = false;
            addressEditPanel.add(addressAddButton);
        }
        //======== addressEditPanel ========
        if (isAddressExist) {
            //---- addressEditButton ----
            addressEditButton.setText("Edit");
            addressEditButton.setBackground(new Color(0x00a5f3));
            addressEditButton.setForeground(new Color(0xe9e4e3));
            addressEditButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    addressEditButtonMouseClicked(e);
                }
            });
            addressEditPanel.add(addressEditButton);
        }
        addressPanel.add(addressEditPanel);
        addressPanel.revalidate();
        addressPanel.repaint();
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
    private JPanel customerInfoPanel;
    private JPanel addressPanel;
    private JLabel addressLabel;
    private JPanel addressEditPanel;
    private JButton addressAddButton;
    private JButton addressEditButton;
    private JPanel paymentPanel;
    private JLabel paymentLabel;
    private JPanel paymentEditPanel;
    private JButton paymentAddButton;
    private JButton paymentEditButton;
    private JScrollPane pendingOrderScrollPanel;
    private JPanel pendingOrderItemsPanel;
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
