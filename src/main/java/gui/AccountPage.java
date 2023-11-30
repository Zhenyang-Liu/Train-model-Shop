package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import DAO.LoginDAO;
import DAO.UserDAO;
import exception.DatabaseException;
import helper.Logging;
import model.BankDetail;
import model.Login;
import model.User;
import service.AddressService;
import service.BankDetailService;

/**
 * Written by: Julian Jones
 */
public class AccountPage extends JFrame {
    // GUI Variables
    private JPanel WindowPanel;
    private JPanel MainDialoguePanel;
    private JPanel TitlePanel;
    private JPanel DetailsPanel;
    private JPanel ButtonPanel;
    private JLabel errorLabel;
    private JLabel successLabel;
    private JPasswordField passwordInput;
    private HashMap<String, JTextField> inputs;
    private User user;
    private Login userLogin;
    private final String DEFAULT_PASSWORD = "CorrectPassword1?";

    /**
     * Instantiate object and create components for GUI
     */
    public AccountPage(int userID) {
        Logging.getLogger().info("Creating account page for user " + userID);
        try {
            user = UserDAO.findUserByID(userID);
            userLogin = LoginDAO.findLoginByUserID(userID);
        } catch (DatabaseException | SQLException e) {
            Logging.getLogger().warning("User not logged in for account page, closing account page");
            LoginPage loginPage = new LoginPage();
            loginPage.setVisible(true);
            this.dispose();
        }

        initComponents();
    }

    /**
     * Close Window
     */
    private void closeWindow() {
        this.dispose();
    }

    /**
     * Updates the values of the user, login, and bank tables (if needing changed)
     */
    private void updateDetails() {
        // Default password, account number, sort code
        String defaultPassword = "CorrectPassword1?";

        // Get inputs
        String email = inputs.get("email").getText();
        String forename = inputs.get("forename").getText();
        String surname = inputs.get("surname").getText();
        String password = new String(passwordInput.getPassword());

        if (password.equals("")) 
            password = defaultPassword;

        // Validate inputs
        String error = RegistrationPage.checkInputs(email, forename, surname, password, password);
        if (!error.equals("OK")) {
            errorLabel.setText(error);
        } else {
            // Update values in objects
            user.setEmail(email);
            user.setForename(forename);
            user.setSurname(surname);
            if (!password.equals(defaultPassword))
                userLogin.setPassword(password);

            // Update values in DB
            boolean updatedUser = UserDAO.updateUser(user);
            boolean updatePassword = false;
            boolean updatedCard = false;
            if (!password.equals(DEFAULT_PASSWORD)) {
                try {
                    LoginDAO.updateLoginDetails(userLogin);
                    updatePassword = true;
                } catch (SQLException e) {
                    errorLabel.setText("Could not update password.");
                }
            } else {
                updatePassword = true;
            }

            // Update Messages
            if (updatedUser && updatePassword && updatedCard) {
                errorLabel.setText("");
                successLabel.setText("Successfully updated user details!");
            } else {
                errorLabel.setText("Unknown error updating user details... Please report to admin");
            }
        }
    }

    /**
     * Initialises the components for this page 
     */
    public void initComponents() {
        // Create components
        WindowPanel = new JPanel();
        MainDialoguePanel = new JPanel();
        DetailsPanel = new JPanel();
        TitlePanel = new JPanel();
        ButtonPanel = new JPanel();
        errorLabel = new JLabel();
        successLabel = new JLabel();
        inputs = new HashMap<>();

        // Set size and layout
        setPreferredSize(new Dimension(600, 450));
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // Add dialogue panel 
        WindowPanel.setBorder(new EmptyBorder(12, 12, 12, 12));
        WindowPanel.setLayout(new BorderLayout());
        MainDialoguePanel.setLayout(new GridBagLayout());
        ((GridBagLayout)MainDialoguePanel.getLayout()).columnWidths = new int[] {0, 0};
        ((GridBagLayout)MainDialoguePanel.getLayout()).rowHeights = new int[] {0, 0, 0};
        ((GridBagLayout)MainDialoguePanel.getLayout()).columnWeights = new double[] {0.0, 1.0E-4};
        ((GridBagLayout)MainDialoguePanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};
        
        // Create panels and add to mainDialogue
        createTitlePanel();
        createDetailsPanel();
        createButtonPanel();
        
        // Finally add everything to contentPane
        WindowPanel.add(MainDialoguePanel, BorderLayout.CENTER);
        contentPane.add(WindowPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
    }

    /**
     * Create title panel
     */
    private void createTitlePanel() {
        // Create title panel and label
        TitlePanel.setLayout(new BoxLayout(TitlePanel, BoxLayout.Y_AXIS));
        JLabel TitleLabel = new JLabel("Your Account");
        setTextStyle(TitleLabel, true);
        TitlePanel.add(TitleLabel);

        // Title Separator
        JSeparator TitleSeparator = new JSeparator();
        TitleSeparator.setForeground(new Color(0x7f7272));
        TitleSeparator.setBackground(new Color(0x7f7272));
        TitlePanel.add(TitleSeparator);

        // Add error and success messages
        errorLabel.setForeground(new Color(0xb13437));
        successLabel.setForeground(new Color(0x32a852));
        TitlePanel.add(errorLabel);
        TitlePanel.add(successLabel);

        // Add to Main Dialogue 
        MainDialoguePanel.add(TitlePanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 5, 0), 0, 0));
    }

    /**
     * Create details panel
     */
    private void createDetailsPanel() {
        DetailsPanel.setPreferredSize(new Dimension(400, 150));
        DetailsPanel.setMinimumSize(new Dimension(50, 92));
        DetailsPanel.setLayout(new GridLayout(0, 2));
        
        // Add all labels and inputs (that are generic)
        inputs.put("email", addLabelAndInput("Email:", user.getEmail(), DetailsPanel));
        inputs.put("forename", addLabelAndInput("First Name:", user.getForename(), DetailsPanel));
        inputs.put("surname", addLabelAndInput("Last Name:", user.getSurname(), DetailsPanel));

        // Password section, needs to be different
        JLabel passwordLabel = new JLabel("Password:");
        JButton changePasswordButton = new JButton("Change Password");
        changePasswordButton.addActionListener(e -> onChangePassword());
        DetailsPanel.add(passwordLabel );
        DetailsPanel.add(changePasswordButton);

        // Bank Detail Section
        JLabel bankDetailLabel = new JLabel("Bank Detail:");
        JButton bankDetailButton = new JButton("Add Bank Detail");
        bankDetailButton.addActionListener(e -> onEditBankDetails());
        setTextStyle(bankDetailLabel, false);

        if (BankDetailService.findBankDetail() != null) {
            bankDetailButton.setText("Edit Bank Detail");
        }
        DetailsPanel.add(bankDetailLabel);
        DetailsPanel.add(bankDetailButton);

        // Address Section
        JLabel addressLabel = new JLabel("Address:");
        JButton addressButton = new JButton("Add Address");
        addressButton.addActionListener(e -> onEditAddress());
        setTextStyle(addressLabel, false);
        if (!AddressService.isAddressEmpty(AddressService.getAddressByUser())){
            addressButton.setText("Edit Address");
        }
        DetailsPanel.add(addressLabel);
        DetailsPanel.add(addressButton);

        // Add to Main Dialogue
        MainDialoguePanel.add(DetailsPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets(0, 0, 0, 0), 0, 0));
    }

    /**
     * Open change password dialogue box
     */
    private void onChangePassword() {
        ChangePasswordDialog dialog = new ChangePasswordDialog(this);
        dialog.setVisible(true);
    }
 
    /**
     * Open edit bank details dialogue box
     */
    private void onEditBankDetails() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        boolean isEdit = BankDetailService.findBankDetail() != null;
        BankDetailDialog bankDetailDialog = new BankDetailDialog(parentFrame,BankDetailService.findBankDetail(),isEdit);
        bankDetailDialog.setVisible(true);
    }

    /**
     * Open edit address dialogue box
     */
    private void onEditAddress() {
        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        boolean isEdit = !AddressService.isAddressEmpty(AddressService.getAddressByUser());
        AddressDialog addressDialog = new AddressDialog(parentFrame, AddressService.getAddressByUser(),isEdit);
        addressDialog.setVisible(true);
    }

    /**
     * Create generic input with label for a panel
     * @param labelName The label name that will be displayed 
     * @param textFieldValue The default text field value (can be empty)
     * @param panel The panel to add these to
     */
    private JTextField addLabelAndInput(String labelName, String textFieldValue, JPanel panel) {
        JLabel label = new JLabel(labelName);
        JTextField input = new JTextField(textFieldValue);
        setTextStyle(label, false);
        panel.add(label);
        panel.add(input);
        return input;
    }

    /**
     * Set default font, colour, and border
     */
    private void setTextStyle(JLabel textLabel, boolean header) {
        if (header) {
            textLabel.setFont(textLabel.getFont().deriveFont(textLabel.getFont().getStyle() | Font.BOLD, textLabel.getFont().getSize() + 11f));
            textLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
            textLabel.setForeground(new Color(0x003366));
        } else {
            textLabel.setFont(textLabel.getFont().deriveFont(textLabel.getFont().getSize() + 4f));
            textLabel.setPreferredSize(new Dimension(42, 11));
            textLabel.setMinimumSize(new Dimension(42, 16));
            textLabel.setMaximumSize(null);
        }
    }

    /**
     * Create button panel
     */
    private void createButtonPanel() {
        // Create button panel
        ButtonPanel.setBorder(new EmptyBorder(12, 0, 0, 0));
        ButtonPanel.setLayout(new GridBagLayout());
        ((GridBagLayout)ButtonPanel.getLayout()).columnWidths = new int[] {0, 0, 85, 80};
        ((GridBagLayout)ButtonPanel.getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0, 0.0};

        // Make back button
        JButton closeButton = makeButton("Close", 0xcf3a30, 0xe9e5e5);
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                closeWindow();
            }
        });
        ButtonPanel.add(closeButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 5), 0, 0));


        // Make update button
        JButton updateButton = makeButton("Update", 0x55a15a, 0xe9e5e5);
        updateButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateDetails();
            }
        });
        ButtonPanel.add(updateButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));

        // Add to window panel at bottom of page
        WindowPanel.add(ButtonPanel, BorderLayout.SOUTH);
    }

    /**
     * Create a default button
     * @param buttonLabel the text on the button
     * @param bgColor the button background colour
     * @param fgColor the button foreground colour
     * @return
     */
    private JButton makeButton(String buttonLabel, int bgColor, int fgColor) {
        JButton button = new JButton(buttonLabel);
        button.setBackground(new Color(bgColor));
        button.setForeground(new Color(fgColor));
        button.setPreferredSize(new Dimension(95, 28));
        button.setFont(button.getFont().deriveFont(button.getFont().getSize() + 1f));
        return button;
    }

}
