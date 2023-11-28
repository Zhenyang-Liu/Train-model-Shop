package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.TitlePaneLayout;

public class AccountPage extends JFrame {
    // GUI Variables
    private JPanel MainDialoguePanel;
    private JPanel TitleContentPanel;
    private JPanel DetailsPanel;
    private JLabel TitleLabel;
    private JSeparator TitleSeparator;

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
        MainDialoguePanel = new JPanel();
        TitleContentPanel = new JPanel();
        DetailsPanel = new JPanel();
        TitleLabel = new JLabel();
        TitleSeparator = new JSeparator();

        // Set size and layout
        setPreferredSize(new Dimension(600, 450));
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // Add dialogue panel 
        MainDialoguePanel.setBorder(new EmptyBorder(12, 12, 12, 12));
        MainDialoguePanel.setLayout(new BorderLayout());

        // Create panels and add to mainDialogue
        createTitlePanel();
        createDetailsPanel();
        MainDialoguePanel.add(TitleContentPanel);
        MainDialoguePanel.add(DetailsPanel);

        // Finally add everything to contentPane
        contentPane.add(MainDialoguePanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
    }

    /*
     * Create title panel, put in its own function to make main more readable
     */
    private void createTitlePanel() {
        // Create title panel and label
        TitleContentPanel.setLayout(new BoxLayout(TitleContentPanel, BoxLayout.Y_AXIS));
        TitleLabel.setText("Your Account");
        TitleLabel.setFont(TitleLabel.getFont().deriveFont(TitleLabel.getFont().getStyle() | Font.BOLD, TitleLabel.getFont().getSize() + 11f));
        TitleLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        TitleLabel.setForeground(new Color(0x003366));
        TitleContentPanel.add(TitleLabel);

        // Title Separator
        TitleSeparator.setForeground(new Color(0x7f7272));
        TitleSeparator.setBackground(new Color(0x7f7272));
        TitleContentPanel.add(TitleSeparator);
    }

    /*
     * Create details panel, put in its own function to make main more readable
     */
    private void createDetailsPanel() {
        DetailsPanel.setLayout(new BoxLayout(DetailsPanel, BoxLayout.Y_AXIS));
    }
}
