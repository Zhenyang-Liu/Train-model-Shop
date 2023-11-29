package gui;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import gui.ProductManagePage.ProductTableModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import model.BoxedSet;
import model.Product;
import service.ProductService;

public class ProductSelectPage extends JDialog {
    private JTable allProductsTable;
    private String productType;
    private BoxedSet boxedSet;
    private JPanel rightPanel; 
    private boolean selectSingleController = false;

    public Map<Product, Integer> getSelection(){
        return boxedSet.getContain();
    }

    public ProductSelectPage(Dialog parent, String productType, Map<Product,Integer> itemList, boolean selectSingle) {
        super(parent, "Select Products", true);
        this.productType = productType;
        this.boxedSet = new BoxedSet();
        boxedSet.setContain(itemList);
        this.selectSingleController = selectSingle;
        initComponents();
    }

    private void initComponents() {
        setTitle("Select " + productType);
        setSize(800, 600);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
        JPanel leftPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        leftPanel.add(createLeftPanel(productType), gbc);

        rightPanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        addOrderItemsToWindow(rightPanel, boxedSet.getContain());
    
        JScrollPane scrollPane = new JScrollPane(rightPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
    
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, scrollPane);
        splitPane.setResizeWeight(0.4); 
    
        add(createTopPanel(), BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
    
        JLabel titleLabel = new JLabel("Product Selection", JLabel.CENTER);
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
            loadProductData(productType);
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

    private JPanel createLeftPanel(String productType) {
        JPanel productPanel = new JPanel(new BorderLayout());
        productPanel.setLayout(new BorderLayout());

        ProductTableModel tableModel = new ProductTableModel();
        allProductsTable = new JTable(tableModel);

        ButtonColumnListener listener = new ButtonColumnListener() {
            @Override
            public void onButtonClicked(int row, int column) {
                SwingUtilities.invokeLater(() -> {
                    handleAddButtonClicked(row);
                });
            }
        };        

        allProductsTable.getColumn("Add").setCellRenderer(new ButtonRenderer());
        allProductsTable.getColumn("Add").setCellEditor(new ButtonEditor(new JCheckBox(), listener));

        JScrollPane scrollPane = new JScrollPane(allProductsTable);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    
        productPanel.add(scrollPane, BorderLayout.CENTER);
    
        return productPanel;
    }

    private void addOrderItemsToWindow(JPanel rightPanel, Map<Product, Integer> items) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
    
        JLabel itemsTitleLabel = new JLabel("Selected "+productType);
        setTextStyle(itemsTitleLabel, true, 16);
        rightPanel.add(itemsTitleLabel, gbc);
        gbc.gridy++;
    
        if (items == null || items.isEmpty()) {
            JLabel noItemsLabel = new JLabel("You have not selected any " + productType);
            setTextStyle(noItemsLabel, false, 14);
            rightPanel.add(noItemsLabel, gbc);
            return;
        }
    
        for (Map.Entry<Product, Integer> entry : items.entrySet()) {
            JPanel itemPanel = createOrderItemPanel(entry.getKey(), entry.getValue());
            rightPanel.add(itemPanel, gbc);
            gbc.gridy++;
        }
    }    

    private void setTextStyle(JLabel label, boolean isHeader, int fontSize) {
        label.setFont(new Font(label.getFont().getName(), isHeader ? Font.BOLD : Font.PLAIN, fontSize));
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

    private void performSearch(String searchType, String searchTerm) {
        // TODO: applySearch
    }

    private void loadProductData(String filter) {
        ArrayList<Product> filteredProducts = ProductService.getAllProductsByType(filter);
        updateTableModel(filteredProducts);
    }    

    private void updateTableModel(ArrayList<Product> products) {
        ProductTableModel model = (ProductTableModel) allProductsTable.getModel();
        model.setProductList(products);
        model.fireTableDataChanged();
    }

    private void updateRightPanel() {
        rightPanel.removeAll();
        addOrderItemsToWindow(rightPanel, boxedSet.getContain());
        
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    private void handleAddButtonClicked(int row) {
        if (selectSingleController) {
            Product selectedProduct = ((ProductTableModel) allProductsTable.getModel()).getProductAt(row);
            boxedSet.getContain().clear();
            boxedSet.addProduct(selectedProduct, 1);
            updateRightPanel();
        } else {
            Product selectedProduct = ((ProductTableModel) allProductsTable.getModel()).getProductAt(row);
            int maxQuantity = selectedProduct.getStockQuantity();
            String quantityString = JOptionPane.showInputDialog(
                this, 
                "Enter quantity (Max: " + maxQuantity + "):", 
                "Select Quantity", 
                JOptionPane.PLAIN_MESSAGE
            );
        
            try {
                int quantity = Integer.parseInt(quantityString);
                if (quantity > 0 && quantity <= maxQuantity) {
                    boxedSet.addProduct(selectedProduct, quantity);
                    updateRightPanel();
                } else {
                    JOptionPane.showMessageDialog(
                        this, 
                        "Invalid quantity. Please enter a number between 1 and " + maxQuantity + ".", 
                        "Invalid Input", 
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(
                    this, 
                    "Invalid input. Please enter a valid number.", 
                    "Invalid Input", 
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    // JTable Model
    class ProductTableModel extends AbstractTableModel {
        private final String[] columnNames = { "Brand", "Name", "Code", "Price", "Quantity", "Add"};
        private ArrayList<Product> productList;
    
        public ProductTableModel() {
            this.productList = new ArrayList<>();
            loadInitialData();
        }
    
        private void loadInitialData() {
            this.productList = ProductService.getAllProductsByType(productType);
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
                case 5: return "Add";
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
        //TODO: add quantity
        AddProductPage addProductPage = new AddProductPage(null);
        addProductPage.pack();
        addProductPage.setSize(500, 400);
        addProductPage.setLocationRelativeTo(null);
        addProductPage.setVisible(true);
    }

}

