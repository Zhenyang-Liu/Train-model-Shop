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

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import DAO.ProductDAO;
import DAO.UserDAO;
import exception.DatabaseException;
import helper.ImageUtils;
import helper.Logging;
import helper.UserSession;
import model.Product;
import service.PermissionService;

public class ProductPage extends JFrame {

    private Product p;
    private boolean isStaff;

    private JTextField productName;
    private JTextField productDescription;
    private JTextField productPrice;
    private JComboBox productType;
    private JLabel productImage;
    private JLabel productID;
    private JButton saveProduct;
    private JButton deleteProduct;

    public ProductPage(Product p){
        this.p = p;
        this.isStaff = PermissionService.hasPermission(UserSession.getInstance().getCurrentUser().getUserID(), "UPDATE_PRODUCT");

        this.setSize(new Dimension(640, 480));
        this.setResizable(false);
        this.setTitle((isStaff ? "Editing " : "Viewing ") + p.getProductName());

        initComponents();
        // Ensure nothing is default editing
        requestFocus();
    }

    private void createProductID(){
        productID = new JLabel("Product ID: " + String.valueOf(p.getProductID()));
        productID.setFont(productID.getFont().deriveFont(productID.getFont().getStyle() | Font.ITALIC, 14.0f));
        productID.setForeground(new Color(0, 0, 0, 0.3f));
        productID.setHorizontalAlignment(SwingConstants.RIGHT);
        // productID.setAlignmentX(Component.RIGHT_ALIGNMENT);
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

        createProductID();
        imagePanel.add(productID);

        return imagePanel;
    }

    private void addLabelTextField(String labelName, JTextField field, JPanel panel){
        JLabel label = new JLabel(labelName);
        label.setFont(label.getFont().deriveFont(label.getFont().getStyle() | Font.BOLD, 14.0f));

        field.setBorder(new EmptyBorder(0, 0, 0, 0));
        Dimension d = field.getPreferredSize();
        d.width = 200;
        field.setPreferredSize(d);
        field.setBackground(new Color(0xFFFFFF));
        field.setEditable(isStaff);

        panel.add(label);
        panel.add(field);
    }

    private JPanel initAttributePanel(){

        JPanel attributePanel = new JPanel();
        productName = new JTextField(p.getProductName());
        productDescription = new JTextField(p.getDescription());
        productPrice = new JTextField("£" + String.valueOf(p.getRetailPrice()));

        attributePanel.setBackground(new Color(0xFFFFFF));
        attributePanel.setLayout(new BoxLayout(attributePanel, BoxLayout.Y_AXIS));

        attributePanel.setMinimumSize(new Dimension(250, 300));
        attributePanel.setMaximumSize(new Dimension(250, 300));
        attributePanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        addLabelTextField("Product Name: ", productName, attributePanel);
        addLabelTextField("Description: ", productDescription, attributePanel);
        addLabelTextField("Price: ", productPrice, attributePanel);
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
                this.p.setDescription(productDescription.getText());
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



