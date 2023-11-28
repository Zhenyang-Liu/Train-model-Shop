package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class AccountPage extends JFrame {
    // GUI Variables
    private JPanel WindowPanel;
    private JPanel MainDialoguePanel;
    private JPanel TitlePanel;
    private JPanel DetailsPanel;
    private JSeparator TitleSeparator;
    private JLabel TitleLabel;
    private JLabel EmailLabel;
    private JLabel AddressLabel;
    private JTextField EmailTextField;
    private JTextField AddressTextField;

    /*
     * Instantiate object and create components for GUI
     */
    public AccountPage() {
        initComponents();
    }

    /*
     * Initialises the components for this page 
     */
    public void initComponents() {
        // Create components
        WindowPanel = new JPanel();
        MainDialoguePanel = new JPanel();
        DetailsPanel = new JPanel();
        TitlePanel = new JPanel();
        TitleSeparator = new JSeparator();
        TitleLabel = new JLabel();
        EmailLabel = new JLabel();
        AddressLabel = new JLabel();
        EmailTextField = new JTextField();
        AddressTextField = new JTextField();

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
        
        // Finally add everything to contentPane
        WindowPanel.add(MainDialoguePanel, BorderLayout.CENTER);
        contentPane.add(WindowPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
    }

    /*
     * Create title panel
     */
    private void createTitlePanel() {
        // Create title panel and label
        TitlePanel.setLayout(new BoxLayout(TitlePanel, BoxLayout.Y_AXIS));
        TitleLabel.setText("Your Account");
        setTextStyle(TitleLabel, true);
        TitlePanel.add(TitleLabel);

        // Title Separator
        TitleSeparator.setForeground(new Color(0x7f7272));
        TitleSeparator.setBackground(new Color(0x7f7272));
        TitlePanel.add(TitleSeparator);

        // Add to Main Dialogue 
        MainDialoguePanel.add(TitlePanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 5, 0), 0, 0));
    }

    /*
     * Create details panel
     */
    private void createDetailsPanel() {
        DetailsPanel.setPreferredSize(new Dimension(200, 130));
        DetailsPanel.setMinimumSize(new Dimension(50, 92));
        DetailsPanel.setLayout(new GridLayout(4, 2));

        // Email section
        EmailTextField.setText("example@email.com");
        EmailLabel.setText("Your Email:");
        setTextStyle(EmailLabel, false);
        DetailsPanel.add(EmailLabel);
        DetailsPanel.add(EmailTextField);

        // Email section
        AddressTextField.setText("some address, postcode, england");
        AddressLabel.setText("Your Address:");
        setTextStyle(AddressLabel, false);
        DetailsPanel.add(AddressLabel);
        DetailsPanel.add(AddressTextField);

        // Add to Main Dialogue
        MainDialoguePanel.add(DetailsPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE,
            new Insets(0, 0, 0, 0), 0, 0));
    }

    /*
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
}
