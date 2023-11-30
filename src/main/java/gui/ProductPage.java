/*
 * Created by JFormDesigner on Fri Nov 10 16:19:34 GMT 2023
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.JTextComponent;

import DAO.BoxedSetDAO;
import DAO.ControllerDAO;
import DAO.LocomotiveDAO;
import DAO.ProductDAO;
import DAO.RollingStockDAO;
import DAO.TrackDAO;
import DAO.UserDAO;
import exception.DatabaseException;
import exception.ExceptionHandler;
import helper.ImageUtils;
import helper.Logging;
import helper.UserSession;
import model.*;
import service.PermissionService;
import service.ProductService;

public class ProductPage extends JFrame {

    private Product p;
    private Track track;
    private Locomotive loco;
    private RollingStock roll;
    private Controller ctrl;
    private BoxedSet set;
    private List<Integer> eraList;

    private boolean isStaff;

    private JTextField productName, productPrice,productStock,productBrand,productCode;
    private JTextArea productDescription;
    private JLabel productImage;
    private JComboBox<String> gaugeComboBox, dccComboBox, compartmentComboBox, digitalComboBox;
    private JButton saveProduct;
    private JButton deleteProduct;
    private ProductManagePage managePage;

    public ProductPage(ProductManagePage managePage, Product product) {
        this.managePage = managePage;
        initializePage(product);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                if (managePage != null) {
                    managePage.refreshTableData();
                }
            }
        });
    }

    public ProductPage(Product product) {
        initializePage(product);
    }

    private void initializePage(Product product) {
        this.p = product;
        this.isStaff = PermissionService.hasPermission(UserSession.getInstance().getCurrentUser().getUserID(), "UPDATE_PRODUCT");

        initComponents();

        setTitle((isStaff ? "Editing " : "Viewing ") + p.getProductName());
        setSize(new Dimension(700, 500));
        setResizable(true);
        setLocationRelativeTo(null);
        setVisible(true);
        pack();
    }    

    private JPanel createDescription() {
        JPanel descriptionPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; 
        gbc.weighty = 1.0; 
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
    
        JLabel label = new JLabel("Description");
        label.setFont(label.getFont().deriveFont(Font.BOLD, 14.0f));
        descriptionPanel.add(label, gbc);
    
        gbc.gridy++;
    
        productDescription = new JTextArea(p.getDescription());
        productDescription.setBorder(new EmptyBorder(5, 5, 5, 5));
        productDescription.setLineWrap(true);
        productDescription.setWrapStyleWord(true);
        productDescription.setEditable(isStaff);
    
        JScrollPane scrollPane = new JScrollPane(productDescription);
        scrollPane.setPreferredSize(new Dimension(200, 50));
        descriptionPanel.add(scrollPane, gbc);
    
        return descriptionPanel;
    }   

    private JButton createButton(String text, Color c){
        JButton rButton = new JButton(text);
        rButton.setBackground(c);
        rButton.setForeground(Color.WHITE);
        return rButton;
    }

    private void createProductImage(){

        productImage = new JLabel();
        productImage.setPreferredSize(new Dimension(256, 140));

        ImageIcon originalIcon = this.p.getProductImage();
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(256, 140, Image.SCALE_SMOOTH);
        productImage.setIcon(new ImageIcon(resizedImage));
        productImage.setBorder(new LineBorder(Color.BLACK));

    }

    private void updateImage(){
        final JFileChooser fc = new JFileChooser();
        fc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "png");
        fc.setFileFilter(filter);
        int returnVal = fc.showOpenDialog(this);
        if(returnVal != JFileChooser.APPROVE_OPTION)
            return;
        String imageBase64 = ImageUtils.toBase64(fc.getSelectedFile());

        p.setImageBase64(imageBase64);

        productImage.setIcon(ImageUtils.imageToIcon(imageBase64));
        productImage.repaint();
    }

    private JPanel initImagePanel() {
        JPanel imagePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
    
        createProductImage();
        imagePanel.add(productImage, gbc);
    
        gbc.gridy++;
        imagePanel.add(createDescription(), gbc);
    
        return imagePanel;
    }    

    private void addLabelComponent(String labelName, JComponent component, JPanel panel, GridBagConstraints gbc) {
        JLabel label = createStyledLabel(labelName);
        label.setFont(label.getFont().deriveFont(label.getFont().getStyle() | Font.BOLD, 14.0f));
        
        if (component instanceof JTextArea) {
            JScrollPane scrollPane = new JScrollPane(component);
            scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
            Dimension d = scrollPane.getPreferredSize();
            d.width = 200;
            d.height = 100;
            scrollPane.setPreferredSize(d);
            gbc.gridwidth = 2;
            panel.add(scrollPane, gbc);
            gbc.gridwidth = 1;
        } else {
            component.setBorder(new EmptyBorder(0, 0, 0, 0));
            Dimension d = component.getPreferredSize();
            d.width = 200;
            component.setPreferredSize(d);
            panel.add(label, gbc);
            gbc.gridx++;
            panel.add(component, gbc);
            gbc.gridx--;
        }
        
        gbc.gridy++;
    
        if (component instanceof JTextComponent) {
            ((JTextComponent) component).setEditable(isStaff);
        }
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 14.0f));
        label.setForeground(new Color(0x003366));
        return label;
    }

    private JPanel initAttributePanel() {
        JPanel attributePanel = new JPanel();
        attributePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
    
        productName = new JTextField(p.getProductName());
        productCode = new JTextField(p.getProductCode());
        productPrice = new JTextField(String.format("%.2f", p.getRetailPrice()));
        productStock = new JTextField(String.valueOf(p.getStockQuantity()));
        productBrand = new JTextField(p.getBrand());
    
        addLabelComponent("Product Name: ", productName, attributePanel, gbc);
        gbc.gridy++;
        addLabelComponent("Product Code: ", productCode, attributePanel, gbc);
        gbc.gridy++;
        addLabelComponent("Brand: ", productBrand, attributePanel, gbc);
        gbc.gridy++;
        addLabelComponent("Price "+"\u00A3"+": ", productPrice, attributePanel, gbc);
        gbc.gridy++;
        addLabelComponent("Stock Quantity: ", productStock, attributePanel, gbc);
        gbc.gridy++;

        productCode.setEditable(false);
    
        gaugeComboBox = new JComboBox<>(new String[]{"OO", "TT", "N"});
        dccComboBox = new JComboBox<>(new String[]{"Analogue", "Ready", "Fitted", "Sound"});
        compartmentComboBox = new JComboBox<>(new String[]{"Wagon", "Carriage"});
        digitalComboBox = new JComboBox<>(new String[]{"Digital", "Analogue"});

        JButton eraSelectButton = createButton("Select Era", new Color(0x003366));
        eraSelectButton.addActionListener(e -> openEraSelectDialog(eraList));
        
        String productType = this.p.getProductType();
        try {
            switch (productType) {
                case "Track":
                    track = TrackDAO.findTrackByID(p.getProductID());
                    gaugeComboBox.setSelectedItem(track.getGauge());
                    if (isStaff) {
                        addLabelComponent("Gauge: ", gaugeComboBox, attributePanel, gbc);
                    } else {
                        addLabelComponent("Gauge: ", new JTextField(track.getGauge()), attributePanel, gbc);
                    } 
                    gbc.gridy++;
                    break;
                case "Locomotive":
                    loco = LocomotiveDAO.findLocomotiveByID(p.getProductID());
                    gaugeComboBox.setSelectedItem(loco.getGauge());
                    dccComboBox.setSelectedItem(loco.getDCCType().getName());
                    setSelectedEra(loco.getEra());

                    if (isStaff) {
                        addLabelComponent("Gauge: ", gaugeComboBox, attributePanel, gbc);
                        gbc.gridy++;
                        addLabelComponent("DCC Type: ", dccComboBox, attributePanel, gbc);
                        gbc.gridy++;
                        addLabelComponent("", eraSelectButton, attributePanel, gbc);
                    } else  {
                        addLabelComponent("Gauge: ", new JTextField(loco.getGauge()), attributePanel, gbc);
                        gbc.gridy++;
                        addLabelComponent("DCC Type: ", new JTextField(loco.getDCCType().getName()), attributePanel, gbc);
                        gbc.gridy++;
                        for(String description : ProductService.findEraDescription(loco.getEra())){
                            JLabel d = new JLabel(description);
                            gbc.gridwidth = 2;
                            attributePanel.add(d, gbc);
                            gbc.gridwidth = 1;
                            gbc.gridy++;
                        }

                    }
                    break;
                case "Rolling Stock":
                    roll = RollingStockDAO.findRollingStockByID(p.getProductID());
                    gaugeComboBox.setSelectedItem(roll.getGauge());
                    compartmentComboBox.setSelectedItem(roll.getRollingStockType());
                    setSelectedEra(roll.getEra());
                    if (isStaff) {
                        addLabelComponent("Gauge: ", gaugeComboBox, attributePanel, gbc);
                        gbc.gridy++;
                        addLabelComponent("Compartment Type: ", compartmentComboBox, attributePanel, gbc);
                        gbc.gridy++;
                        addLabelComponent("", eraSelectButton, attributePanel, gbc);
                    } else  {
                        addLabelComponent("Gauge: ", new JTextField(roll.getGauge()), attributePanel, gbc);
                        gbc.gridy++;
                        addLabelComponent("Compartment Type: ", new JTextField(roll.getRollingStockType()), attributePanel, gbc);
                        gbc.gridy++;
                        for(String description : ProductService.findEraDescription(roll.getEra())){
                            JLabel d = new JLabel(description);
                            gbc.gridwidth = 2;
                            attributePanel.add(d, gbc);
                            gbc.gridwidth = 1;
                            gbc.gridy++;
                        }
                    }
                    break;
                case "Controller":
                    ctrl = ControllerDAO.findControllerByID(p.getProductID());
                    digitalComboBox.setSelectedItem(ctrl.getDigitalType() ? "Digital" : "Analogue");
                    if (isStaff) {
                        addLabelComponent("Digital Type: ", digitalComboBox, attributePanel, gbc);
                    }else {
                        addLabelComponent("Digital Type: ", new JTextField(ctrl.getDigitalType() ? "Digital" : "Analogue"), attributePanel, gbc);
                    }
                    break;
                case "Train Set":
                    set = BoxedSetDAO.findBoxedSetByID(p.getProductID());
                    addLabelComponent(productType, createStyledLabel("Include: "), attributePanel, gbc);
                    gbc.gridy++;
                    for(Map.Entry<Product,Integer> entry : set.getContain().entrySet()){
                        Product setItem = entry.getKey();
                        JLabel d = new JLabel(setItem.getProductCode()+" "+setItem.getProductName() + "  x " +String.valueOf(entry.getValue()) + " ;");
                        gbc.gridwidth = 2;
                        attributePanel.add(d, gbc);
                        gbc.gridwidth = 1;
                        gbc.gridy++;
                    }
                    break;
                case "Track Pack":
                    set = BoxedSetDAO.findBoxedSetByID(p.getProductID());
                    addLabelComponent(productType, createStyledLabel("Include: "), attributePanel, gbc);
                    gbc.gridy++;
                    for(Map.Entry<Product,Integer> entry : set.getContain().entrySet()){
                        Product setItem = entry.getKey();
                        JLabel d = new JLabel(setItem.getProductCode()+" "+setItem.getProductName() + "  x " +String.valueOf(entry.getValue()) + " ;");
                        gbc.gridwidth = 2;
                        attributePanel.add(d, gbc);
                        gbc.gridwidth = 1;
                        gbc.gridy++;
                    }
                    break;
            } 
        } catch (DatabaseException ex) {
            ExceptionHandler.printErrorMessage(ex);
        }
        return attributePanel;
    }

    private JPanel initProductPanel(){
        JPanel productArea = new JPanel();
        productArea.setLayout(new BoxLayout(productArea, BoxLayout.X_AXIS));
        productArea.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        productArea.add(initImagePanel());
        productArea.add(initAttributePanel());
        return productArea;
    }

    private JPanel initProductButtons(){
        JPanel productButtons = new JPanel();
        deleteProduct = createButton("Delete product", new Color(0xBC2626));
        saveProduct = createButton("Save", new Color(0x82be73));
        saveProduct.setPreferredSize(deleteProduct.getPreferredSize());
        productButtons.add(saveProduct);
        productButtons.add(deleteProduct);
        return productButtons;
    }

    private void openEraSelectDialog(List<Integer> eras) {
        EraSelect eraSelect = new EraSelect(null, eras);
        eraSelect.setVisible(true);
        List<Integer> selectedEras = eraSelect.getSelectedEras();
        setSelectedEra(selectedEras);
    }

    private void setSelectedEra(List<Integer> eraList) {
        this.eraList = eraList;
    }

    private void setSelectedEra(int[] eraCollection) {
        this.eraList = new ArrayList<>();;
        for (int era: eraCollection){
            eraList.add(era);
        }
    }

    private void addListeners(){
        productImage.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e){
                updateImage();
            }
            @Override
            public void mouseEntered(MouseEvent e){
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e){
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        if(!isStaff)
            return;
        saveProduct.addActionListener(e -> {submitProduct();});
        deleteProduct.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to delete this product? This cannot be undone.", 
                "Confirm Deletion", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE);
        
            if (confirm == JOptionPane.YES_OPTION) {
                String deleteResult = ProductService.deleteProduct(p);
                if ("success".equalsIgnoreCase(deleteResult)) {
                    JOptionPane.showMessageDialog(this, 
                        "Product " + p.getProductCode() + " deleted successfully.", 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Failed to delete product. Error: " + deleteResult, 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
    }

    private void initComponents() {
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());

        JPanel content = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
    
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
    
        JPanel productArea = initProductPanel();
        content.add(productArea, gbc);
    
        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
    
        if (isStaff) {
            JPanel productButtons = initProductButtons();
            content.add(productButtons, gbc);
        } else {
            Logging.getLogger().info("Showing the standard user view of the product page");
        }
    
        contentPane.add(content);
    
        addListeners();
        pack();
        setLocationRelativeTo(null);
    }

    private void submitProduct() {
        String selectedType = p.getProductType();
        JLabel errorLabel = new JLabel();

        String newName = productName.getText();
        String newCode = productCode.getText().toUpperCase();
        String newBrand = productBrand.getText();
        String newPrice = productPrice.getText();
        String newQuantity = productStock.getText();
        String newDes = productDescription.getText();

        String validationResult = ProductService.validateProductInput(newName, newCode, newBrand, newPrice, newQuantity);
        String ignore = "Product Code has existed in database.";
        if (!validationResult.equals(ignore) && validationResult != null) {
            JOptionPane.showMessageDialog(null,validationResult, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        p.setBrand(newBrand);
        p.setProductCode(newCode);
        p.setProductName(newName);
        p.setRetailPrice(Double.parseDouble(newPrice));
        p.setStockQuantity(Integer.parseInt(newQuantity));
        p.setDescription(newDes);
        String result = ProductService.updateProduct(p);
        if (!"success".equals(result)){
            JOptionPane.showMessageDialog(null,"Update failed. "+result, "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try{
            switch (selectedType) {
                case "Track":
                    String selectedGauge = gaugeComboBox.getSelectedItem().toString();
                    Track track = new Track(p, selectedGauge);
                    TrackDAO.updateTrack(track);
                    break;
                case "Controller":
                    boolean selectedDigitalType = digitalComboBox.getSelectedItem().equals("Digital");
                    Controller controller = new Controller(p, selectedDigitalType);
                    ControllerDAO.updateController(controller);
                    break;
                case "Locomotive":
                    String selectedDccType = dccComboBox.getSelectedItem().toString();
                    String selectedGaugeForLoco = gaugeComboBox.getSelectedItem().toString();
                    int[] era = eraList.stream().mapToInt(i -> i).toArray();
                    Locomotive locomotive = new Locomotive(p, selectedGaugeForLoco,selectedDccType,era);
                    LocomotiveDAO.updateLocomotive(locomotive);
                    break;
                case "Rolling Stock":
                    String selectedCompartmentType = compartmentComboBox.getSelectedItem().toString();
                    String selectedGaugeForRoll = gaugeComboBox.getSelectedItem().toString();
                    int[] era1 = eraList.stream().mapToInt(i -> i).toArray();
                    RollingStock rollingStock = new RollingStock(p,selectedCompartmentType,selectedGaugeForRoll,era1);
                    RollingStockDAO.updateRollingStock(rollingStock);
                    break;
                case "Train Set":
                    break;
                case "Track Pack":
                    break;
                }
            } catch (Exception ex) {
                Logging.getLogger().warning("Could not update product " + p.getProductID() + "\n Stacktrace: " + ex.getMessage());
                JOptionPane.showMessageDialog(null,"Update failed. "+ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

        errorLabel.setVisible(false);
        JOptionPane.showMessageDialog(this, "Product updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        setVisible(false);
    }
    
    public static void main(String[] args){
        // UserSession.getInstance().setCurrentUser(UserDAO.findUserByEmail("testemail@gmail.com"));
        UserSession.getInstance().setCurrentUser(UserDAO.findUserByEmail("testey@gmail.com"));
        ProductPage p = new ProductPage(ProductDAO.getAllProduct().get(6));
        p.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        p.setVisible(true);
    }
}



