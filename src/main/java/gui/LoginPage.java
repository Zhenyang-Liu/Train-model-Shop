/*
 * Created by JFormDesigner on Fri Nov 10 16:19:34 GMT 2023
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
public class LoginPage extends JFrame {
    public LoginPage() {
        initComponents();
    }

    private void button_to_registerPageMouseClicked(MouseEvent e) {
        // TODO add your code here
        RegistrationPage registrationPage = new RegistrationPage();
        registrationPage.setVisible(true);

        this.dispose();
    }

    private void backButtonMouseClicked(MouseEvent e) {
        // TODO add your code here
        this.dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        ResourceBundle bundle = ResourceBundle.getBundle("gui.form");
        dialogPane = new JPanel();
        buttonBar = new JPanel();
        button_register = new JButton();
        okButton = new JButton();
        cancelButton = new JButton();
        label1 = new JLabel();
        panel2 = new JPanel();
        panel3 = new JPanel();
        label4 = new JLabel();
        label5 = new JLabel();
        textField1 = new JTextField();
        passwordField1 = new JPasswordField();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 0, 85, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0, 0.0};

                //---- button_register ----
                button_register.setText(bundle.getString("LoginPage.button_register.text"));
                button_register.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        button_to_registerPageMouseClicked(e);
                    }
                });
                buttonBar.add(button_register, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- okButton ----
                okButton.setText("Login");
                okButton.setActionCommand("Login");
                buttonBar.add(okButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
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
                buttonBar.add(cancelButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);

            //---- label1 ----
            label1.setText(bundle.getString("LoginPage.label1.text"));
            label1.setFont(label1.getFont().deriveFont(label1.getFont().getSize() + 11f));
            dialogPane.add(label1, BorderLayout.NORTH);

            //======== panel2 ========
            {
                panel2.setLayout(null);

                //======== panel3 ========
                {
                    panel3.setLayout(null);

                    //---- label4 ----
                    label4.setText("Email");
                    label4.setFont(label4.getFont().deriveFont(label4.getFont().getSize() + 4f));
                    panel3.add(label4);
                    label4.setBounds(15, 15, 160, label4.getPreferredSize().height);

                    //---- label5 ----
                    label5.setText(bundle.getString("LoginPage.label5.text"));
                    label5.setFont(label5.getFont().deriveFont(label5.getFont().getSize() + 4f));
                    panel3.add(label5);
                    label5.setBounds(15, 90, 160, label5.getPreferredSize().height);
                    panel3.add(textField1);
                    textField1.setBounds(15, 45, 160, 25);
                    panel3.add(passwordField1);
                    passwordField1.setBounds(15, 120, 160, 25);

                    {
                        // compute preferred size
                        Dimension preferredSize = new Dimension();
                        for(int i = 0; i < panel3.getComponentCount(); i++) {
                            Rectangle bounds = panel3.getComponent(i).getBounds();
                            preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                            preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                        }
                        Insets insets = panel3.getInsets();
                        preferredSize.width += insets.right;
                        preferredSize.height += insets.bottom;
                        panel3.setMinimumSize(preferredSize);
                        panel3.setPreferredSize(preferredSize);
                    }
                }
                panel2.add(panel3);
                panel3.setBounds(125, 25, 200, 190);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for(int i = 0; i < panel2.getComponentCount(); i++) {
                        Rectangle bounds = panel2.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                    }
                    Insets insets = panel2.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    panel2.setMinimumSize(preferredSize);
                    panel2.setPreferredSize(preferredSize);
                }
            }
            dialogPane.add(panel2, BorderLayout.CENTER);
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
    private JPanel buttonBar;
    private JButton button_register;
    private JButton okButton;
    private JButton cancelButton;
    private JLabel label1;
    private JPanel panel2;
    private JPanel panel3;
    private JLabel label4;
    private JLabel label5;
    private JTextField textField1;
    private JPasswordField passwordField1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
