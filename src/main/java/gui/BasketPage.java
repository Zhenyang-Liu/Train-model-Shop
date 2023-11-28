/*
 * Created by JFormDesigner on Sat Nov 18 00:15:08 GMT 2023
 */

package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import exception.DatabaseException;
import listeners.ReloadListener;
import model.CartItem;
import model.Product;
import model.User;
import service.CartService;
import model.Cart;

import static DAO.OrderDAO.findOrderByID;

/**
 * @author Zhenyang Liu
 */
public class BasketPage extends JFrame {
    private Cart cart;
    private ReloadListener reloadListener;
    public void setReloadListener(ReloadListener listener) {
        this.reloadListener = listener;
    }

    public BasketPage(int userID) {
        initComponents();
        this.cart = CartService.getCartDetails(userID);
        if (cart != null){
            loadUserCart(userID);
        }else{
            CartService.createCartForUser(userID);
            loadUserCart(userID);
        }
    }

    private void checkOutButtonMouseClicked(MouseEvent e) {
        int orderID = CartService.checkoutCart(cart.getCartID());
        if (orderID == -1){

        } else if (orderID == -2) {

        } else if (orderID == -3) {

        } else {
            PendingOrderPage pendingOrderPage = new PendingOrderPage(findOrderByID(orderID));
            pendingOrderPage.setVisible(true);
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        ResourceBundle bundle = ResourceBundle.getBundle("gui.form");
        trolleyTitleLabel = new JLabel();
        trolleyContentPanel = new JPanel();
        panel2 = new JPanel();
        trolleyScrollPanel = new JScrollPane();
        trolleyItemsPanel = new JPanel();
        trolleyCardPanel = new JPanel();
        itemImage1 = new JLabel();
        itemName1 = new JLabel();
        itemPrice1 = new JLabel();
        adjustPanel = new JPanel();
        itemSpinner1 = new JSpinner();
        itemRemoveButton1 = new JButton();
        panel3 = new JPanel();
        trolleyButtonPanel = new JPanel();
        checkOutButton = new JButton();

        //======== this ========
        setBackground(Color.white);
        setPreferredSize(new Dimension(900, 600));
        setMinimumSize(new Dimension(900, 600));
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(5, 5));

        //---- trolleyTitleLabel ----
        trolleyTitleLabel.setText(bundle.getString("BasketPage.trolleyTitleLabel.text"));
        trolleyTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        trolleyTitleLabel.setFont(trolleyTitleLabel.getFont().deriveFont(trolleyTitleLabel.getFont().getStyle() | Font.BOLD, trolleyTitleLabel.getFont().getSize() + 13f));
        trolleyTitleLabel.setForeground(new Color(0x003366));
        trolleyTitleLabel.setBorder(new EmptyBorder(10, 5, 10, 5));
        contentPane.add(trolleyTitleLabel, BorderLayout.NORTH);

        //======== trolleyContentPanel ========
        {
            trolleyContentPanel.setLayout(new GridBagLayout());
            ((GridBagLayout)trolleyContentPanel.getLayout()).columnWidths = new int[] {0, 0, 0, 0};
            ((GridBagLayout)trolleyContentPanel.getLayout()).rowHeights = new int[] {0, 0};
            ((GridBagLayout)trolleyContentPanel.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};
            ((GridBagLayout)trolleyContentPanel.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

            //======== panel2 ========
            {
                panel2.setLayout(null);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for(int i = 0; i < panel2.getComponentCount(); i++) {
                        Rectangle bounds = panel2.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                    }
                    Insets insets = panel2.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    panel2.setMinimumSize(preferredSize);
                    panel2.setPreferredSize(preferredSize);
                }
            }
            trolleyContentPanel.add(panel2, new GridBagConstraints(0, 0, 1, 1, 0.5, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 5), 0, 0));

            //======== trolleyScrollPanel ========
            {
                trolleyScrollPanel.setPreferredSize(new Dimension(615, 200));
                trolleyScrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                trolleyScrollPanel.setMaximumSize(new Dimension(600, 32767));
                trolleyScrollPanel.setMinimumSize(new Dimension(600, 6));

                //======== trolleyItemsPanel ========
                {
                    trolleyItemsPanel.setMaximumSize(new Dimension(600, 32767));
                    trolleyItemsPanel.setMinimumSize(new Dimension(600, 1096));
                    trolleyItemsPanel.setLayout(new GridBagLayout());
                    ((GridBagLayout)trolleyItemsPanel.getLayout()).columnWidths = new int[] {0, 0};
                    ((GridBagLayout)trolleyItemsPanel.getLayout()).rowHeights = new int[] {0, 0};
                    ((GridBagLayout)trolleyItemsPanel.getLayout()).columnWeights = new double[] {0.0, 1.0E-4};
                    ((GridBagLayout)trolleyItemsPanel.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

                    //======== trolleyCardPanel ========
                    {
                        trolleyCardPanel.setBorder(new MatteBorder(3, 3, 3, 3, new Color(0x003762)));
                        trolleyCardPanel.setForeground(new Color(0x003366));
                        trolleyCardPanel.setPreferredSize(new Dimension(600, 120));
                        trolleyCardPanel.setMaximumSize(new Dimension(2147483647, 100));
                        trolleyCardPanel.setLayout(new BoxLayout(trolleyCardPanel, BoxLayout.X_AXIS));

                        //---- itemImage1 ----
                        itemImage1.setText(bundle.getString("BasketPage.itemImage1.text"));
                        itemImage1.setIcon(new ImageIcon(getClass().getResource("/images/tgv.jpeg")));
                        itemImage1.setPreferredSize(new Dimension(150, 120));
                        itemImage1.setMaximumSize(new Dimension(194, 120));
                        trolleyCardPanel.add(itemImage1);

                        //---- itemName1 ----
                        itemName1.setText(bundle.getString("BasketPage.itemName1.text"));
                        itemName1.setHorizontalAlignment(SwingConstants.CENTER);
                        itemName1.setPreferredSize(new Dimension(200, 17));
                        itemName1.setFont(itemName1.getFont().deriveFont(itemName1.getFont().getSize() + 2f));
                        trolleyCardPanel.add(itemName1);

                        //---- itemPrice1 ----
                        itemPrice1.setText(bundle.getString("BasketPage.itemPrice1.text"));
                        itemPrice1.setHorizontalAlignment(SwingConstants.CENTER);
                        itemPrice1.setFont(itemPrice1.getFont().deriveFont(itemPrice1.getFont().getSize() + 4f));
                        itemPrice1.setPreferredSize(new Dimension(100, 22));
                        trolleyCardPanel.add(itemPrice1);

                        //======== adjustPanel ========
                        {
                            adjustPanel.setPreferredSize(new Dimension(90, 100));
                            adjustPanel.setLayout(null);

                            //---- itemSpinner1 ----
                            itemSpinner1.setPreferredSize(new Dimension(40, 10));
                            itemSpinner1.setMinimumSize(new Dimension(30, 10));
                            itemSpinner1.setBorder(new MatteBorder(1, 1, 1, 1, Color.black));
                            adjustPanel.add(itemSpinner1);
                            itemSpinner1.setBounds(25, 20, 90, 35);

                            //---- itemRemoveButton1 ----
                            itemRemoveButton1.setText(bundle.getString("BasketPage.itemRemoveButton1.text"));
                            itemRemoveButton1.setPreferredSize(new Dimension(40, 15));
                            itemRemoveButton1.setForeground(new Color(0xe9e4e3));
                            itemRemoveButton1.setBackground(new Color(0xd54945));
                            adjustPanel.add(itemRemoveButton1);
                            itemRemoveButton1.setBounds(25, 65, 90, 35);

                            {
                                // compute preferred size
                                Dimension preferredSize = new Dimension();
                                for(int i = 0; i < adjustPanel.getComponentCount(); i++) {
                                    Rectangle bounds = adjustPanel.getComponent(i).getBounds();
                                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                                }
                                Insets insets = adjustPanel.getInsets();
                                preferredSize.width += insets.right;
                                preferredSize.height += insets.bottom;
                                adjustPanel.setMinimumSize(preferredSize);
                                adjustPanel.setPreferredSize(preferredSize);
                            }
                        }
                        trolleyCardPanel.add(adjustPanel);
                    }
                    trolleyItemsPanel.add(trolleyCardPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
                }
                trolleyScrollPanel.setViewportView(trolleyItemsPanel);
            }
            trolleyContentPanel.add(trolleyScrollPanel, new GridBagConstraints(1, 0, 1, 1, 0.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 5), 0, 0));

            //======== panel3 ========
            {
                panel3.setLayout(null);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for(int i = 0; i < panel3.getComponentCount(); i++) {
                        Rectangle bounds = panel3.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                    }
                    Insets insets = panel3.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    panel3.setMinimumSize(preferredSize);
                    panel3.setPreferredSize(preferredSize);
                }
            }
            trolleyContentPanel.add(panel3, new GridBagConstraints(2, 0, 1, 1, 0.5, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        }
        contentPane.add(trolleyContentPanel, BorderLayout.CENTER);

        //======== trolleyButtonPanel ========
        {
            trolleyButtonPanel.setLayout(new FlowLayout());

            //---- checkOutButton ----
            checkOutButton.setText(bundle.getString("BasketPage.checkOutButton.text"));
            checkOutButton.setPreferredSize(new Dimension(120, 40));
            checkOutButton.setBackground(new Color(0x55a15a));
            checkOutButton.setForeground(new Color(0xe9e4e3));
            checkOutButton.setFont(checkOutButton.getFont().deriveFont(checkOutButton.getFont().getStyle() | Font.BOLD, checkOutButton.getFont().getSize() + 2f));
            checkOutButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    checkOutButtonMouseClicked(e);
                }
            });
            trolleyButtonPanel.add(checkOutButton);
        }
        contentPane.add(trolleyButtonPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JLabel trolleyTitleLabel;
    private JPanel trolleyContentPanel;
    private JPanel panel2;
    private JScrollPane trolleyScrollPanel;
    private JPanel trolleyItemsPanel;
    private JPanel trolleyCardPanel;
    private JLabel itemImage1;
    private JLabel itemName1;
    private JLabel itemPrice1;
    private JPanel adjustPanel;
    private JSpinner itemSpinner1;
    private JButton itemRemoveButton1;
    private JPanel panel3;
    private JPanel trolleyButtonPanel;
    private JButton checkOutButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on

    private void loadUserCart(int userID) {
            loadTrolleyItems(cart.getCartItems()); // Load cart items into the trolley view
    }

    // Method to load cart items into the trolley view
    public void loadTrolleyItems(List<CartItem> cartItems) {
        trolleyItemsPanel.removeAll(); // Clear existing content

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER; // Each card spans the whole row
        gbc.weightx = 1; // Set horizontal weight
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fill horizontal space
        gbc.anchor = GridBagConstraints.NORTH; // Align to the top
        gbc.insets = new Insets(2, 2, 2, 2); // Set gaps between cards

        for (CartItem cartItem : cartItems) {
            // Create a new card panel for each cart item
            JPanel cardPanel = createTrolleyCardPanel(cartItem); // Create card panel using cart item
            trolleyItemsPanel.add(cardPanel, gbc); // Add card panel to trolleyItemsPanel
        }

        // Add extra blank area to push all components to the top
        gbc.weighty = 1; // Set vertical weight to absorb extra space
        trolleyItemsPanel.add(Box.createVerticalGlue(), gbc);

        trolleyItemsPanel.revalidate(); // Update panel to reflect changes
        trolleyItemsPanel.repaint(); // Repaint panel
    }

    // This method creates a single trolley card panel with all its components
    private JPanel createTrolleyCardPanel(CartItem cartItem) {
        Product product = cartItem.getItem();
        int quantity = cartItem.getQuantity();

        // Create a panel for the product card with a border and fixed maximum height
        JPanel trolleyCardPanel = new JPanel();
        trolleyCardPanel.setBorder(new MatteBorder(3, 3, 3, 3, new Color(0x003762)));
        trolleyCardPanel.setForeground(new Color(0x003366));
        trolleyCardPanel.setPreferredSize(new Dimension(590, 100));
        trolleyCardPanel.setMaximumSize(new Dimension(2147483647, 100));
        trolleyCardPanel.setLayout(new BoxLayout(trolleyCardPanel, BoxLayout.X_AXIS));

        // Create and configure the product image label
        JLabel itemImage = new JLabel();
        ImageIcon originalIcon = product.getProductImage();
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(150, 100, Image.SCALE_SMOOTH);
        itemImage.setIcon(new ImageIcon(resizedImage));


        trolleyCardPanel.add(itemImage);

        // Create and configure the product name label
        JLabel itemName = new JLabel(product.getProductName());
        itemName.setHorizontalAlignment(SwingConstants.CENTER);
        itemName.setPreferredSize(new Dimension(200, 17));
        itemName.setFont(itemName.getFont().deriveFont(itemName.getFont().getSize() + 2f));
        trolleyCardPanel.add(itemName);

        // Create and configure the product price label
        JLabel itemPrice = new JLabel(String.format("\u00A3" +"%.2f", product.getRetailPrice()));
        itemPrice.setHorizontalAlignment(SwingConstants.CENTER);
        itemPrice.setFont(itemPrice.getFont().deriveFont(itemPrice.getFont().getSize() + 4f));
        itemPrice.setPreferredSize(new Dimension(100, 22));
        trolleyCardPanel.add(itemPrice);

        // Create and configure the panel for quantity adjustment and removal button
        JPanel adjustPanel = new JPanel();
        adjustPanel.setPreferredSize(new Dimension(100, 100));
        adjustPanel.setLayout(null); // Null layout for absolute positioning

        // Create and configure the spinner for product quantity
        JSpinner itemSpinner = new JSpinner(new SpinnerNumberModel(quantity, 1, null, 1));
        itemSpinner.setPreferredSize(new Dimension(40, 10));
        itemSpinner.setMinimumSize(new Dimension(30, 10));
        itemSpinner.setBorder(new MatteBorder(1, 1, 1, 1, Color.black));
        adjustPanel.add(itemSpinner);
        itemSpinner.setBounds(25, 10, 90, 35);

        itemSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int currentQuantity = (Integer) itemSpinner.getValue();

                if(CartService.updateCartItem(cartItem.getItemID(), currentQuantity)){
                    if (reloadListener != null) {
                        reloadListener.reloadProducts();
                    }
                }else{
                    //TODO: Missing logic if update failed
                }
            }
        });

        // Create and configure the button for removing the product
        JButton itemRemoveButton = new JButton("Remove");
        itemRemoveButton.setPreferredSize(new Dimension(40, 15));
        itemRemoveButton.setForeground(new Color(0xe9e4e3));
        itemRemoveButton.setBackground(new Color(0xd54945));
        adjustPanel.add(itemRemoveButton);
        itemRemoveButton.setBounds(25, 50, 90, 35);

        // Compute the preferred size of the adjustPanel based on its components
        Dimension preferredSize = new Dimension();
        for(int i = 0; i < adjustPanel.getComponentCount(); i++) {
            Rectangle bounds = adjustPanel.getComponent(i).getBounds();
            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
        }
        Insets insets = adjustPanel.getInsets();
        preferredSize.width += insets.right;
        preferredSize.height += insets.bottom;
        adjustPanel.setMinimumSize(preferredSize);
        adjustPanel.setPreferredSize(preferredSize);

        // Add the adjustPanel to the trolleyCardPanel
        trolleyCardPanel.add(adjustPanel);

        return trolleyCardPanel; // Return the fully constructed product card panel
    }

}
