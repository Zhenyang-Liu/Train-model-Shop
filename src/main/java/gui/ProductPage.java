/*
 * Created by JFormDesigner on Fri Nov 10 16:19:34 GMT 2023
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.text.JTextComponent;

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

    public ProductPage(Product p){
        this.p = p;
        this.isStaff = PermissionService.hasPermission(UserSession.getInstance().getCurrentUser().getUserID(), "UPDATE_PRODUCT");
    
        initComponents();
    
        this.setTitle((isStaff ? "Editing " : "Viewing ") + p.getProductName());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(new Dimension(700, 500));
        // this.setBackground(new Color(0xFFFFFF));
        this.setResizable(true);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }    

    private JPanel createDescription() {
        JPanel descriptionPanel = new JPanel(new GridBagLayout());
        // descriptionPanel.setBackground(new Color(0xFFFFFF));
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
        // productDescription.setBackground(new Color(0xFFFFFF));
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
        // productImage.setAlignmentX(Component.LEFT_ALIGNMENT);

    }

    private void updateImage(){
        final JFileChooser fc = new JFileChooser();
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
        // imagePanel.setBackground(new Color(0xFFFFFF));
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
        // attributePanel.setBackground(new Color(0xFFFFFF));
        attributePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
    
        productName = new JTextField(p.getProductName());
        productCode = new JTextField(p.getProductCode());
        productPrice = new JTextField(String.format("\u00A3%.2f", p.getRetailPrice()));
        productStock = new JTextField(String.valueOf(p.getStockQuantity()));
    
        addLabelComponent("Product Name: ", productName, attributePanel, gbc);
        gbc.gridy++;
        addLabelComponent("Product Code: ", productCode, attributePanel, gbc);
        gbc.gridy++;
        addLabelComponent("Price: ", productPrice, attributePanel, gbc);
        gbc.gridy++;
        addLabelComponent("Stock Quantity: ", productStock, attributePanel, gbc);
        gbc.gridy++;
    
        gaugeComboBox = new JComboBox<>(new String[]{"OO", "TT", "N"});
        dccComboBox = new JComboBox<>(new String[]{"Analogue", "Ready", "Fitted", "Sound"});
        compartmentComboBox = new JComboBox<>(new String[]{"Wagon", "Carriage"});
        digitalComboBox = new JComboBox<>(new String[]{"Digital", "Analogue"});

        JButton eraSelectButton = new JButton("Select Era");
        eraSelectButton.addActionListener(e -> openEraSelectDialog(eraList));
        
        String productType = this.p.getProductType();
        try {
            switch (productType) {
                case "Track":
                    track = TrackDAO.findTrackByID(p.getProductID());
                    gaugeComboBox.setSelectedItem(track.getGauge());
                    if (isStaff)
                        addLabelComponent("Gauge: ", gaugeComboBox, attributePanel, gbc);
                    else 
                        addLabelComponent("Gauge: ", new JTextField(track.getGauge()), attributePanel, gbc);
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
                        //TODO: display Era
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
                        //TODO: display Era
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
                    // Add components for Train Set
                    break;
                case "Track Pack":
                    // Add components for Track Pack
                    break;
            } 
        } catch (DatabaseException ex) {
            ExceptionHandler.printErrorMessage(ex);
        }
        return attributePanel;
    }

    private JPanel initProductPanel(){
        JPanel productArea = new JPanel();
        // productArea.setBackground(new Color(0xFFFFFF));
        productArea.setLayout(new BoxLayout(productArea, BoxLayout.X_AXIS));
        productArea.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        productArea.add(initImagePanel());
        productArea.add(initAttributePanel());
        return productArea;
    }

    private JPanel initProductButtons(){
        JPanel productButtons = new JPanel();
        // productButtons.setBackground(new Color(0xFFFFFF));
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
        // Saving products
        if(!isStaff)
            return;
        saveProduct.addActionListener(e -> {
            try{
                this.p.setProductName(productName.getText());
                // this.p.setDescription(productDescription.getText());
                // TODO: Update Product
                ProductDAO.updateProduct(p);
            }catch(DatabaseException e1){
                Logging.getLogger().warning("Could not update product " + p.getProductID() + "\n Stacktrace: " + e1.getMessage());
            }
            
        });
        deleteProduct.addActionListener(e -> {
            try {
                JOptionPane.showMessageDialog(new JFrame(), "Are you sure you want to delete this product? This cannot be undone.", "Dialog", JOptionPane.OK_CANCEL_OPTION);
                ProductDAO.deleteProduct(p.getProductID());
            } catch (DatabaseException e1) {
                Logging.getLogger().warning("Could not delete product " + p.getProductID());
            }
        });
    }

    private void initComponents() {
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        // contentPane.setBackground(new Color(0xFFFFFF));

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
    
    public static void main(String[] args){
        // UserSession.getInstance().setCurrentUser(UserDAO.findUserByEmail("manager@manager.com"));
        UserSession.getInstance().setCurrentUser(UserDAO.findUserByEmail("testemail@gmail.com"));
        ProductPage p = new ProductPage(ProductDAO.getAllProduct().get(0));
        p.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        p.setVisible(true);
    }
}



