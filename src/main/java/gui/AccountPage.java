package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class AccountPage extends JFrame {
    // GUI Variables
    private JPanel MainDialoguePanel;
    private JPanel TitleContentPanel;
    private JLabel TitleLabel;

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
        TitleLabel = new JLabel();

        // Set size and layout
        setPreferredSize(new Dimension(600, 450));
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // Add dialogue panel 
        MainDialoguePanel.setBorder(new EmptyBorder(12, 12, 12, 12));
        MainDialoguePanel.setLayout(new BorderLayout());

        // Create title panel and label
        TitleContentPanel.setLayout(new BoxLayout(TitleContentPanel, BoxLayout.Y_AXIS));
        TitleLabel.setText("Account");
        TitleLabel.setFont(TitleLabel.getFont().deriveFont(TitleLabel.getFont().getStyle() | Font.BOLD, TitleLabel.getFont().getSize() + 11f));
        TitleLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
        TitleLabel.setForeground(new Color(0x003366));
        TitleContentPanel.add(TitleLabel);

        // Finally add everything to contentPane
        contentPane.add(MainDialoguePanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
    }
}
