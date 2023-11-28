/*
 * Created by JFormDesigner on Fri Nov 10 16:19:34 GMT 2023
 */

package gui;

import helper.UserSession;
import listeners.ReloadListener;
import model.Login;
import model.User;
import service.PermissionService;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import java.sql.SQLException;

import model.Product;
import DAO.AuthenticationDAO;
import DAO.LoginDAO;
import DAO.ProductDAO;
import DAO.UserDAO;
import exception.DatabaseException;

public class ProductPage extends JFrame {

    private Product p;
    private boolean isStaff;

    private JTextField productName;
    private JTextArea productDescription;
    private JComboBox productType;
    private JPanel productArea;
    private JLabel productImage;


    public ProductPage(Product p){
        this.p = p;
        this.isStaff = PermissionService.hasPermission(UserSession.getInstance().getCurrentUser().getUserID(), "UPDATE_PRODUCT");

        this.setSize(new Dimension(640, 480));
        this.setResizable(false);
        this.setTitle((isStaff ? "Editing " : "Viewing ") + p.getProductName());

        initComponents();
    }

    private GridBagConstraints getPanelConstraint(int x, int y, int width, int height){
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = x;
        gbc.gridy = y;

        gbc.gridwidth = width;
        gbc.gridheight = height;

        gbc.fill = GridBagConstraints.NONE;
        gbc.ipady = 0;
        gbc.ipadx = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        return gbc;
    }

    private GridBagConstraints createProductName(){
        productName = new JTextField(p.getProductName());
        productName.setHorizontalAlignment(JTextField.CENTER);
        productName.setEditable( isStaff );
        productName.setPreferredSize(new Dimension(256, 50));
        productName.setFont(productName.getFont().deriveFont(24.0f));
        return getPanelConstraint(2, 1, 1, 1);
    }

    private GridBagConstraints createProductDescription(){
        productDescription = new JTextArea(p.getDescription());
        productDescription.setEditable( isStaff );
        productDescription.setPreferredSize(new Dimension(256, 150));

        return getPanelConstraint(2, 2, 1, 2);
    }

    private GridBagConstraints createProductImage(){

        productImage = new JLabel();
        productImage.setPreferredSize(new Dimension(256, 140));

        ImageIcon originalIcon = new ImageIcon(this.p.getProductImage());
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(256, 140, Image.SCALE_SMOOTH);
        productImage.setIcon(new ImageIcon(resizedImage));
        productImage.setBorder(BorderFactory.createStrokeBorder(new BasicStroke(3.0f)));

        return getPanelConstraint(1, 1, 1, 2);
    }

    private void initComponents(){
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());
        
        productArea = new JPanel(new GridBagLayout());
        productArea.setBackground(Color.WHITE);

        GridBagConstraints imageLayout = createProductImage();
        GridBagConstraints nameLayout = createProductName();
        GridBagConstraints descriptionLayout = createProductDescription();

        productArea.add(productImage, imageLayout);
        productArea.add(productName, nameLayout);
        productArea.add(productDescription, descriptionLayout);

        contentPane.add(productArea, BorderLayout.CENTER);

        pack();
    }

    public static void main(String[] args){
        UserSession.getInstance().setCurrentUser(UserDAO.findUserByEmail("testey@gmail.com"));
        try{
            ProductPage p = new ProductPage(ProductDAO.getAllProduct().get(0));
            p.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            p.setVisible(true);
        }catch(DatabaseException e){
            e.printStackTrace();
        }
    }
}



