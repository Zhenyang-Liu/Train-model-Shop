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


    public ProductPage(Product p){
        this.p = p;
        this.isStaff = PermissionService.hasPermission(UserSession.getInstance().getCurrentUser().getUserID(), "UPDATE_PRODUCT");
        
        this.setSize(new Dimension(640, 480));

        initComponents();
    }

    private void initComponents(){
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new GridBagLayout());
        
        productName = new JTextField(p.getProductName());
        productName.setHorizontalAlignment(JTextField.CENTER);
        productName.setEditable( isStaff );
        productName.setVisible(true);

        contentPane.add(productName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
             GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 0), 0, 0));
    }

    public static void main(String[] args){
        UserSession.getInstance().setCurrentUser(UserDAO.findUserByEmail("testey@gmail.com"));
        try{
            ProductPage p = new ProductPage(ProductDAO.getAllProduct().get(0));
            p.setVisible(true);
        }catch(DatabaseException e){
            e.printStackTrace();
        }
    }
}



