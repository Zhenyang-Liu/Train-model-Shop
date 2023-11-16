/*
 * Created by JFormDesigner on Thu Nov 09 16:18:30 GMT 2023
 */

package gui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * @author LIU ZHENYANG
 */
public class RegistrationPage extends JFrame {
    public RegistrationPage() {
        initComponents();
    }

    private void backButtonMouseClicked(MouseEvent e) {
        // TODO add your code here
        this.dispose();
        LoginPage loginPage = new LoginPage();
        loginPage.setVisible(true);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        ResourceBundle bundle = ResourceBundle.getBundle("gui.form");
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        panel1 = new JPanel();
        label5 = new JLabel();
        textField1 = new JTextField();
        label2 = new JLabel();
        passwordField1 = new JPasswordField();
        label3 = new JLabel();
        passwordField2 = new JPasswordField();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();
        label4 = new JLabel();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(null);

                //======== panel1 ========
                {
                    panel1.setLayout(null);

                    //---- label5 ----
                    label5.setText(bundle.getString("RegistrationPage.label5.text"));
                    label5.setFont(label5.getFont().deriveFont(label5.getFont().getSize() + 4f));
                    panel1.add(label5);
                    label5.setBounds(30, 20, 45, label5.getPreferredSize().height);
                    panel1.add(textField1);
                    textField1.setBounds(30, 45, 195, 25);

                    //---- label2 ----
                    label2.setText(bundle.getString("RegistrationPage.label2.text"));
                    label2.setFont(label2.getFont().deriveFont(label2.getFont().getSize() + 3f));
                    panel1.add(label2);
                    label2.setBounds(30, 85, 155, label2.getPreferredSize().height);
                    panel1.add(passwordField1);
                    passwordField1.setBounds(30, 110, 195, 25);

                    //---- label3 ----
                    label3.setText(bundle.getString("RegistrationPage.label3.text"));
                    label3.setFont(label3.getFont().deriveFont(label3.getFont().getSize() + 3f));
                    panel1.add(label3);
                    label3.setBounds(30, 150, 165, 20);
                    panel1.add(passwordField2);
                    passwordField2.setBounds(30, 175, 195, 25);

                    {
                        // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panel1.getComponentCount(); i++) {
                            Rectangle bounds = panel1.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panel1.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panel1.setMinimumSize(preferredSize);
                        panel1.setPreferredSize(preferredSize);
                    }
                }
                contentPanel.add(panel1);
                panel1.setBounds(95, 10, 280, 220);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for(int i = 0; i < contentPanel.getComponentCount(); i++) {
                        Rectangle bounds = contentPanel.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                    }
                    Insets insets = contentPanel.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    contentPanel.setMinimumSize(preferredSize);
                    contentPanel.setPreferredSize(preferredSize);
                }
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

                //---- okButton ----
                okButton.setText("Submit");
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- cancelButton ----
                cancelButton.setText("Back");
                cancelButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        backButtonMouseClicked(e);
                    }
                });
                buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.PAGE_END);

            //---- label4 ----
            label4.setText(bundle.getString("RegistrationPage.label4.text"));
            label4.setHorizontalTextPosition(SwingConstants.CENTER);
            label4.setFont(label4.getFont().deriveFont(label4.getFont().getSize() + 11f));
            label4.setIconTextGap(6);
            dialogPane.add(label4, BorderLayout.PAGE_START);
        }
        contentPane.add(dialogPane);
        dialogPane.setBounds(0, 0, 478, 329);

        {
            // compute preferred size
            Dimension preferredSize = new Dimension();
            for(int i = 0; i < contentPane.getComponentCount(); i++) {
                Rectangle bounds = contentPane.getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = contentPane.getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            contentPane.setMinimumSize(preferredSize);
            contentPane.setPreferredSize(preferredSize);
        }
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JPanel panel1;
    private JLabel label5;
    private JTextField textField1;
    private JLabel label2;
    private JPasswordField passwordField1;
    private JLabel label3;
    private JPasswordField passwordField2;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    private JLabel label4;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
