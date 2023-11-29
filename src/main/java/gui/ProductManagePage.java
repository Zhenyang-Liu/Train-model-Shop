package gui;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import DAO.ProductDAO;
import DAO.UserDAO;
import helper.UserSession;

import java.awt.*;
import java.util.ArrayList;

import model.*;
import service.ProductService;

public class ProductManagePage extends JFrame {
    private JTable productTable;
    private String currentTypeFilter;

    public ProductManagePage() {
        setTitle("Product Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        currentTypeFilter = "All";

        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        JPanel productPanel = createProductPanel();
        add(productPanel, BorderLayout.CENTER);

        JPanel filterPanel = createProductFilterPanel();
        add(filterPanel, BorderLayout.SOUTH);
        
        setLocationRelativeTo(null);
        setVisible(true);

        loadProductData("all");
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
    
        JLabel titleLabel = new JLabel("Product Management", JLabel.CENTER);
        titleLabel.setFont(new Font(titleLabel.getFont().getFontName(), Font.BOLD, 26));
        titleLabel.setForeground(new Color(0x003366));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        topPanel.add(titleLabel, gbc);
    
        String[] searchOptions = {"Email", "ProductID"};
        JComboBox<String> searchComboBox = new JComboBox<>(searchOptions);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        topPanel.add(searchComboBox, gbc);
    
        JTextField searchField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        topPanel.add(searchField, gbc);
    
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(0x204688));
        searchButton.setForeground(Color.WHITE);
        gbc.gridx = 2;
        gbc.gridy = 1;
        topPanel.add(searchButton, gbc);

        searchButton.addActionListener(e -> performSearch((String)searchComboBox.getSelectedItem(), searchField.getText()));
    
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> {
            searchField.setText("");
            searchComboBox.setSelectedIndex(0);
            loadProductData(currentTypeFilter);
        });
        gbc.gridx = 3;
        gbc.gridy = 1;
        topPanel.add(clearButton, gbc);

        JButton addButton = new JButton("Add Product");
        addButton.setBackground(new Color(34, 139, 34));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> {
            openAddProductDialog();
        });
        gbc.gridx = 4;
        gbc.gridy = 1;
        topPanel.add(addButton, gbc);

        return topPanel;
    }

    private JPanel createProductPanel() {
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BorderLayout());
    
        ProductTableModel tableModel = new ProductTableModel();
        productTable = new JTable(tableModel);
    
        ButtonColumnListener listener = new ButtonColumnListener() {
            @Override
            public void onButtonClicked(int row, int column) {
                Product selectedProduct = tableModel.getProductAt(row);
                SwingUtilities.invokeLater(() -> {
                    openDetailDialog(selectedProduct.getProductID(), selectedProduct.getProductType());
                });
            }
        };
    
        productTable.getColumn("Details").setCellRenderer(new ButtonRenderer());
        productTable.getColumn("Details").setCellEditor(new ButtonEditor(new JCheckBox(), listener));
    
        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    
        productPanel.add(scrollPane, BorderLayout.CENTER);
    
        return productPanel;
    }

    private JPanel createProductFilterPanel() {
        JPanel filterPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel filterLabel = new JLabel("Filter by Type:");
        filterPanel.add(filterLabel, gbc);
        
        String[] productTypes = {"All", "Track", "Locomotive", "Rolling Stock", "Controller", "Train Set", "Track Pack"};
        JComboBox<String> productTypeComboBox = new JComboBox<>(productTypes);
        productTypeComboBox.addActionListener(e -> {
            String selectedType = (String) productTypeComboBox.getSelectedItem();
            loadProductData(selectedType);
        });
    
        gbc.gridx = 1;
        filterPanel.add(productTypeComboBox, gbc);
    
        return filterPanel;
    }

    private void performSearch(String searchType, String searchTerm) {
        // TODO: applySearch
    }

    private void loadProductData(String filter) {
        ArrayList<Product> filteredProducts = ProductService.getAllProductsByType(filter);
        updateTableModel(filteredProducts);
    } 

    public void refreshProductList() {
        loadProductData(currentTypeFilter);
    }

    private void updateTableModel(ArrayList<Product> products) {
        ProductTableModel model = (ProductTableModel) productTable.getModel();
        model.setProductList(products);
        model.fireTableDataChanged();
    }   
  
    // Inner Class
    class ProductTableModel extends AbstractTableModel {
        private final String[] columnNames = { "Brand", "Name", "Code", "Price", "Quantity", "Details"};
        private ArrayList<Product> productList;
    
        public ProductTableModel() {
            this.productList = new ArrayList<>();
            loadInitialData();
        }
    
        private void loadInitialData() {
            this.productList = ProductService.getAllProducts();
        }
    
        @Override
        public int getRowCount() {
            return productList.size();
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
            Product product = productList.get(rowIndex);
            switch (columnIndex) {
                case 0: return product.getBrand();
                case 1: return product.getProductName();
                case 2: return product.getProductCode();
                case 3: return String.format("\u00A3%.2f", product.getRetailPrice());
                case 4: return product.getStockQuantity();
                case 5: return "Details";
                default: return null;
            }
        }
    
        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 5;
        }

        public void setProductList(ArrayList<Product> newProducts) {
            if (newProducts != null) {
                productList = newProducts;
            } else {
                productList.clear();
            }
            fireTableDataChanged();
        }

        public Product getProductAt(int rowIndex) {
            return productList.get(rowIndex);
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
        private boolean isPushed;
        private ButtonColumnListener listener;
        private int editingRow;
        private int editingColumn;
    
        public ButtonEditor(JCheckBox checkBox, ButtonColumnListener listener) {
            super(checkBox);
            this.button = new JButton();
            this.button.setOpaque(true);
            this.listener = listener;
            this.button.addActionListener(e -> {
                isPushed = true;
                fireEditingStopped();
            });
        }
    
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.editingRow = row;
            this.editingColumn = column;
            button.setText((value == null) ? "" : value.toString());
            return button;
        }
    
        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                if (listener != null) {
                    listener.onButtonClicked(editingRow, editingColumn);
                }
                isPushed = false;
            }
            return button.getText();
        }
    
        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
    
    public interface ButtonColumnListener {
        void onButtonClicked(int row, int column);
    }

    private void openAddProductDialog() {
        AddProductPage addProductPage = new AddProductPage(this);
        addProductPage.pack();
        addProductPage.setSize(500, 400);
        addProductPage.setLocationRelativeTo(null);
        addProductPage.setVisible(true);

        addProductPage.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                refreshProductList();
            }
        });
    }

    private void openDetailDialog(int productID, String productTypeString) {
        ProductPage p = new ProductPage(ProductService.findProductByID(productID));
        p.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        p.setVisible(true);

        p.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                refreshProductList();
            }
        });
    }
    
    public static void main(String[] args) {
        User user = UserDAO.findUserByEmail("manager@manager.com");
        UserSession.getInstance().setCurrentUser(user);

        SwingUtilities.invokeLater(() -> {
            ProductManagePage frame = new ProductManagePage();
            frame.setVisible(true);
        });
    }
}
