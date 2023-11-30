/*
 * Created by JFormDesigner on Thu Nov 30 21:56:28 GMT 2023
 */

package gui;

import java.awt.*;
import java.util.*;
import javax.swing.*;

/**
 * @author Zhenyang Liu
 */
public class WelcomePage extends JFrame {
    public WelcomePage() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        ResourceBundle bundle = ResourceBundle.getBundle("gui.form");
        label1 = new JLabel();
        label2 = new JLabel();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(5, 5));

        //---- label1 ----
        label1.setText("WELCOME TO TRAINSHOP");
        contentPane.add(label1, BorderLayout.NORTH);

        //---- label2 ----
        label2.setText(bundle.getString("WelcomePage.label2.text"));
        contentPane.add(label2, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JLabel label1;
    private JLabel label2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
