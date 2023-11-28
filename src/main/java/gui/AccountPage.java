package gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.border.*;

import DAO.BankDetailDAO;
import DAO.LoginDAO;
import DAO.UserDAO;
import exception.DatabaseException;
import model.BankDetail;
import model.Login;
import model.User;

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
    private JPasswordField passwordInput;
    private HashMap<String, JTextField> inputs;
    private User user;
    private Login userLogin;
    private BankDetail userBankDetails;

    /**
     * Instantiate object and create components for GUI
     */
    public AccountPage(int userID) {
        try {
            user = UserDAO.findUserByID(userID);
            userLogin = LoginDAO.findLoginByUserID(userID);
            userBankDetails = BankDetailDAO.findBankDetail(userID);
        } catch (DatabaseException | SQLException e) {
            System.out.println("User not logged in for account page, closing account page");
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
        // Look over each field, check if there's a change, verify and upload if there is
        String email = inputs.get("email").toString();
        String forename = inputs.get("forename").toString();
        String surname = inputs.get("surname").toString();
        String address = inputs.get("address").toString();
        String accountNumber = inputs.get("accountNumber").toString();
        String sortCode = inputs.get("sortCode").toString();
        String expiryDate = inputs.get("expiryDate").toString();
        String password = new String(passwordInput.getPassword());
        String error = checkInputs(email, forename, surname, address, password, accountNumber, sortCode, expiryDate);

        if (error.equals("OK")) {
        } else {
            errorLabel.setText(error);
        }
    }

    /**
     * Check the input fields for errors
     * @param email inputted email
     * @param forename inputted forename
     * @param surname inputted surname
     * @param address inputted address
     * @param password inputted password
     * @param accountNumber inputted account number
     * @param sortCode inputted sort code
     * @param expiryDate inputted expiry date
     * @return "OK" if there were no errors otherwise an error message
     */
    private String checkInputs(String email, String forename, String surname, String address, String password, String accountNumber, String sortCode, String expiryDate) {
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

        // Address
        if (address.length() < 3)
            return "Address is too short, it must be greater than 2 letters";

        // Account number, sort code, and expiry date
        Pattern accountNumberPattern = Pattern.compile("^[1-9]{4}-[1-9]{4}-[1-9]{4}-[1-9]{4}$");
        Pattern sortCodePattern = Pattern.compile("^[1-9]{2}-[1-9]{2}-[1-9]{2}$");
        Pattern expiryDatePattern = Pattern.compile("^[1-9]{2}/[1-9]{2}$");
        if (!accountNumberPattern.matcher(accountNumber).matches() || accountNumber.length() != 19)
            return "Account number must be in format xxxx-xxxx-xxxx-xxxx";
        if (!sortCodePattern.matcher(sortCode).matches() || sortCode.length() != 8)
            return "Sort code must be in format xx-xx-xx";
        if (!expiryDatePattern.matcher(expiryDate).matches() || expiryDate.length() != 5)
            return "Expiry date must be in format xx/xx";

        // Passwords
        Pattern passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z])(?=.*[@!£$%^&*?]).{8,}");
        if (!passwordPattern.matcher(password).matches())
            return "Password must be longer than 8 letters and have at least one digit and special character";

        return "OK";
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
        inputs.put("address", addLabelAndInput("Address:", user.getAddress(), DetailsPanel));
        inputs.put("forename", addLabelAndInput("First Name:", user.getForename(), DetailsPanel));
        inputs.put("surname", addLabelAndInput("Last Name:", user.getSurname(), DetailsPanel));

        // Password section, needs to be different
        JLabel passwordLabel = new JLabel("Password:");
        passwordInput = new JPasswordField();
        setTextStyle(passwordLabel, false);
        DetailsPanel.add(passwordLabel);
        DetailsPanel.add(passwordInput);
        inputs.put("password", passwordInput);

        // Bank section, uses another panel
        JPanel BankPanel = new JPanel();
        JLabel bankLabel = new JLabel("Bank Details:");
        JTextField accountNumber = new JTextField("xxxxxxxxxxxxxxxx");
        JTextField sortCode = new JTextField("xx-xx-xx");
        JTextField expiryDate = new JTextField(userBankDetails.getExpiryDate());
        setTextStyle(bankLabel, false);
        BankPanel.setLayout(new GridLayout(1, 3));
        BankPanel.add(accountNumber);
        BankPanel.add(sortCode);
        BankPanel.add(expiryDate);
        DetailsPanel.add(bankLabel);
        DetailsPanel.add(BankPanel);
        inputs.put("accountNumber", accountNumber);
        inputs.put("sortCode", sortCode);
        inputs.put("expiryDate", expiryDate);

        // Add error messages
        errorLabel.setForeground(new Color(0xb13437));
        DetailsPanel.add(errorLabel);

        // Add to Main Dialogue
        MainDialoguePanel.add(DetailsPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets(0, 0, 0, 0), 0, 0));
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

    private JButton makeButton(String buttonLabel, int bgColor, int fgColor) {
        JButton button = new JButton(buttonLabel);
        button.setBackground(new Color(bgColor));
        button.setForeground(new Color(fgColor));
        button.setPreferredSize(new Dimension(95, 28));
        button.setFont(button.getFont().deriveFont(button.getFont().getSize() + 1f));
        return button;
    }
}
