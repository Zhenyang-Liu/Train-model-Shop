/*
 * Created by JFormDesigner on Wed Nov 29 23:07:30 GMT 2023
 */

package gui;

import java.awt.*;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import helper.ImageUtils;
import model.Address;
import model.Order;
import model.Product;
import service.AddressService;

/**
 * @author Zhenyang Liu
 */
public class OrderPage extends JFrame {
    private final Order order;
    public OrderPage(Order order) {
        this.order = order;
        initComponents();
        addOrderDetail();
        addProductCards();
    }

    private void addOrderDetail(){
        orderNumText.setText(String.valueOf(order.getOrderID()));

        statusText.setText(order.getStatusString());

        createDateText.setText(order.getFormattedCreateTime());

        updateDateText.setText(order.getFormattedUpdateTime());

        Address address = AddressService.getAddressDetailByID(order.getAddressID());
        if (!AddressService.isAddressEmpty(address)){
            String formattedText = "<html><body>"
                    + address.getHouseNumber() + "</b><br>"
                    + address.getRoadName() + "</b><br>"
                    + address.getCity()+ "</b><br>"
                    + address.getPostcode()+ "</b><br>"
                    + "</b><br>"+"</body></html>";
            addressText.setText(formattedText);
        }

        if (order.getBankDetailState()){
            paymentText.setText("Valid Payment Provided");
        }else{
            paymentText.setText("Invalid Payment");
        }
        totalPrice.setText(String.format("\u00A3" +"%.2f", order.getTotalCost()));
    }

    private void addProductCards() {
        orderItemsPanel.removeAll();
        Map<Product, Integer> products = order.getOrderItems();
        orderItemsPanel.setLayout(new GridBagLayout());
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
            orderItemsPanel.add(cardPanel, gbc);
        }
    }

    private JPanel createProductCard(Product product, int quantity) {
        JPanel cardPanel = new JPanel();
        cardPanel.setBorder(new MatteBorder(3, 3, 3, 3, new Color(0x003762)));
        cardPanel.setPreferredSize(new Dimension(590, 120));
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.X_AXIS));

        ImageIcon originalIcon = product.getProductImage();
        ImageIcon resizedIcon = ImageUtils.resizeAndFillImageIcon(originalIcon, 160, 120);
        JLabel itemImage = new JLabel(resizedIcon);
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



    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        ResourceBundle bundle = ResourceBundle.getBundle("gui.form");
        orderTitleLabel = new JLabel();
        orderContentPanel = new JPanel();
        infoPanel = new JPanel();
        customerInfoPanel = new JPanel();
        orderNumPanel = new JPanel();
        orderNumLabel = new JLabel();
        orderNumText = new JLabel();
        statusPanel = new JPanel();
        statusLabel = new JLabel();
        statusText = new JLabel();
        createDatePanel = new JPanel();
        createDateLabel = new JLabel();
        createDateText = new JLabel();
        updateDatePanel = new JPanel();
        updateDateLabel = new JLabel();
        updateDateText = new JLabel();
        addressPanel = new JPanel();
        addressLabel = new JLabel();
        addressText = new JLabel();
        paymentPanel = new JPanel();
        paymentLabel = new JLabel();
        paymentText = new JLabel();
        orderScrollPanel = new JScrollPane();
        orderItemsPanel = new JPanel();
        pendingOrderCardPanel = new JPanel();
        itemImage1 = new JLabel();
        itemName1 = new JLabel();
        itemPrice1 = new JLabel();
        itemNumLabel1 = new JLabel();
        bottomPanel = new JPanel();
        paymentAmountPanel = new JPanel();
        subtotalLabel = new JLabel();
        totalPrice = new JLabel();

        //======== this ========
        setPreferredSize(new Dimension(900, 700));
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //---- orderTitleLabel ----
        orderTitleLabel.setText(bundle.getString("OrderPage.orderTitleLabel.text"));
        orderTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        orderTitleLabel.setFont(orderTitleLabel.getFont().deriveFont(orderTitleLabel.getFont().getStyle() | Font.BOLD, orderTitleLabel.getFont().getSize() + 13f));
        orderTitleLabel.setForeground(new Color(0x003366));
        orderTitleLabel.setBorder(new EmptyBorder(10, 5, 10, 5));
        contentPane.add(orderTitleLabel, BorderLayout.NORTH);

        //======== orderContentPanel ========
        {
            orderContentPanel.setLayout(new GridBagLayout());
            ((GridBagLayout)orderContentPanel.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0};
            ((GridBagLayout)orderContentPanel.getLayout()).rowHeights = new int[] {0, 0};
            ((GridBagLayout)orderContentPanel.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0E-4};
            ((GridBagLayout)orderContentPanel.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

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
                    ((GridBagLayout)customerInfoPanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0};
                    ((GridBagLayout)customerInfoPanel.getLayout()).columnWeights = new double[] {0.0, 1.0E-4};
                    ((GridBagLayout)customerInfoPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

                    //======== orderNumPanel ========
                    {
                        orderNumPanel.setMaximumSize(new Dimension(300, 200));
                        orderNumPanel.setMinimumSize(new Dimension(260, 150));
                        orderNumPanel.setLayout(new BoxLayout(orderNumPanel, BoxLayout.Y_AXIS));

                        //---- orderNumLabel ----
                        orderNumLabel.setText("Order No.");
                        orderNumLabel.setForeground(new Color(0x003366));
                        orderNumLabel.setFont(orderNumLabel.getFont().deriveFont(orderNumLabel.getFont().getStyle() | Font.BOLD, orderNumLabel.getFont().getSize() + 5f));
                        orderNumLabel.setMaximumSize(new Dimension(144, 23));
                        orderNumLabel.setMinimumSize(new Dimension(144, 23));
                        orderNumLabel.setPreferredSize(new Dimension(144, 23));
                        orderNumPanel.add(orderNumLabel);

                        //---- orderNumText ----
                        orderNumText.setMinimumSize(new Dimension(100, 200));
                        orderNumText.setFont(orderNumText.getFont().deriveFont(orderNumText.getFont().getSize() + 1f));
                        orderNumPanel.add(orderNumText);
                    }
                    customerInfoPanel.add(orderNumPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                        new Insets(0, 0, 5, 0), 0, 0));

                    //======== statusPanel ========
                    {
                        statusPanel.setMaximumSize(new Dimension(300, 200));
                        statusPanel.setMinimumSize(new Dimension(260, 150));
                        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));

                        //---- statusLabel ----
                        statusLabel.setText("Status");
                        statusLabel.setForeground(new Color(0x003366));
                        statusLabel.setFont(statusLabel.getFont().deriveFont(statusLabel.getFont().getStyle() | Font.BOLD, statusLabel.getFont().getSize() + 5f));
                        statusLabel.setMaximumSize(new Dimension(144, 23));
                        statusLabel.setMinimumSize(new Dimension(144, 23));
                        statusLabel.setPreferredSize(new Dimension(144, 23));
                        statusPanel.add(statusLabel);

                        //---- statusText ----
                        statusText.setMinimumSize(new Dimension(100, 100));
                        statusText.setMaximumSize(null);
                        statusText.setFont(statusText.getFont().deriveFont(statusText.getFont().getSize() + 1f));
                        statusPanel.add(statusText);
                    }
                    customerInfoPanel.add(statusPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                        new Insets(20, 0, 5, 0), 0, 0));

                    //======== createDatePanel ========
                    {
                        createDatePanel.setMaximumSize(new Dimension(300, 200));
                        createDatePanel.setMinimumSize(new Dimension(260, 150));
                        createDatePanel.setLayout(new BoxLayout(createDatePanel, BoxLayout.Y_AXIS));

                        //---- createDateLabel ----
                        createDateLabel.setText("Created at");
                        createDateLabel.setForeground(new Color(0x003366));
                        createDateLabel.setFont(createDateLabel.getFont().deriveFont(createDateLabel.getFont().getStyle() | Font.BOLD, createDateLabel.getFont().getSize() + 5f));
                        createDateLabel.setMaximumSize(new Dimension(144, 23));
                        createDateLabel.setMinimumSize(new Dimension(144, 23));
                        createDateLabel.setPreferredSize(new Dimension(144, 23));
                        createDatePanel.add(createDateLabel);

                        //---- createDateText ----
                        createDateText.setMinimumSize(new Dimension(100, 200));
                        createDateText.setFont(createDateText.getFont().deriveFont(createDateText.getFont().getSize() + 1f));
                        createDatePanel.add(createDateText);
                    }
                    customerInfoPanel.add(createDatePanel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                        new Insets(20, 0, 5, 0), 0, 0));

                    //======== updateDatePanel ========
                    {
                        updateDatePanel.setMaximumSize(new Dimension(300, 200));
                        updateDatePanel.setMinimumSize(new Dimension(260, 150));
                        updateDatePanel.setLayout(new BoxLayout(updateDatePanel, BoxLayout.Y_AXIS));

                        //---- updateDateLabel ----
                        updateDateLabel.setText("Updated at");
                        updateDateLabel.setForeground(new Color(0x003366));
                        updateDateLabel.setFont(updateDateLabel.getFont().deriveFont(updateDateLabel.getFont().getStyle() | Font.BOLD, updateDateLabel.getFont().getSize() + 5f));
                        updateDateLabel.setMaximumSize(new Dimension(144, 23));
                        updateDateLabel.setMinimumSize(new Dimension(144, 23));
                        updateDateLabel.setPreferredSize(new Dimension(144, 23));
                        updateDatePanel.add(updateDateLabel);

                        //---- updateDateText ----
                        updateDateText.setMinimumSize(new Dimension(100, 200));
                        updateDateText.setFont(updateDateText.getFont().deriveFont(updateDateText.getFont().getSize() + 1f));
                        updateDatePanel.add(updateDateText);
                    }
                    customerInfoPanel.add(updateDatePanel, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                        new Insets(20, 0, 5, 0), 0, 0));

                    //======== addressPanel ========
                    {
                        addressPanel.setMaximumSize(new Dimension(300, 200));
                        addressPanel.setMinimumSize(new Dimension(260, 150));
                        addressPanel.setLayout(new BoxLayout(addressPanel, BoxLayout.Y_AXIS));

                        //---- addressLabel ----
                        addressLabel.setText("Address");
                        addressLabel.setForeground(new Color(0x003366));
                        addressLabel.setFont(addressLabel.getFont().deriveFont(addressLabel.getFont().getStyle() | Font.BOLD, addressLabel.getFont().getSize() + 5f));
                        addressLabel.setMaximumSize(new Dimension(144, 23));
                        addressLabel.setMinimumSize(new Dimension(144, 23));
                        addressLabel.setPreferredSize(new Dimension(144, 23));
                        addressPanel.add(addressLabel);

                        //---- addressText ----
                        addressText.setMinimumSize(new Dimension(100, 200));
                        addressText.setFont(addressText.getFont().deriveFont(addressText.getFont().getSize() + 1f));
                        addressPanel.add(addressText);
                    }
                    customerInfoPanel.add(addressPanel, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                        new Insets(20, 0, 5, 0), 0, 0));

                    //======== paymentPanel ========
                    {
                        paymentPanel.setMaximumSize(new Dimension(300, 200));
                        paymentPanel.setMinimumSize(new Dimension(260, 150));
                        paymentPanel.setLayout(new BoxLayout(paymentPanel, BoxLayout.Y_AXIS));

                        //---- paymentLabel ----
                        paymentLabel.setText("Payment ");
                        paymentLabel.setForeground(new Color(0x003366));
                        paymentLabel.setFont(paymentLabel.getFont().deriveFont(paymentLabel.getFont().getStyle() | Font.BOLD, paymentLabel.getFont().getSize() + 5f));
                        paymentLabel.setMaximumSize(new Dimension(144, 23));
                        paymentLabel.setMinimumSize(new Dimension(144, 23));
                        paymentLabel.setPreferredSize(new Dimension(144, 23));
                        paymentPanel.add(paymentLabel);

                        //---- paymentText ----
                        paymentText.setMinimumSize(new Dimension(100, 100));
                        paymentText.setMaximumSize(null);
                        paymentText.setFont(paymentText.getFont().deriveFont(paymentText.getFont().getSize() + 1f));
                        paymentPanel.add(paymentText);
                    }
                    customerInfoPanel.add(paymentPanel, new GridBagConstraints(0, 5, 1, 1, 1.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                        new Insets(20, 0, 0, 0), 0, 0));
                }
                infoPanel.add(customerInfoPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 10, 0, 20), 0, 0));
            }
            orderContentPanel.add(infoPanel, new GridBagConstraints(0, 0, 1, 1, 0.8, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 5), 0, 0));

            //======== orderScrollPanel ========
            {
                orderScrollPanel.setPreferredSize(new Dimension(620, 200));
                orderScrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                orderScrollPanel.setMaximumSize(new Dimension(620, 32767));
                orderScrollPanel.setMinimumSize(new Dimension(620, 6));

                //======== orderItemsPanel ========
                {
                    orderItemsPanel.setMaximumSize(new Dimension(610, 32767));
                    orderItemsPanel.setMinimumSize(new Dimension(610, 1096));
                    orderItemsPanel.setLayout(new GridBagLayout());
                    ((GridBagLayout)orderItemsPanel.getLayout()).columnWidths = new int[] {0, 0};
                    ((GridBagLayout)orderItemsPanel.getLayout()).rowHeights = new int[] {0, 0};
                    ((GridBagLayout)orderItemsPanel.getLayout()).columnWeights = new double[] {0.0, 1.0E-4};
                    ((GridBagLayout)orderItemsPanel.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

                    //======== pendingOrderCardPanel ========
                    {
                        pendingOrderCardPanel.setBorder(new MatteBorder(3, 3, 3, 3, new Color(0x003762)));
                        pendingOrderCardPanel.setForeground(new Color(0x003366));
                        pendingOrderCardPanel.setPreferredSize(new Dimension(600, 120));
                        pendingOrderCardPanel.setMaximumSize(new Dimension(2147483647, 100));
                        pendingOrderCardPanel.setLayout(new BoxLayout(pendingOrderCardPanel, BoxLayout.X_AXIS));

                        //---- itemImage1 ----
                        itemImage1.setText(bundle.getString("OrderPage.itemImage1.text"));
                        itemImage1.setIcon(new ImageIcon(getClass().getResource("/images/tgv.jpeg")));
                        itemImage1.setPreferredSize(new Dimension(150, 120));
                        itemImage1.setMaximumSize(new Dimension(160, 120));
                        itemImage1.setMinimumSize(new Dimension(160, 120));
                        pendingOrderCardPanel.add(itemImage1);

                        //---- itemName1 ----
                        itemName1.setText(bundle.getString("OrderPage.itemName1.text"));
                        itemName1.setHorizontalAlignment(SwingConstants.CENTER);
                        itemName1.setPreferredSize(new Dimension(200, 17));
                        itemName1.setFont(itemName1.getFont().deriveFont(itemName1.getFont().getSize() + 2f));
                        itemName1.setMinimumSize(new Dimension(200, 19));
                        itemName1.setMaximumSize(new Dimension(200, 19));
                        pendingOrderCardPanel.add(itemName1);

                        //---- itemPrice1 ----
                        itemPrice1.setText(bundle.getString("OrderPage.itemPrice1.text"));
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
                    orderItemsPanel.add(pendingOrderCardPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                        new Insets(0, 0, 0, 0), 0, 0));
                }
                orderScrollPanel.setViewportView(orderItemsPanel);
            }
            orderContentPanel.add(orderScrollPanel, new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 0, 5), 0, 0));
        }
        contentPane.add(orderContentPanel, BorderLayout.CENTER);

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
                subtotalLabel.setText("Total Cost :");
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
        }
        contentPane.add(bottomPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JLabel orderTitleLabel;
    private JPanel orderContentPanel;
    private JPanel infoPanel;
    private JPanel customerInfoPanel;
    private JPanel orderNumPanel;
    private JLabel orderNumLabel;
    private JLabel orderNumText;
    private JPanel statusPanel;
    private JLabel statusLabel;
    private JLabel statusText;
    private JPanel createDatePanel;
    private JLabel createDateLabel;
    private JLabel createDateText;
    private JPanel updateDatePanel;
    private JLabel updateDateLabel;
    private JLabel updateDateText;
    private JPanel addressPanel;
    private JLabel addressLabel;
    private JLabel addressText;
    private JPanel paymentPanel;
    private JLabel paymentLabel;
    private JLabel paymentText;
    private JScrollPane orderScrollPanel;
    private JPanel orderItemsPanel;
    private JPanel pendingOrderCardPanel;
    private JLabel itemImage1;
    private JLabel itemName1;
    private JLabel itemPrice1;
    private JLabel itemNumLabel1;
    private JPanel bottomPanel;
    private JPanel paymentAmountPanel;
    private JLabel subtotalLabel;
    private JLabel totalPrice;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
