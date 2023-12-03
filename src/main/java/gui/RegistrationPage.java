/*
 * Created by JFormDesigner on Thu Nov 09 16:18:30 GMT 2023
 */

package gui;

import DAO.AddressDAO;
import DAO.AuthenticationDAO;
import DAO.LoginDAO;
import DAO.UserDAO;
import exception.DatabaseException;
import helper.UserSession;
import helper.Logging;
import model.Address;
import model.Login;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * @author LIU ZHENYANG
 * @author JULIAN JONES
 */
public class RegistrationPage extends JFrame {
    public RegistrationPage() {
        initComponents();
        createUIComponents();
    }

    /**
     * Button handler to go back to loginPage
     * @param e the mouse button event, this isn't used
     */
    private void backButtonMouseClicked(MouseEvent e) {
        this.dispose();
        LoginPage loginPage = new LoginPage();
        loginPage.setAlwaysOnTop(true);
        loginPage.setVisible(true);
    }

    /**
     * Button handler for submit button to validate user and attempt to create a new user
     * @param email the email of the user
     * @param forename the first name of the user
     * @param surname the last name of the user
     * @param address the address of the user
     * @param password the password of the login account, different to user
     */
    private String submitButtonClicked(String email, String forename, String surname, String address, String password) {
        // Insert user
        if (!UserDAO.doesUserExist(email)) {
            User newUser = new User(email, forename, surname);
            Logging.getLogger().info("User has logged in (id = " + newUser.getUserID() + ")");
            
            // Check to see if address has been added
            if (addressDialog == null || addressDialog.isInputValid()) {
                return "Address has not been set.";
            }
            
            // Attempt to set default role
            UserDAO.insertUser(newUser);
            try {
                AuthenticationDAO.setDefaultRole(newUser.getUserID());
                UserSession.getInstance().setCurrentUser(newUser);
            } catch (DatabaseException e) {
                return "Error adding users role";
            }

            // Create login, and login user
            Login newLogin = new Login(newUser.getUserID());
            newLogin.setPassword(password);
            boolean loginSuccess = LoginDAO.insertLoginDetails(newLogin);
            if (loginSuccess) {
                try {
                    AddressDAO.insertAddress(newUser.getUserID(), addressDialog.getNewAddress());
                } catch (DatabaseException e) {
                    Logging.getLogger().warning("Could not add address into database\nStackTrace: " + e.getMessage());
                }
                UserSession.getInstance().clear();
                return "OK";
            }
            UserSession.getInstance().clear();
            return "Error creating user login, they may already exist!";
        }

        // User was not created because the email already exists
        return "Email already exists!";
    }

    /**
     * Checks the inputs to make sure they're valid before adding to database
     * @param email inputted email
     * @param forename inputted forename (first name)
     * @param surname inputted surname (last name)
     * @param password inputted password
     * @param passwordValidate inputted password confirm, will be checked against password
     * @return a string for the error message or "OK" if everything is good
     */
    public static String checkInputs(String email, String forename, String surname, String password, String passwordValidate) {
        // Emails
        Pattern emailPattern = Pattern.compile("^[A-z0-9._%+-]+@+[A-z0-9_%+-]+.[A-z_-]{2,}$");
        if (!emailPattern.matcher(email).matches())
            return "Email field(s) are not valid emails";

        // Forename
        if (forename.length() < 3)
            return "First name is too short, it must be greater than 2 letters";
        if (forename.length() > 16)
            return "First name is too long, it must be less than 16 letters";

        // Surname
        if (surname.length() < 3)
            return "Last name is too short, it must be greater than 2 letters";
        if (surname.length() > 16)
            return "Last name is too long, it must be less than 16 letters";

        // Passwords
        Pattern passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z])(?=.*[@!£$%^&*?]).{8,}");
        if (!password.equals(passwordValidate))
            return "Passwords do not match";
        if (!passwordPattern.matcher(password).matches())
            return "Password must be longer than 8 letters and have at least one digit and special character";

        // Everything passed!
        return "OK";
    }

    /**
     * Creates UI Components that are custom (not from JFormDesigner or need extra tweaking)
     */
    private void createUIComponents() {
        //---- errorLabel ----
        errorLabel.setText("");
        errorLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        errorLabel.setFont(errorLabel.getFont().deriveFont(errorLabel.getFont().getSize() + 1f));
        errorLabel.setIconTextGap(6);
        errorLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        errorLabel.setForeground(new Color(0xb13437));
        RegisterTitlePanel.add(errorLabel);
    }

    /**
     * Closes the registration page
     */
    private void closeRegistration() {
        this.dispose();
    }

    /**
     * Open edit address dialogue box
     */
    private void onEditAddress() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        addressDialog = new AddressDialog(parentFrame, new Address(), false, true);
        addressDialog.setAlwaysOnTop(true);
        addressDialog.setVisible(true);
    }

    private void RegisterSubmitButtonMouseClicked(MouseEvent e) {
        String fieldsError = checkInputs(emailTextField.getText(), firstNameTextField.getText(), lastNameTextField.getText(), new String(passwordField_create.getPassword()), new String(passwordField_confirm.getPassword()));
        if (fieldsError == "OK") {
            String userCreationError = submitButtonClicked(emailTextField.getText(), firstNameTextField.getText(), lastNameTextField.getText(), "", new String(passwordField_confirm.getPassword()));
            if (userCreationError == "OK") {
                closeRegistration();
            } else
                errorLabel.setText(userCreationError);
        }
        else {
            Logging.getLogger().warning("Inputs were not valid: " + fieldsError);
            errorLabel.setText(fieldsError);
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        ResourceBundle bundle = ResourceBundle.getBundle("gui.form");
        RegisterDialogPane = new JPanel();
        RegisterTitlePanel = new JPanel();
        lRegisterTitleLabel = new JLabel();
        RegisterTitleSeparator = new JSeparator();
        errorLabel = new JLabel();
        RegisterButtonBar = new JPanel();
        RegisterSubmitButton = new JButton();
        RegisterBacklButton = new JButton();
        RegisterContentPanel = new JPanel();
        RegisterFormPanel = new JPanel();
        label_Email = new JLabel();
        emailTextField = new JTextField();
        label_FN = new JLabel();
        firstNameTextField = new JTextField();
        label_LN = new JLabel();
        lastNameTextField = new JTextField();
        label_createPassword = new JLabel();
        passwordField_create = new JPasswordField();
        label_confirmPassword = new JLabel();
        passwordField_confirm = new JPasswordField();

        //======== this ========
        setPreferredSize(new Dimension(600, 450));
        setMinimumSize(new Dimension(600, 450));
        setMaximumSize(new Dimension(600, 450));
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== RegisterDialogPane ========
        {
            RegisterDialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            RegisterDialogPane.setLayout(new BorderLayout());

            //======== RegisterTitlePanel ========
            {
                RegisterTitlePanel.setLayout(new BoxLayout(RegisterTitlePanel, BoxLayout.Y_AXIS));

                //---- lRegisterTitleLabel ----
                lRegisterTitleLabel.setText(bundle.getString("RegistrationPage.lRegisterTitleLabel.text"));
                lRegisterTitleLabel.setHorizontalTextPosition(SwingConstants.CENTER);
                lRegisterTitleLabel.setFont(lRegisterTitleLabel.getFont().deriveFont(lRegisterTitleLabel.getFont().getStyle() | Font.BOLD, lRegisterTitleLabel.getFont().getSize() + 11f));
                lRegisterTitleLabel.setIconTextGap(6);
                lRegisterTitleLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
                lRegisterTitleLabel.setForeground(new Color(0x003366));
                RegisterTitlePanel.add(lRegisterTitleLabel);

                //---- RegisterTitleSeparator ----
                RegisterTitleSeparator.setForeground(new Color(0x7f7272));
                RegisterTitleSeparator.setBackground(new Color(0x7f7272));
                RegisterTitlePanel.add(RegisterTitleSeparator);

                //---- errorLabel ----
                errorLabel.setHorizontalTextPosition(SwingConstants.CENTER);
                errorLabel.setFont(errorLabel.getFont().deriveFont(errorLabel.getFont().getStyle() | Font.BOLD, errorLabel.getFont().getSize() + 1f));
                errorLabel.setIconTextGap(6);
                errorLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
                errorLabel.setForeground(Color.red);
                RegisterTitlePanel.add(errorLabel);
            }
            RegisterDialogPane.add(RegisterTitlePanel, BorderLayout.PAGE_START);

            //======== RegisterButtonBar ========
            {
                RegisterButtonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                RegisterButtonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)RegisterButtonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
                ((GridBagLayout)RegisterButtonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

                //---- RegisterSubmitButton ----
                RegisterSubmitButton.setText("Submit");
                RegisterSubmitButton.setBackground(new Color(0x003366));
                RegisterSubmitButton.setForeground(new Color(0xe9e5e5));
                RegisterSubmitButton.setPreferredSize(new Dimension(78, 28));
                RegisterSubmitButton.setFont(RegisterSubmitButton.getFont().deriveFont(RegisterSubmitButton.getFont().getSize() + 1f));
                RegisterSubmitButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        RegisterSubmitButtonMouseClicked(e);
                    }
                });
                RegisterButtonBar.add(RegisterSubmitButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- RegisterBacklButton ----
                RegisterBacklButton.setText("Back");
                RegisterBacklButton.setFont(RegisterBacklButton.getFont().deriveFont(RegisterBacklButton.getFont().getSize() + 1f));
                RegisterBacklButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        backButtonMouseClicked(e);
                    }
                });
                RegisterButtonBar.add(RegisterBacklButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            RegisterDialogPane.add(RegisterButtonBar, BorderLayout.PAGE_END);

            //======== RegisterContentPanel ========
            {
                RegisterContentPanel.setLayout(new GridBagLayout());
                ((GridBagLayout)RegisterContentPanel.getLayout()).columnWidths = new int[] {0, 0};
                ((GridBagLayout)RegisterContentPanel.getLayout()).rowHeights = new int[] {0, 0};
                ((GridBagLayout)RegisterContentPanel.getLayout()).columnWeights = new double[] {0.0, 1.0E-4};
                ((GridBagLayout)RegisterContentPanel.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

                //======== RegisterFormPanel ========
                {
                    RegisterFormPanel.setPreferredSize(new Dimension(200, 275));
                    RegisterFormPanel.setLayout(new GridLayout(12, 1, 4, 4));

                    //---- label_Email ----
                    label_Email.setText(bundle.getString("RegistrationPage.label_Email.text"));
                    label_Email.setFont(label_Email.getFont().deriveFont(label_Email.getFont().getSize() + 3f));
                    RegisterFormPanel.add(label_Email);

                    //---- emailTextField ----
                    emailTextField.setMinimumSize(new Dimension(80, 23));
                    emailTextField.setPreferredSize(new Dimension(80, 23));
                    RegisterFormPanel.add(emailTextField);

                    //---- label_FN ----
                    label_FN.setText(bundle.getString("RegistrationPage.label_FN.text"));
                    label_FN.setFont(label_FN.getFont().deriveFont(label_FN.getFont().getSize() + 3f));
                    RegisterFormPanel.add(label_FN);
                    RegisterFormPanel.add(firstNameTextField);

                    //---- label_LN ----
                    label_LN.setText(bundle.getString("RegistrationPage.label_LN.text"));
                    label_LN.setFont(label_LN.getFont().deriveFont(label_LN.getFont().getSize() + 3f));
                    RegisterFormPanel.add(label_LN);
                    RegisterFormPanel.add(lastNameTextField);

                    //---- label_createPassword ----
                    label_createPassword.setText(bundle.getString("RegistrationPage.label_createPassword.text"));
                    label_createPassword.setFont(label_createPassword.getFont().deriveFont(label_createPassword.getFont().getSize() + 3f));
                    RegisterFormPanel.add(label_createPassword);
                    RegisterFormPanel.add(passwordField_create);

                    //---- label_confirmPassword ----
                    label_confirmPassword.setText(bundle.getString("RegistrationPage.label_confirmPassword.text"));
                    label_confirmPassword.setFont(label_confirmPassword.getFont().deriveFont(label_confirmPassword.getFont().getSize() + 3f));
                    RegisterFormPanel.add(label_confirmPassword);
                    RegisterFormPanel.add(passwordField_confirm);

                    // Address Section
                    JLabel addressLabel = new JLabel("Address:");
                    JButton addressButton = new JButton("Add Address");
                    addressButton.addActionListener(e -> onEditAddress());
                    addressLabel.setFont(addressLabel.getFont().deriveFont(addressLabel.getFont().getSize() + 3f));
                    addressButton.setText("Edit Address");
                    RegisterFormPanel.add(addressLabel);
                    RegisterFormPanel.add(addressButton);
                }
                RegisterContentPanel.add(RegisterFormPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                    GridBagConstraints.CENTER, GridBagConstraints.NONE,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            RegisterDialogPane.add(RegisterContentPanel, BorderLayout.CENTER);
        }
        contentPane.add(RegisterDialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel RegisterDialogPane;
    private JPanel RegisterTitlePanel;
    private JLabel lRegisterTitleLabel;
    private JSeparator RegisterTitleSeparator;
    private JLabel errorLabel;
    private JPanel RegisterButtonBar;
    private JButton RegisterSubmitButton;
    private JButton RegisterBacklButton;
    private JPanel RegisterContentPanel;
    private JPanel RegisterFormPanel;
    private JLabel label_Email;
    private JTextField emailTextField;
    private JLabel label_FN;
    private JTextField firstNameTextField;
    private JLabel label_LN;
    private JTextField lastNameTextField;
    private JLabel label_createPassword;
    private JPasswordField passwordField_create;
    private JLabel label_confirmPassword;
    private JPasswordField passwordField_confirm;
    private AddressDialog addressDialog;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
