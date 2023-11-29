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
import java.awt.Image;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
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

        this.setSize(new Dimension(640, 600));
        this.setResizable(true);
        this.setTitle((isStaff ? "Editing " : "Viewing ") + p.getProductName());

        initComponents();
        // Ensure nothing is default editing
        requestFocus();
        setLocationRelativeTo(null);
    }

    private JPanel createDescription() {
        JPanel descriptionPanel = new JPanel();
        descriptionPanel.setLayout(new BorderLayout());
        descriptionPanel.setBackground(new Color(0xFFFFFF));

        JLabel label = new JLabel("Description");
        label.setFont(label.getFont().deriveFont(Font.BOLD, 14.0f));
        descriptionPanel.add(label, BorderLayout.NORTH);

        productDescription = new JTextArea(p.getDescription());
        productDescription.setBorder(new EmptyBorder(5, 5, 5, 5));
        productDescription.setLineWrap(true);
        productDescription.setWrapStyleWord(true);
        productDescription.setBackground(new Color(0xFFFFFF));
        productDescription.setEditable(isStaff);

        JScrollPane scrollPane = new JScrollPane(productDescription);
        scrollPane.setPreferredSize(new Dimension(100, 50));
        descriptionPanel.add(scrollPane, BorderLayout.CENTER);

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

    private JPanel initImagePanel(){
        JPanel imagePanel = new JPanel();
        imagePanel.setBackground(new Color(0xFFFFFF));
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS));

        createProductImage();
        imagePanel.add(productImage);

        imagePanel.add(createDescription());

        return imagePanel;
    }

    private void addLabelComponent(String labelName, JComponent component, JPanel panel) {
        JLabel label = new JLabel(labelName);
        label.setFont(label.getFont().deriveFont(label.getFont().getStyle() | Font.BOLD, 14.0f));

        if (component instanceof JTextArea) {
            JScrollPane scrollPane = new JScrollPane(component);
            scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
            Dimension d = scrollPane.getPreferredSize();
            d.width = 200;
            d.height = 100;
            scrollPane.setPreferredSize(d);
        } else {
            component.setBorder(new EmptyBorder(0, 0, 0, 0));
            Dimension d = component.getPreferredSize();
            d.width = 200;
            component.setPreferredSize(d);
        }

        component.setBackground(new Color(0xFFFFFF));
        if (component instanceof JTextComponent) {
            ((JTextComponent) component).setEditable(isStaff);
        }

        panel.add(label);
        panel.add(component);
    }


    private JPanel initAttributePanel(){

        JPanel attributePanel = new JPanel();
        attributePanel.setBackground(new Color(0xFFFFFF));
        attributePanel.setLayout(new BoxLayout(attributePanel, BoxLayout.Y_AXIS));
        attributePanel.setMinimumSize(new Dimension(250, 300));
        attributePanel.setMaximumSize(new Dimension(250, 300));
        attributePanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        productName = new JTextField(p.getProductName());
        productCode = new JTextField(p.getProductCode());
        productPrice = new JTextField(String.format("\u00A3%.2f", p.getRetailPrice()));
        productStock = new JTextField(String.valueOf(p.getStockQuantity()));

        addLabelComponent("Product Name: ", productName, attributePanel);
        addLabelComponent("Product Code: ", productCode, attributePanel);
        addLabelComponent("Price: ", productPrice, attributePanel);
        addLabelComponent("Stock Quantity", productStock, attributePanel);

        gaugeComboBox = new JComboBox<>(new String[]{"OO", "TT", "N"});
        dccComboBox = new JComboBox<>(new String[]{"Analogue", "Ready", "Fitted", "Sound"});
        compartmentComboBox = new JComboBox<>(new String[]{"Wagon", "Carriage"});
        digitalComboBox = new JComboBox<>(new String[]{"Digital", "Analogue"});

        JButton eraSelectButton = new JButton("Select Era");
        eraSelectButton.addActionListener(e -> openEraSelectDialog(eraList));
        
        String productType = this.p.getProductType();
        try{
            switch (productType) {
                case "Track":
                    track = TrackDAO.findTrackByID(p.getProductID());
                    gaugeComboBox.setSelectedItem(track.getGauge());
                    addLabelComponent("Gauge:", gaugeComboBox, attributePanel);
                    break;
                case "Locomotive":
                    loco = LocomotiveDAO.findLocomotiveByID(p.getProductID());
                    gaugeComboBox.setSelectedItem(loco.getGauge());
                    dccComboBox.setSelectedItem(loco.getDCCType().getName());
                    setSelectedEra(loco.getEra());

                    addLabelComponent("Gauge:", gaugeComboBox, attributePanel);
                    addLabelComponent("DCC Type:", dccComboBox, attributePanel);
                    if(isStaff)
                        addLabelComponent("", eraSelectButton, attributePanel);
                    break;
                case "Rolling Stock":
                    roll = RollingStockDAO.findRollingStockByID(p.getProductID());
                    gaugeComboBox.setSelectedItem(roll.getGauge());
                    compartmentComboBox.setSelectedItem(roll.getRollingStockType());
                    setSelectedEra(roll.getEra());

                    addLabelComponent("Compartment Type:", compartmentComboBox, attributePanel);
                    addLabelComponent("Gauge:", gaugeComboBox, attributePanel);
                    if(isStaff)
                        addLabelComponent("", eraSelectButton, attributePanel);
                    break;
                case "Controller":
                    ctrl = ControllerDAO.findControllerByID(p.getProductID());
                    if (ctrl.getDigitalType()){
                        digitalComboBox.setSelectedItem("Digital");
                    } else {
                        digitalComboBox.setSelectedItem("Analogue");
                    }
                    addLabelComponent("Digital Type:", digitalComboBox, attributePanel);
                    break;
                case "Train Set":

                    break;
                case "Track Pack":

                    break;
            } 
        } catch (DatabaseException ex){
            ExceptionHandler.printErrorMessage(ex);
        }
        return attributePanel;
    }

    private JPanel initProductPanel(){
        JPanel productArea = new JPanel();
        productArea.setBackground(new Color(0xFFFFFF));
        productArea.setLayout(new BoxLayout(productArea, BoxLayout.X_AXIS));
        productArea.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        productArea.add(initImagePanel());
        productArea.add(initAttributePanel());
        return productArea;
    }

    private JPanel initProductButtons(){
        JPanel productButtons = new JPanel();
        productButtons.setBackground(new Color(0xFFFFFF));
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

    private void initComponents(){
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());

        JPanel content = new JPanel();
        content.setBackground(new Color(0xFFFFFF));
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(15, 15, 15, 15));

        content.add(initProductPanel());
        if(isStaff)
            content.add(initProductButtons());
        else
            Logging.getLogger().info("Showing the standard user view of the product page");
        
        contentPane.add(content);

        addListeners();
        pack();
    }

    public static void main(String[] args){
        UserSession.getInstance().setCurrentUser(UserDAO.findUserByEmail("testey@gmail.com"));
        ProductPage p = new ProductPage(ProductDAO.getAllProduct().get(0));
        p.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        p.setVisible(true);
    }
}



