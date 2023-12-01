/*
 * Created by JFormDesigner on Fri Nov 10 16:19:34 GMT 2023
 */

package gui;

import helper.UserSession;
import listeners.ReloadListener;
import listeners.SetRoleButtons;
import model.Login;
import model.User;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import java.sql.SQLException;

import DAO.LoginDAO;
import DAO.UserDAO;

/**
 * @author LIU ZHENYANG
 */

public class LoginPage extends JFrame {
    public LoginPage() {
        initComponents();
        createUIComponents();
    }

    private ReloadListener loginSuccessListener;
    private SetRoleButtons setRoleButtonsListener;
    public void setLoginSuccessListener(ReloadListener listener) {
        this.loginSuccessListener = listener;
    }
    public void setRoleButtonsListener(SetRoleButtons listener) {
        this.setRoleButtonsListener = listener;
    }

    private void button_to_registerPageMouseClicked(MouseEvent e) {
        RegistrationPage registrationPage = new RegistrationPage();
        registrationPage.setAlwaysOnTop(true);
        registrationPage.setVisible(true);

        this.dispose();
    }

    private void backButtonMouseClicked() {
        this.dispose();
    }

    private String LoginButtonMouseClicked(String email, String password) {
        // Try to get and check login details
        try {
            User user = UserDAO.findUserByEmail(email);
            Login userLogin = LoginDAO.findLoginByUserID(user.getUserID());

            if (!UserSession.getInstance().isLoggedIn() && UserDAO.doesUserExist(email)) {
                if (userLogin.doesPasswordMatch(password)) {
                    UserSession.getInstance().setCurrentUser(user);

                    // Reload products for user
                    if (loginSuccessListener != null) {
                        loginSuccessListener.reloadProducts();
                    }
                    if (setRoleButtonsListener != null) {
                        setRoleButtonsListener.setButtonsByRole();
                    }

                    // Return true as everything went successful
                    backButtonMouseClicked();
                    return "OK";
                }
            } else if(!UserDAO.doesUserExist(email)){
                return "email or password not valid";
            }else {
                return "User is already logged in";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Assume something went wrong, return default error message
        // for security we don't want to say if the password or email was wrong
        return "Email or Password was incorrect";
    }

    private void createUIComponents() {
        //---- errorLabel ----
        errorLabel = new JLabel();
        errorLabel.setText("");
        errorLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        errorLabel.setFont(errorLabel.getFont().deriveFont(errorLabel.getFont().getSize() + 1f));
        errorLabel.setIconTextGap(6);
        errorLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        errorLabel.setForeground(new Color(0xb13437));
        LoginTitlePanel.add(errorLabel);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        ResourceBundle bundle = ResourceBundle.getBundle("gui.form");
        LoginDialogPanel = new JPanel();
        LoginContentPanel = new JPanel();
        LoginTitlePanel = new JPanel();
        LoginTitleLabel = new JLabel();
        LoginTitleSeparator = new JSeparator();
        LoginFormPanel = new JPanel();
        LoginLabel_email = new JLabel();
        LoginTextField_email = new JTextField();
        LoginLabel_password = new JLabel();
        LoginPasswordField = new JPasswordField();
        LoginButtonBar = new JPanel();
        button_register = new JButton();
        LoginButton = new JButton();

        //======== this ========
        setPreferredSize(new Dimension(600, 450));
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== LoginDialogPanel ========
        {
            LoginDialogPanel.setBorder(new EmptyBorder(12, 12, 12, 12));
            LoginDialogPanel.setLayout(new BorderLayout());

            //======== LoginContentPanel ========
            {
                LoginContentPanel.setLayout(new GridBagLayout());
                ((GridBagLayout)LoginContentPanel.getLayout()).columnWidths = new int[] {0, 0};
                ((GridBagLayout)LoginContentPanel.getLayout()).rowHeights = new int[] {0, 0, 0};
                ((GridBagLayout)LoginContentPanel.getLayout()).columnWeights = new double[] {0.0, 1.0E-4};
                ((GridBagLayout)LoginContentPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};

                //======== LoginTitlePanel ========
                {
                    LoginTitlePanel.setLayout(new BoxLayout(LoginTitlePanel, BoxLayout.Y_AXIS));

                    //---- LoginTitleLabel ----
                    LoginTitleLabel.setText(bundle.getString("LoginPage.LoginTitleLabel.text"));
                    LoginTitleLabel.setFont(LoginTitleLabel.getFont().deriveFont(LoginTitleLabel.getFont().getStyle() | Font.BOLD, LoginTitleLabel.getFont().getSize() + 11f));
                    LoginTitleLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
                    LoginTitleLabel.setForeground(new Color(0x003366));
                    LoginTitlePanel.add(LoginTitleLabel);

                    //---- LoginTitleSeparator ----
                    LoginTitleSeparator.setForeground(new Color(0x7f7272));
                    LoginTitleSeparator.setBackground(new Color(0x7f7272));
                    LoginTitlePanel.add(LoginTitleSeparator);
                }
                LoginContentPanel.add(LoginTitlePanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //======== LoginFormPanel ========
                {
                    LoginFormPanel.setPreferredSize(new Dimension(200, 130));
                    LoginFormPanel.setMinimumSize(new Dimension(50, 92));
                    LoginFormPanel.setLayout(new GridLayout(4, 1));

                    //---- LoginLabel_email ----
                    LoginLabel_email.setText("Email");
                    LoginLabel_email.setFont(LoginLabel_email.getFont().deriveFont(LoginLabel_email.getFont().getSize() + 4f));
                    LoginLabel_email.setPreferredSize(new Dimension(42, 11));
                    LoginLabel_email.setMinimumSize(new Dimension(42, 16));
                    LoginLabel_email.setMaximumSize(null);
                    LoginFormPanel.add(LoginLabel_email);
                    LoginFormPanel.add(LoginTextField_email);

                    //---- LoginLabel_password ----
                    LoginLabel_password.setText(bundle.getString("LoginPage.LoginLabel_password.text"));
                    LoginLabel_password.setFont(LoginLabel_password.getFont().deriveFont(LoginLabel_password.getFont().getSize() + 4f));
                    LoginFormPanel.add(LoginLabel_password);
                    LoginFormPanel.add(LoginPasswordField);
                }
                LoginContentPanel.add(LoginFormPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            LoginDialogPanel.add(LoginContentPanel, BorderLayout.CENTER);

            //======== LoginButtonBar ========
            {
                LoginButtonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                LoginButtonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)LoginButtonBar.getLayout()).columnWidths = new int[] {0, 0, 85, 80};
                ((GridBagLayout)LoginButtonBar.getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0, 0.0};

                //---- button_register ----
                button_register.setText(bundle.getString("LoginPage.button_register.text"));
                button_register.setBackground(new Color(0x55a15a));
                button_register.setForeground(new Color(0xe9e5e5));
                button_register.setPreferredSize(new Dimension(95, 28));
                button_register.setFont(button_register.getFont().deriveFont(button_register.getFont().getSize() + 1f));
                button_register.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        button_to_registerPageMouseClicked(e);
                    }
                });
                LoginButtonBar.add(button_register, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- LoginButton ----
                LoginButton.setText("Login");
                LoginButton.setBackground(new Color(0x003366));
                LoginButton.setForeground(new Color(0xe9e5e5));
                LoginButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        String error = LoginButtonMouseClicked(LoginTextField_email.getText(), new String(LoginPasswordField.getPassword()));
                        if (!error.equals("OK")) {
                            errorLabel.setText(error);
                        }
                    }
                });
                LoginButtonBar.add(LoginButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            LoginDialogPanel.add(LoginButtonBar, BorderLayout.SOUTH);
        }
        contentPane.add(LoginDialogPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel LoginDialogPanel;
    private JPanel LoginContentPanel;
    private JPanel LoginTitlePanel;
    private JLabel LoginTitleLabel;
    private JSeparator LoginTitleSeparator;
    private JPanel LoginFormPanel;
    private JLabel LoginLabel_email;
    private JTextField LoginTextField_email;
    private JLabel LoginLabel_password;
    private JPasswordField LoginPasswordField;
    private JPanel LoginButtonBar;
    private JButton button_register;
    private JButton LoginButton;
    private JLabel errorLabel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
