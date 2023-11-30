/*
 * Created by JFormDesigner on Sun Oct 29 23:58:30 GMT 2023
 */

package gui;

import DAO.DatabaseConnectionHandler;
import DAO.ProductDAO;
import DAO.UserDAO;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import java.awt.*;
import java.util.concurrent.ExecutionException;
import javax.swing.*;
import exception.DatabaseException;
import helper.Filter;
import helper.Logging;
import helper.UserSession;
import listeners.ReloadListener;
import model.*;
import model.Locomotive.DCCType;
import service.CartService;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * @author Zhenyang Liu
 */
public class MainPage extends JFrame implements ReloadListener {
    private Filter f;
    public MainPage() {

        Logging.getLogger().info("Creating Main Page");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
        f = new Filter();
        populateFilterBoxes();
        loadProducts();
        customizeComponents();
        button_accountMouseClicked();
    }

    public void reloadProducts() {
        loadProducts();
    }

    private void button_accountMouseClicked() {
        SwingUtilities.invokeLater(() -> {
            if (!UserSession.getInstance().isLoggedIn()) {
                LoginPage loginPage = new LoginPage();;
                loginPage.setVisible(true);
                loginPage.setLoginSuccessListener(this::loadProducts);
            } else {
                int userID = UserSession.getInstance().getCurrentUser().getUserID();
                AccountPage accountPage = new AccountPage(userID);
                accountPage.setVisible(true);
            }
        });
    }

    private void button_cartMouseClicked(MouseEvent e) {
        User currentUser = UserSession.getInstance().getCurrentUser();

        if (currentUser != null) {
            int userID = currentUser.getUserID();
            BasketPage basketPage = new BasketPage(userID);
            basketPage.setVisible(true);
            basketPage.setReloadListener(this::loadProducts);
        } else {
            // USER NOT LOGIN
            LoginPage loginPage = new LoginPage();
            loginPage.setVisible(true);
        }

    }

    private void populatePriceRangeFilters(){
        priceFilterBox.addItem(f.new PriceRange(0f, 1e10f, "All"));
        priceFilterBox.addItem(f.new PriceRange(0.0f, 15.0f));
        priceFilterBox.addItem(f.new PriceRange(15.0f, 30.0f));
        priceFilterBox.addItem(f.new PriceRange(30.0f, 50.0f));
        priceFilterBox.addItem(f.new PriceRange(50.0f, 100.0f));
        priceFilterBox.addItem(f.new PriceRange(100.0f, 500.0f));
        priceFilterBox.addItem(f.new PriceRange(500.0f, 1e10f, "£500<"));

        priceFilterBox.addItemListener(e -> {
            loadProducts();
        });
    }

    private void populateSubFilters(){
        subTypeFilterBox.removeAllItems();
        String table = ((Filter.TypeFilter)typeFilterBox.getSelectedItem()).getDbTable();
        subTypeFilterBox.setVisible(true);
        subTypeFilterLabel.setVisible(true);
        if(table.equals("Locomotive")){
            subTypeFilterLabel.setText("DCC Type");
            subTypeFilterBox.addItem(DCCType.ANALOGUE);
            subTypeFilterBox.addItem(DCCType.FITTED);
            subTypeFilterBox.addItem(DCCType.READY);
            subTypeFilterBox.addItem(DCCType.SOUND);
        }else if(table.equals("Track")){
            subTypeFilterLabel.setText("Gauge");
            subTypeFilterBox.addItem(Gauge.OO);
            subTypeFilterBox.addItem(Gauge.TT);
            subTypeFilterBox.addItem(Gauge.N);
        }else{
            subTypeFilterBox.setVisible(false);
            subTypeFilterLabel.setVisible(false);
        }
    }

    private void populateTypeFilters(){
        typeFilterBox.addItem(f.new TypeFilter("", "All", ""));
        typeFilterBox.addItem(f.new TypeFilter("Locomotive", "Locomotives", "dcc_type"));
        typeFilterBox.addItem(f.new TypeFilter("Track", "Tracks", "track_type"));
        typeFilterBox.addItem(f.new TypeFilter("BoxedSet", "Box Sets", "pack_type"));

        typeFilterBox.addItemListener(e -> {
            populateSubFilters();
            loadProducts();
        });
        subTypeFilterBox.addItemListener(e -> {
            ((Filter.TypeFilter)typeFilterBox.getSelectedItem()).setSubFilter(e.getItem().toString());
            loadProducts();
        });
    }

    private void populateSortOptions(){
        sortOptions.addItem(f.new SortBy("Name (asc)", "product_name"));
        sortOptions.addItem(f.new SortBy("Name (desc)", "product_name", false));
        sortOptions.addItem(f.new SortBy("Price (asc)", "retail_price"));
        sortOptions.addItem(f.new SortBy("Price (desc)", "retail_price", false));

        sortOptions.addItemListener(e -> {
            loadProducts();
        });
    }

    private void populateBrandFilters(){
        ArrayList<String> toAdd = ProductDAO.findAllBrand();
        filterBox4.addItem(f.new BrandFilter(null, "All"));
        for(String b: toAdd)
            filterBox4.addItem(f.new BrandFilter(b));
        filterBox4.addItemListener(e -> {
            loadProducts();
        });
    }

    private void populateFilterBoxes()
    {
        populatePriceRangeFilters();
        populateBrandFilters();
        populateSortOptions();
        populateTypeFilters();
        populateSubFilters();

        searchButton.addActionListener(e -> {
            loadProducts();
        });
    }

    private void button_staffMouseClicked(MouseEvent e) {
        ProductManagePage p = new ProductManagePage();
        p.setVisible(true);
    }

    private void button_ordersMouseClicked(MouseEvent e) {
        User currentUser = UserSession.getInstance().getCurrentUser();

        if (currentUser != null) {
            OrderHistory ordersPage = new OrderHistory();
            ordersPage.setVisible(true);
        } else {
            // USER NOT LOGIN
            LoginPage loginPage = new LoginPage();
            loginPage.setVisible(true);
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        ResourceBundle bundle = ResourceBundle.getBundle("gui.form");
        DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
        topPanel = new JPanel();
        accountPanel = new JPanel();
        leftButtonPanel = new JPanel();
        button_account = new JButton();
        button_staff = new JButton();
        rightButtonPanel = new JPanel();
        button_cart = new JButton();
        button_orders = new JButton();
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
        sortLabel = new JLabel();
        sortOptions = new JComboBox<>();
        priceFilterLabel = new JLabel();
        priceFilterBox = new JComboBox<>();
        typeFilterLabel = new JLabel();
        typeFilterBox = new JComboBox<>();
        brandFilterLabel = new JLabel();
        filterBox4 = new JComboBox<>();
        subTypeFilterLabel = new JLabel();
        subTypeFilterBox = new JComboBox<>();
        scrollPane1 = new JScrollPane();
        productPanel = new JPanel();
        productCardPanel1 = new JPanel();
        productImage1 = new JLabel();
        productName1 = new JLabel();
        purchasePanel1 = new JPanel();
        productPrice1 = new JLabel();
        buttonPanel1 = new JPanel();
        moreButton1 = new JButton();
        addButton1 = new JButton();
        soldoutLabel1 = new JLabel();
        adjustNumPanel1 = new JPanel();
        removeButton = new JButton();
        NumButton = new JButton();
        addButton = new JButton();
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

                //======== leftButtonPanel ========
                {
                    leftButtonPanel.setLayout(new BoxLayout(leftButtonPanel, BoxLayout.X_AXIS));

                    //---- button_account ----
                    button_account.setIcon(new FlatSVGIcon("images/person_black_24dp.svg"));
                    button_account.setBackground(new Color(0xf2f2f2));
                    button_account.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            button_accountMouseClicked();
                        }
                    });
                    leftButtonPanel.add(button_account);

                    //---- button_staff ----
                    button_staff.setIcon(new FlatSVGIcon("images/assignment_black_24dp.svg"));
                    button_staff.setBackground(new Color(0xf2f2f2));
                    button_staff.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            button_staffMouseClicked(e);
                        }
                    });
                    leftButtonPanel.add(button_staff);
                }
                accountPanel.add(leftButtonPanel, BorderLayout.WEST);

                //======== rightButtonPanel ========
                {
                    rightButtonPanel.setLayout(new BoxLayout(rightButtonPanel, BoxLayout.X_AXIS));

                    //---- button_cart ----
                    button_cart.setSelectedIcon(null);
                    button_cart.setIcon(new FlatSVGIcon("images/shopping_cart_black_24dp.svg"));
                    button_cart.setBackground(new Color(0xf2f2f2));
                    button_cart.setHorizontalAlignment(SwingConstants.RIGHT);
                    button_cart.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            button_cartMouseClicked(e);
                        }
                    });
                    rightButtonPanel.add(button_cart);

                    //---- button_orders ----
                    button_orders.setIcon(new FlatSVGIcon("images/list_alt_black_24dp.svg"));
                    button_orders.setBackground(new Color(0xf2f2f2));
                    button_orders.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            button_ordersMouseClicked(e);
                        }
                    });
                    rightButtonPanel.add(button_orders);
                }
                accountPanel.add(rightButtonPanel, BorderLayout.EAST);
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
                mainTitle.setBorder(new EmptyBorder(10, 0, 5, 0));
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

                //---- sortLabel ----
                sortLabel.setText(bundle.getString("MainPage.sortLabel.text"));
                sortLabel.setHorizontalTextPosition(SwingConstants.LEFT);
                sortLabel.setHorizontalAlignment(SwingConstants.TRAILING);
                sortLabel.setPreferredSize(null);
                filterPanel.add(sortLabel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 5, 0), 0, 0));
                filterPanel.add(sortOptions, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- priceFilterLabel ----
                priceFilterLabel.setText(bundle.getString("MainPage.priceFilterLabel.text"));
                filterPanel.add(priceFilterLabel, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                    new Insets(5, 0, 5, 0), 0, 0));
                filterPanel.add(priceFilterBox, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- typeFilterLabel ----
                typeFilterLabel.setText(bundle.getString("MainPage.typeFilterLabel.text"));
                filterPanel.add(typeFilterLabel, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                    new Insets(5, 0, 5, 0), 0, 0));
                filterPanel.add(typeFilterBox, new GridBagConstraints(0, 5, 1, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- brandFilterLabel ----
                brandFilterLabel.setText("BRAND");
                filterPanel.add(brandFilterLabel, new GridBagConstraints(0, 6, 1, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                    new Insets(5, 0, 5, 0), 0, 0));
                filterPanel.add(filterBox4, new GridBagConstraints(0, 7, 1, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- subTypeFilterLabel ----
                subTypeFilterLabel.setText(bundle.getString("MainPage.subTypeFilterLabel.text"));
                filterPanel.add(subTypeFilterLabel, new GridBagConstraints(0, 8, 1, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                    new Insets(5, 0, 5, 0), 0, 0));
                filterPanel.add(subTypeFilterBox, new GridBagConstraints(0, 9, 1, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));
            }
            mainPageSplitPane.setLeftComponent(filterPanel);

            //======== scrollPane1 ========
            {

                //======== productPanel ========
                {
                    productPanel.setMaximumSize(null);
                    productPanel.setPreferredSize(new Dimension(300, 1000));
                    productPanel.setMinimumSize(null);
                    productPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

                    //======== productCardPanel1 ========
                    {
                        productCardPanel1.setBorder(new LineBorder(new Color(0x002c7b), 2, true));
                        productCardPanel1.setPreferredSize(new Dimension(260, 280));
                        productCardPanel1.setMaximumSize(new Dimension(230, 240));
                        productCardPanel1.setMinimumSize(new Dimension(230, 240));
                        productCardPanel1.setLayout(new GridBagLayout());
                        ((GridBagLayout)productCardPanel1.getLayout()).columnWidths = new int[] {0, 0};
                        ((GridBagLayout)productCardPanel1.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0};
                        ((GridBagLayout)productCardPanel1.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
                        ((GridBagLayout)productCardPanel1.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0E-4};

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

                            //======== buttonPanel1 ========
                            {
                                buttonPanel1.setPreferredSize(new Dimension(240, 40));
                                buttonPanel1.setLayout(new FlowLayout());

                                //---- moreButton1 ----
                                moreButton1.setText("Detail");
                                moreButton1.setBackground(new Color(0x4e748d));
                                moreButton1.setForeground(new Color(0xe0e2e8));
                                moreButton1.setPreferredSize(new Dimension(100, 30));
                                buttonPanel1.add(moreButton1);

                                //---- addButton1 ----
                                addButton1.setText(bundle.getString("MainPage.addButton1.text"));
                                addButton1.setBackground(new Color(0x55a15a));
                                addButton1.setForeground(new Color(0xe0e2e8));
                                addButton1.setPreferredSize(new Dimension(100, 30));
                                buttonPanel1.add(addButton1);

                                //---- soldoutLabel1 ----
                                soldoutLabel1.setText("Out of Stock");
                                soldoutLabel1.setFont(soldoutLabel1.getFont().deriveFont(soldoutLabel1.getFont().getStyle() | Font.BOLD, soldoutLabel1.getFont().getSize() + 3f));
                                soldoutLabel1.setForeground(new Color(0x85816c));
                                buttonPanel1.add(soldoutLabel1);

                                //======== adjustNumPanel1 ========
                                {
                                    adjustNumPanel1.setLayout(new GridBagLayout());
                                    ((GridBagLayout)adjustNumPanel1.getLayout()).columnWidths = new int[] {0, 0, 0, 0};
                                    ((GridBagLayout)adjustNumPanel1.getLayout()).rowHeights = new int[] {0, 0, 0};
                                    ((GridBagLayout)adjustNumPanel1.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};
                                    ((GridBagLayout)adjustNumPanel1.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};

                                    //---- removeButton ----
                                    removeButton.setBackground(new Color(0xb13437));
                                    removeButton.setForeground(new Color(0xe0e2e8));
                                    removeButton.setFont(removeButton.getFont().deriveFont(removeButton.getFont().getSize() + 7f));
                                    removeButton.setIcon(new FlatSVGIcon("images/remove_white_18dp.svg"));
                                    adjustNumPanel1.add(removeButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                        new Insets(0, 0, 5, 5), 0, 0));

                                    //---- NumButton ----
                                    NumButton.setText("NUM");
                                    NumButton.setPreferredSize(new Dimension(50, 23));
                                    adjustNumPanel1.add(NumButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                        new Insets(0, 0, 5, 5), 0, 0));

                                    //---- addButton ----
                                    addButton.setBackground(new Color(0x55a15a));
                                    addButton.setForeground(new Color(0xe0e2e8));
                                    addButton.setFont(addButton.getFont().deriveFont(addButton.getFont().getSize() + 7f));
                                    addButton.setIcon(new FlatSVGIcon("images/add_white_18dp.svg"));
                                    adjustNumPanel1.add(addButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                        new Insets(0, 0, 5, 0), 0, 0));
                                }
                                buttonPanel1.add(adjustNumPanel1);
                            }
                            purchasePanel1.add(buttonPanel1);
                        }
                        productCardPanel1.add(purchasePanel1, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 0), 0, 0));
                    }
                    productPanel.add(productCardPanel1);
                }
                scrollPane1.setViewportView(productPanel);
            }
            mainPageSplitPane.setRightComponent(scrollPane1);
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
    private JPanel leftButtonPanel;
    private JButton button_account;
    private JButton button_staff;
    private JPanel rightButtonPanel;
    private JButton button_cart;
    private JButton button_orders;
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
    private JLabel sortLabel;
    private JComboBox<Filter.SortBy> sortOptions;
    private JLabel priceFilterLabel;
    private JComboBox<Filter.PriceRange> priceFilterBox;
    private JLabel typeFilterLabel;
    private JComboBox<Filter.TypeFilter> typeFilterBox;
    private JLabel brandFilterLabel;
    private JComboBox<Filter.BrandFilter> filterBox4;
    private JLabel subTypeFilterLabel;
    private JComboBox<Enum> subTypeFilterBox;
    private JScrollPane scrollPane1;
    private JPanel productPanel;
    private JPanel productCardPanel1;
    private JLabel productImage1;
    private JLabel productName1;
    private JPanel purchasePanel1;
    private JLabel productPrice1;
    private JPanel buttonPanel1;
    private JButton moreButton1;
    private JButton addButton1;
    private JLabel soldoutLabel1;
    private JPanel adjustNumPanel1;
    private JButton removeButton;
    private JButton NumButton;
    private JButton addButton;
    private JComponent bottomSeparator;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on

    private void customizeComponents() {
        productPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustPreferredSize(productPanel);
            }
        });
    }
    private static void adjustPreferredSize(JPanel panel) {
        int width = panel.getWidth();
        int preferredHeight = 1000000/width ;

        panel.setPreferredSize(new Dimension(300, preferredHeight));

        panel.revalidate();
    }


    private void loadProducts() {
        new SwingWorker<ArrayList<Product>, Void>() {
            @Override
            protected ArrayList<Product> doInBackground() throws Exception {
                // Get products from the database in the background thread
                Filter.PriceRange pr = (Filter.PriceRange)priceFilterBox.getSelectedItem();
                Filter.BrandFilter br = (Filter.BrandFilter)filterBox4.getSelectedItem();
                Filter.SortBy sb = (Filter.SortBy)sortOptions.getSelectedItem();
                Filter.TypeFilter tp = (Filter.TypeFilter)typeFilterBox.getSelectedItem();
                return ProductDAO.filterProducts(searchKeywordField.getText(), pr.getMin(), pr.getMax(), br.getBrand(), sb.getDbHandle(), sb.isAscending(), tp.getDbTable());
            }

            @Override
            protected void done() {
                try {
                    productPanel.removeAll();
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
                } catch (DatabaseException e) {
                    throw new RuntimeException(e);
                }
            }
        }.execute();
    }

    public JPanel createProductCard(Product product) throws DatabaseException {
        User currentUser = UserSession.getInstance().getCurrentUser();
        int itemID;
        int userID;

        if ( currentUser != null){
            userID = currentUser.getUserID();
            Cart cart = CartService.getCartDetails(userID);
            if ( cart != null){
                itemID = CartService.findItemID(cart.getCartID(), product.getProductID());
            }else {
                itemID = -1;
            }
        }else {
            itemID = -1;
            userID = -1;
        }


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
        ImageIcon originalIcon = product.getProductImage();
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
                (0, 1, 1, 1, 0.0, 0.8,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(1, 0, 0, 0), 0, 0));

        // Create price display labels
        JLabel productPrice = new JLabel();
        productPrice.setText("\u00A3" + String.format("%.2f", product.getRetailPrice()));
        productPrice.setFont(productPrice.getFont().deriveFont(productPrice.getFont().getSize() + 7f));
        productPrice.setHorizontalAlignment(SwingConstants.CENTER);

        // Create Detail Button
        JButton moreButton = new JButton("Detail");
        moreButton.setBackground(new Color(0x4e748d));
        moreButton.setForeground(new Color(0xe0e2e8));
        moreButton.setPreferredSize(new Dimension(100, 30));

        moreButton.addActionListener(e -> {
            ProductPage p = new ProductPage(product);
            p.setVisible(true);
        });


        // Create Add button
        JButton addButton = new JButton("ADD");
        addButton.setBackground(new Color(0x55a15a));
        addButton.setForeground(new Color(0xe0e2e8));
        addButton.setPreferredSize(new Dimension(100, 30));

        // "-"
        JButton minusButton = new JButton();
        minusButton.setBackground(new Color(0xb13437));
        minusButton.setForeground(new Color(0xe0e2e8));
        minusButton.setIcon(new FlatSVGIcon("images/remove_white_18dp.svg"));
        minusButton.setPreferredSize(new Dimension(30, 30));

        // "NUM"
        JButton numberButton = new JButton("1");
        numberButton.setHorizontalAlignment(SwingConstants.CENTER);
        numberButton.setPreferredSize(new Dimension(50, 30));

        // "+"
        JButton plusButton = new JButton();
        plusButton.setBackground(new Color(0x55a15a));
        plusButton.setForeground(new Color(0xe0e2e8));
        plusButton.setIcon(new FlatSVGIcon("images/add_white_18dp.svg"));
        plusButton.setPreferredSize(new Dimension(30, 30));

        JPanel adjustNumPanel = new JPanel(new GridBagLayout());
        adjustNumPanel.setPreferredSize(new Dimension(120, 30));
        ((GridBagLayout)adjustNumPanel.getLayout()).columnWidths = new int[] {0, 0, 0, 0};
        ((GridBagLayout)adjustNumPanel.getLayout()).rowHeights = new int[] {0, 0};
        ((GridBagLayout)adjustNumPanel.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};
        ((GridBagLayout)adjustNumPanel.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};
        adjustNumPanel.add(minusButton, new GridBagConstraints
                (0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 5), 0, 0));
        adjustNumPanel.add(numberButton, new GridBagConstraints
                (1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 5), 0, 0));
        adjustNumPanel.add(plusButton, new GridBagConstraints
                (2, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        if (itemID != -1) {
            adjustNumPanel.setVisible(true);
            addButton.setVisible(false);
            numberButton.setText(String.valueOf(CartService.findItemNUM(itemID)));
        }else {
            adjustNumPanel.setVisible(false);
        }

        JLabel soldOutLabel = new JLabel();
        soldOutLabel.setText("Out of Stock");
        soldOutLabel.setFont(soldoutLabel1.getFont().deriveFont(soldoutLabel1.getFont().getStyle() | Font.BOLD, soldoutLabel1.getFont().getSize() + 3f));
        soldOutLabel.setForeground(new Color(0x85816c));
        if (product.getStockQuantity() > 0){
            soldOutLabel.setVisible(false);
        }else{
            soldOutLabel.setVisible(true);
            adjustNumPanel.setVisible(false);
            addButton.setVisible(false);
        }


        // Adding event listeners to the "Add" button
        addButton.addActionListener(e -> {
            if (currentUser != null) {
                addButton.setVisible(false);
                adjustNumPanel.setVisible(true);
                Cart cart = CartService.getCartDetails(userID);
                int cartID = cart.getCartID();
                if (cartID == 0) {
                    CartService.createCart();
                    cartID = CartService.getCartDetails(userID).getCartID();
                }
                int productID = product.getProductID();
                CartService.addToCart(cartID, productID, 1);
            }else {
                // USER NOT LOGIN
                LoginPage loginPage = new LoginPage();
                loginPage.setVisible(true);
            }
        });

        // Adding event listeners to the "-" button
        minusButton.addActionListener(e -> {
            int num = Integer.parseInt(numberButton.getText());
            num--;
            if (num < 1) {
                adjustNumPanel.setVisible(false);
                addButton.setVisible(true);
                int cartID = CartService.getCartDetails(userID).getCartID();
                int productID = product.getProductID();
                if (!CartService.removeFromCart(cartID,productID)){
                    //TODO: Add action failed information
                }
            } else {
                numberButton.setText(String.valueOf(num));
                int cartID = CartService.getCartDetails(userID).getCartID();
                int productID = product.getProductID();
                if (!CartService.updateCartItem(cartID, productID, num)){
                    //TODO: Add action failed information
                }
            }
        });

        // Adding event listeners to the "+" button
        plusButton.addActionListener(e -> {
            int num = Integer.parseInt(numberButton.getText());
            num += 1;
            numberButton.setText(String.valueOf(num));
            int cartID = CartService.getCartDetails(userID).getCartID();
            int productID = product.getProductID();
                if (!CartService.updateCartItem(cartID, productID, num)){
                    //TODO: Add action failed information
                }
        });

        // Create a purchase panel and add price
        JPanel purchasePanel = new JPanel();
        purchasePanel.setBorder(new EmptyBorder(10, 5, 5, 5));
        purchasePanel.setMaximumSize(new Dimension(190, 85));
        purchasePanel.setLayout(new GridLayout(2, 1, 20, 0));
        purchasePanel.add(productPrice);

        // Create button panel and add buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setPreferredSize(new Dimension(240, 40));
        moreButton.setPreferredSize(new Dimension(115, 30));
        addButton.setPreferredSize(new Dimension(115, 30));
        buttonPanel.add(moreButton);
        buttonPanel.add(addButton);
        buttonPanel.add(adjustNumPanel);
        buttonPanel.add(soldOutLabel);

        // Add button panel to purchase panel
        purchasePanel.add(buttonPanel);

        // Adding the Purchase Panel to the Card Panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1.2;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        productCardPanel.add(purchasePanel, gbc);

        return productCardPanel;
    }

    /*
     * Main function for testing
     */
    public static void main(String[] args) {
        User user = UserDAO.findUserByEmail("testemail@gmail.com");
        Logging.Init(true);
        // User user = UserDAO.findUserByEmail("manager@manager.com");
        UserSession.getInstance().setCurrentUser(user);

        EventQueue.invokeLater(() -> {
            try {
                MainPage frame = new MainPage();
                // Close logging :D
                frame.addWindowListener(new WindowAdapter() {
                    public void windowClosing(WindowEvent e){
                        DatabaseConnectionHandler.shutdown();
                        Logging.Close();
                    }
                });
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
