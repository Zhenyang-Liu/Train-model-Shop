package gui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.*;

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

    /**
     * Instantiate object and create components for GUI
     */
    public AccountPage() {
        initComponents();
    }

    /**
     * Close Window
     */
    private void closeWindow() {
        this.dispose();
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
        addLabelAndInput("Email:", "someemail@gmail.com", DetailsPanel);
        addLabelAndInput("Address:", "Some Address, postcode, england", DetailsPanel);
        addLabelAndInput("First Name:", "User", DetailsPanel);
        addLabelAndInput("Last Name:", "Smith", DetailsPanel);

        // Password section, needs to be different
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        setTextStyle(passwordLabel, false);
        DetailsPanel.add(passwordLabel);
        DetailsPanel.add(passwordField);

        // Bank section, uses another panel
        JPanel BankPanel = new JPanel();
        JLabel bankLabel = new JLabel("Bank Details:");
        JTextField accountNumber = new JTextField("1234567");
        JTextField sortCode = new JTextField("00-00-00");
        JTextField expiryDate = new JTextField("06/25");
        setTextStyle(bankLabel, false);
        BankPanel.setLayout(new GridLayout(1, 3));
        BankPanel.add(accountNumber);
        BankPanel.add(sortCode);
        BankPanel.add(expiryDate);
        DetailsPanel.add(bankLabel);
        DetailsPanel.add(BankPanel);


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
    private void addLabelAndInput(String labelName, String textFieldValue, JPanel panel) {
        JLabel label = new JLabel(labelName);
        JTextField input = new JTextField(textFieldValue);
        setTextStyle(label, false);
        panel.add(label);
        panel.add(input);
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
                System.out.println("clicked");
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
