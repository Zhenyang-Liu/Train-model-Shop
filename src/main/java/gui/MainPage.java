/*
 * Created by JFormDesigner on Sun Oct 29 23:58:30 GMT 2023
 */

package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import javax.swing.border.*;
import com.formdev.flatlaf.extras.*;
import com.jgoodies.forms.factories.*;
import controller.GlobalState;
import model.Product;
import DAO.ProductDAO;
/**
 * @author Zhenyang Liu
 */
public class MainPage extends JFrame {
    private ProductDAO productDAO;

    public MainPage() {
        productDAO = new ProductDAO();  // Instantiating ProductDAO
        initComponents();
        loadProducts();
        customizeComponents();
    }

    private void createUIComponents() {
        // TODO: add custom component creation code here
    }

    private void button_accountMouseClicked(MouseEvent e) {
        // TODO add your code here
        SwingUtilities.invokeLater(() -> {
            if (!GlobalState.isLoggedIn()) {
                LoginPage loginPage = new LoginPage();
                loginPage.setVisible(true);
            } else {
                // User logged in
            }
        });
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        ResourceBundle bundle = ResourceBundle.getBundle("gui.form");
        DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
        topPanel = new JPanel();
        accountPanel = new JPanel();
        button_account = new JButton();
        button_cart = new JButton();
        separatorForAccount = compFactory.createSeparator("");
        titlePanel = new JPanel();
        mainTitle = new JLabel();
        subTitle = new JLabel();
        searchPanel = new JPanel();
        searchLabel = new JLabel();
        searchKeywordField = new JTextField();
        searchButton = new JButton();
        mainPageSplitPane = new JSplitPane();
        filterPanel = new JPanel();
        filterLabel1 = new JLabel();
        filterBox1 = new JComboBox();
        filterLabel2 = new JLabel();
        filterBox2 = new JComboBox();
        filterLabel3 = new JLabel();
        filterBox3 = new JComboBox();
        filterLabel4 = new JLabel();
        filterBox4 = new JComboBox();
        filterLabel5 = new JLabel();
        filterBox5 = new JComboBox();
        productPanel = new JPanel();
        productCardPanel1 = new JPanel();
        productImage1 = new JLabel();
        productName1 = new JLabel();
        purchasePanel1 = new JPanel();
        productPrice1 = new JLabel();
        panel1 = new JPanel();
        moreButton1 = new JButton();
        addButton1 = new JButton();
        bottomSeparator = compFactory.createSeparator("");

        //======== this ========
        setPreferredSize(new Dimension(1080, 720));
        setFont(new Font("Arial", Font.PLAIN, 12));
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== topPanel ========
        {
            topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

            //======== accountPanel ========
            {
                accountPanel.setLayout(new BorderLayout());

                //---- button_account ----
                button_account.setIcon(new FlatSVGIcon("images/person_black_24dp.svg"));
                button_account.setBackground(new Color(0xf2f2f2));
                button_account.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        button_accountMouseClicked(e);
                    }
                });
                accountPanel.add(button_account, BorderLayout.WEST);

                //---- button_cart ----
                button_cart.setSelectedIcon(null);
                button_cart.setIcon(new FlatSVGIcon("images/shopping_cart_black_24dp.svg"));
                button_cart.setBackground(new Color(0xf2f2f2));
                button_cart.setHorizontalAlignment(SwingConstants.RIGHT);
                accountPanel.add(button_cart, BorderLayout.EAST);
            }
            topPanel.add(accountPanel);
            topPanel.add(separatorForAccount);

            //======== titlePanel ========
            {
                titlePanel.setMinimumSize(new Dimension(251, 90));
                titlePanel.setPreferredSize(new Dimension(251, 80));
                titlePanel.setLayout(new GridLayout(2, 1, 5, 0));

                //---- mainTitle ----
                mainTitle.setText(bundle.getString("MainPage.mainTitle.text"));
                mainTitle.setFont(mainTitle.getFont().deriveFont(mainTitle.getFont().getStyle() | Font.BOLD, mainTitle.getFont().getSize() + 26f));
                mainTitle.setForeground(new Color(0x003366));
                mainTitle.setHorizontalAlignment(SwingConstants.CENTER);
                mainTitle.setMaximumSize(null);
                mainTitle.setBorder(new EmptyBorder(10, 0, 0, 0));
                titlePanel.add(mainTitle);

                //---- subTitle ----
                subTitle.setText(bundle.getString("MainPage.subTitle.text"));
                subTitle.setFont(subTitle.getFont().deriveFont(subTitle.getFont().getSize() + 5f));
                subTitle.setHorizontalAlignment(SwingConstants.CENTER);
                subTitle.setMaximumSize(new Dimension(163, 12));
                subTitle.setMinimumSize(new Dimension(163, 12));
                subTitle.setPreferredSize(new Dimension(163, 12));
                titlePanel.add(subTitle);
            }
            topPanel.add(titlePanel);

            //======== searchPanel ========
            {
                searchPanel.setMaximumSize(new Dimension(305, 30));
                searchPanel.setPreferredSize(new Dimension(305, 30));
                searchPanel.setMinimumSize(new Dimension(305, 30));
                searchPanel.setLayout(new GridBagLayout());
                ((GridBagLayout)searchPanel.getLayout()).columnWidths = new int[] {0, 0, 0, 0};
                ((GridBagLayout)searchPanel.getLayout()).rowHeights = new int[] {0, 0};
                ((GridBagLayout)searchPanel.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};
                ((GridBagLayout)searchPanel.getLayout()).rowWeights = new double[] {1.0, 1.0E-4};

                //---- searchLabel ----
                searchLabel.setText(bundle.getString("MainPage.searchLabel.text"));
                searchLabel.setFont(searchLabel.getFont().deriveFont(searchLabel.getFont().getSize() + 2f));
                searchPanel.add(searchLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- searchKeywordField ----
                searchKeywordField.setPreferredSize(new Dimension(160, 23));
                searchKeywordField.setMaximumSize(new Dimension(160, 23));
                searchKeywordField.setMinimumSize(new Dimension(160, 23));
                searchPanel.add(searchKeywordField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 5, 0, 5), 0, 0));

                //---- searchButton ----
                searchButton.setText(bundle.getString("MainPage.searchButton.text"));
                searchButton.setBackground(new Color(0x204688));
                searchButton.setForeground(new Color(0xe0e2e8));
                searchPanel.add(searchButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 5, 0, 0), 0, 0));
            }
            topPanel.add(searchPanel);
        }
        contentPane.add(topPanel, BorderLayout.NORTH);

        //======== mainPageSplitPane ========
        {

            //======== filterPanel ========
            {
                filterPanel.setAlignmentX(-0.6949153F);
                filterPanel.setBorder(new EmptyBorder(10, 10, 0, 10));
                filterPanel.setPreferredSize(null);
                filterPanel.setFont(filterPanel.getFont().deriveFont(filterPanel.getFont().getSize() + 2f));
                filterPanel.setLayout(new GridBagLayout());
                ((GridBagLayout)filterPanel.getLayout()).columnWidths = new int[] {0, 0};
                ((GridBagLayout)filterPanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                ((GridBagLayout)filterPanel.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
                ((GridBagLayout)filterPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

                //---- filterLabel1 ----
                filterLabel1.setText(bundle.getString("MainPage.filterLabel1.text"));
                filterLabel1.setHorizontalTextPosition(SwingConstants.LEFT);
                filterLabel1.setHorizontalAlignment(SwingConstants.TRAILING);
                filterLabel1.setPreferredSize(null);
                filterPanel.add(filterLabel1, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 5, 0), 0, 0));
                filterPanel.add(filterBox1, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- filterLabel2 ----
                filterLabel2.setText(bundle.getString("MainPage.filterLabel2.text"));
                filterPanel.add(filterLabel2, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                    new Insets(5, 0, 5, 0), 0, 0));
                filterPanel.add(filterBox2, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- filterLabel3 ----
                filterLabel3.setText(bundle.getString("MainPage.filterLabel3.text"));
                filterPanel.add(filterLabel3, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                    new Insets(5, 0, 5, 0), 0, 0));
                filterPanel.add(filterBox3, new GridBagConstraints(0, 5, 1, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- filterLabel4 ----
                filterLabel4.setText(bundle.getString("MainPage.filterLabel4.text"));
                filterPanel.add(filterLabel4, new GridBagConstraints(0, 6, 1, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                    new Insets(5, 0, 5, 0), 0, 0));
                filterPanel.add(filterBox4, new GridBagConstraints(0, 7, 1, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- filterLabel5 ----
                filterLabel5.setText(bundle.getString("MainPage.filterLabel5.text"));
                filterPanel.add(filterLabel5, new GridBagConstraints(0, 8, 1, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                    new Insets(5, 0, 5, 0), 0, 0));
                filterPanel.add(filterBox5, new GridBagConstraints(0, 9, 1, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));
            }
            mainPageSplitPane.setLeftComponent(filterPanel);

            //======== productPanel ========
            {
                productPanel.setMaximumSize(new Dimension(300, 32767));
                productPanel.setLayout(new FlowLayout(FlowLayout.LEADING));

                //======== productCardPanel1 ========
                {
                    productCardPanel1.setBorder(new LineBorder(new Color(0x002c7b), 2, true));
                    productCardPanel1.setPreferredSize(new Dimension(260, 280));
                    productCardPanel1.setMaximumSize(new Dimension(230, 240));
                    productCardPanel1.setMinimumSize(new Dimension(230, 240));
                    productCardPanel1.setLayout(new GridBagLayout());
                    ((GridBagLayout)productCardPanel1.getLayout()).columnWidths = new int[] {0, 0};
                    ((GridBagLayout)productCardPanel1.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
                    ((GridBagLayout)productCardPanel1.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
                    ((GridBagLayout)productCardPanel1.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

                    //---- productImage1 ----
                    productImage1.setIcon(new ImageIcon(getClass().getResource("/images/tgv.jpeg")));
                    productImage1.setPreferredSize(new Dimension(216, 120));
                    productImage1.setAlignmentY(0.0F);
                    productImage1.setMaximumSize(new Dimension(216, 120));
                    productImage1.setMinimumSize(new Dimension(216, 120));
                    productCardPanel1.add(productImage1, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

                    //---- productName1 ----
                    productName1.setText(bundle.getString("MainPage.productName1.text"));
                    productName1.setFont(productName1.getFont().deriveFont(productName1.getFont().getSize() + 4f));
                    productName1.setHorizontalAlignment(SwingConstants.CENTER);
                    productName1.setBorder(new MatteBorder(3, 0, 3, 0, Color.black));
                    productName1.setPreferredSize(new Dimension(220, 30));
                    productCardPanel1.add(productName1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.4,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(1, 0, 0, 0), 0, 0));

                    //======== purchasePanel1 ========
                    {
                        purchasePanel1.setBorder(new EmptyBorder(10, 5, 5, 5));
                        purchasePanel1.setMaximumSize(new Dimension(190, 85));
                        purchasePanel1.setLayout(new GridLayout(2, 1, 20, 10));

                        //---- productPrice1 ----
                        productPrice1.setText(bundle.getString("MainPage.productPrice1.text"));
                        productPrice1.setFont(productPrice1.getFont().deriveFont(productPrice1.getFont().getSize() + 7f));
                        productPrice1.setPreferredSize(new Dimension(80, 25));
                        productPrice1.setHorizontalAlignment(SwingConstants.CENTER);
                        purchasePanel1.add(productPrice1);

                        //======== panel1 ========
                        {
                            panel1.setPreferredSize(new Dimension(240, 40));
                            panel1.setLayout(new FlowLayout());

                            //---- moreButton1 ----
                            moreButton1.setText("Detail");
                            moreButton1.setBackground(new Color(0x4e748d));
                            moreButton1.setForeground(new Color(0xe0e2e8));
                            moreButton1.setPreferredSize(new Dimension(100, 30));
                            panel1.add(moreButton1);

                            //---- addButton1 ----
                            addButton1.setText(bundle.getString("MainPage.addButton1.text"));
                            addButton1.setBackground(new Color(0x55a15a));
                            addButton1.setForeground(new Color(0xe0e2e8));
                            addButton1.setPreferredSize(new Dimension(100, 30));
                            panel1.add(addButton1);
                        }
                        purchasePanel1.add(panel1);
                    }
                    productCardPanel1.add(purchasePanel1, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
                }
                productPanel.add(productCardPanel1);
            }
            mainPageSplitPane.setRightComponent(productPanel);
        }
        contentPane.add(mainPageSplitPane, BorderLayout.CENTER);
        contentPane.add(bottomSeparator, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel topPanel;
    private JPanel accountPanel;
    private JButton button_account;
    private JButton button_cart;
    private JComponent separatorForAccount;
    private JPanel titlePanel;
    private JLabel mainTitle;
    private JLabel subTitle;
    private JPanel searchPanel;
    private JLabel searchLabel;
    private JTextField searchKeywordField;
    private JButton searchButton;
    private JSplitPane mainPageSplitPane;
    private JPanel filterPanel;
    private JLabel filterLabel1;
    private JComboBox filterBox1;
    private JLabel filterLabel2;
    private JComboBox filterBox2;
    private JLabel filterLabel3;
    private JComboBox filterBox3;
    private JLabel filterLabel4;
    private JComboBox filterBox4;
    private JLabel filterLabel5;
    private JComboBox filterBox5;
    private JPanel productPanel;
    private JPanel productCardPanel1;
    private JLabel productImage1;
    private JLabel productName1;
    private JPanel purchasePanel1;
    private JLabel productPrice1;
    private JPanel panel1;
    private JButton moreButton1;
    private JButton addButton1;
    private JComponent bottomSeparator;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on

    private void customizeComponents() {
        // 在这里添加自定义组件设置代码
//        ImageIcon originalIcon = new ImageIcon("TrainShop\\src\\main\\images\\tgv.jpeg");
//        Image originalImage = originalIcon.getImage();
//        Image resizedImage = originalImage.getScaledInstance(productImage1.getWidth(), productImage1.getHeight(), Image.SCALE_SMOOTH);
//        productImage1.setIcon(new ImageIcon(resizedImage));

        //Image resizedImage18 = originalImage.getScaledInstance(label18.getWidth(), label18.getHeight(), Image.SCALE_SMOOTH);
        //label18.setIcon(new ImageIcon(resizedImage18));

        //Image resizedImage19 = originalImage.getScaledInstance(label19.getWidth(), label19.getHeight(), Image.SCALE_SMOOTH);
        //label19.setIcon(new ImageIcon(resizedImage19));

    }

    public void loadProducts() {
        new SwingWorker<ArrayList<Product>, Void>() {
            @Override
            protected ArrayList<Product> doInBackground() throws Exception {
                // Get products from the database in the background thread
                return productDAO.getAllProduct();
            }

            @Override
            protected void done() {
                try {
                    // Get the result of doInBackground
                    ArrayList<Product> productList = get();
                    // Updating the GUI with a Product List
                    for (Product product : productList) {
                        // Create cards for each product
                        JPanel productCard = createProductCard(product);
                        // Add the card to the container
                        productPanel.add(productCard);
                    }

                    productPanel.revalidate();
                    productPanel.repaint();

                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    // Handling exceptions
                }
            }
        }.execute();
    }

    public JPanel createProductCard(Product product) {
        // Create the outer panel of the card
        JPanel productCardPanel = new JPanel(new GridBagLayout());
        productCardPanel.setBorder(new LineBorder(new Color(0x002c7b), 2, true));
        productCardPanel.setPreferredSize(new Dimension(260, 280));
        productCardPanel.setLayout(new GridBagLayout());
        ((GridBagLayout)productCardPanel.getLayout()).columnWidths = new int[] {0, 0};
        ((GridBagLayout)productCardPanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
        ((GridBagLayout)productCardPanel.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
        ((GridBagLayout)productCardPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

        // adjust the maximum and minimum sizes as needed.
        productCardPanel.setLayout(new GridBagLayout());

        // Add a product image
        JLabel productImage = new JLabel();
        //productImage.setPreferredSize(new Dimension(260, 120));
        ImageIcon originalIcon = new ImageIcon(product.getProductImage());
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(256, 140, Image.SCALE_SMOOTH);
        productImage.setIcon(new ImageIcon(resizedImage));

        productCardPanel.add(productImage, new GridBagConstraints
                (0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));


        // Add product name
        JLabel productName = new JLabel();
        productName.setText(product.getProductName());
        productName.setFont(productName.getFont().deriveFont(productName.getFont().getSize() + 4f));
        productName.setHorizontalAlignment(SwingConstants.CENTER);
        productName.setBorder(new MatteBorder(3, 0, 3, 0, Color.black));
        productName.setPreferredSize(new Dimension(260, 70));
        productCardPanel.add(productName, new GridBagConstraints
                (0, 1, 1, 1, 0.0, 0.7,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(1, 0, 0, 0), 0, 0));

        // 创建价格显示标签
        JLabel productPrice = new JLabel();
        productPrice.setText(String.format("£%.2f", product.getRetailPrice()));
        productPrice.setFont(productPrice.getFont().deriveFont(productPrice.getFont().getSize() + 7f));
        productPrice.setHorizontalAlignment(SwingConstants.CENTER);

        // 创建详情按钮
        JButton moreButton = new JButton("Detail");
        moreButton.setBackground(new Color(0x4e748d));
        moreButton.setForeground(new Color(0xe0e2e8));
        moreButton.setPreferredSize(new Dimension(100, 30));

        // 创建添加到购物车按钮
        JButton addButton = new JButton("Add");
        addButton.setBackground(new Color(0x55a15a));
        addButton.setForeground(new Color(0xe0e2e8));
        addButton.setPreferredSize(new Dimension(100, 30));

        // 创建购买面板，并添加价格和按钮
        JPanel purchasePanel = new JPanel();
        purchasePanel.setBorder(new EmptyBorder(10, 5, 5, 5));
        purchasePanel.setMaximumSize(new Dimension(190, 85));
        purchasePanel.setLayout(new GridLayout(2, 1, 20, 10));
        purchasePanel.add(productPrice);

        // 创建按钮面板，并添加详情按钮和添加到购物车按钮
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setPreferredSize(new Dimension(240, 40));
        buttonPanel.add(moreButton);
        buttonPanel.add(addButton);

        // 添加按钮面板到购买面板
        purchasePanel.add(buttonPanel);

        // 将购买面板添加到商品卡片面板
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        productCardPanel.add(purchasePanel, gbc);

        return productCardPanel;
    }
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MainPage frame = new MainPage();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
