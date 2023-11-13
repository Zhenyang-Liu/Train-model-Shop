/*
 * Created by JFormDesigner on Sun Oct 29 23:58:30 GMT 2023
 */

package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import com.formdev.flatlaf.extras.*;
import com.jgoodies.forms.factories.*;

/**
 * @author Zhenyang Liu
 */
public class MainPage extends JFrame {
    public MainPage() {
        initComponents();
        customizeComponents();
    }

    private void createUIComponents() {
        // TODO: add custom component creation code here
    }

    private void button_accountMouseClicked(MouseEvent e) {
        // TODO add your code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        ResourceBundle bundle = ResourceBundle.getBundle("gui.form");
        DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
        label1 = new JLabel();
        button_account = new JButton();
        separator1 = compFactory.createSeparator("");
        button_cart = new JButton();
        label2 = new JLabel();
        comboBox1 = new JComboBox();
        label3 = new JLabel();
        label4 = new JLabel();
        label5 = new JLabel();
        label6 = new JLabel();
        label7 = new JLabel();
        comboBox2 = new JComboBox();
        comboBox3 = new JComboBox();
        comboBox4 = new JComboBox();
        comboBox5 = new JComboBox();
        label8 = new JLabel();
        textField1 = new JTextField();
        button1 = new JButton();
        panel8 = new JPanel();
        label9 = new JLabel();
        label10 = new JLabel();
        spinner1 = new JSpinner();
        button2 = new JButton();
        button5 = new JButton();
        label15 = new JLabel();
        panel9 = new JPanel();
        label11 = new JLabel();
        label12 = new JLabel();
        spinner2 = new JSpinner();
        button6 = new JButton();
        button7 = new JButton();
        label18 = new JLabel();
        panel10 = new JPanel();
        label13 = new JLabel();
        label14 = new JLabel();
        spinner3 = new JSpinner();
        button8 = new JButton();
        button9 = new JButton();
        label19 = new JLabel();
        separator2 = compFactory.createSeparator("");

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(null);

        //---- label1 ----
        label1.setText(bundle.getString("MainPage.label1.text"));
        label1.setFont(label1.getFont().deriveFont(label1.getFont().getStyle() | Font.BOLD, label1.getFont().getSize() + 20f));
        label1.setForeground(new Color(0x003366));
        contentPane.add(label1);
        label1.setBounds(370, 35, 260, 45);

        //---- button_account ----
        button_account.setIcon(new FlatSVGIcon(new File("D:\\TrainShop\\src\\main\\images\\person_black_24dp.svg")));
        button_account.setBackground(new Color(0xf2f2f2));
        button_account.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                button_accountMouseClicked(e);
            }
        });
        contentPane.add(button_account);
        button_account.setBounds(5, 0, 30, 30);
        contentPane.add(separator1);
        separator1.setBounds(0, 30, 1000, 15);

        //---- button_cart ----
        button_cart.setSelectedIcon(null);
        button_cart.setIcon(new FlatSVGIcon(new File("D:\\TrainShop\\src\\main\\images\\shopping_cart_black_24dp.svg")));
        button_cart.setBackground(new Color(0xf2f2f2));
        contentPane.add(button_cart);
        button_cart.setBounds(960, 0, 35, 30);

        //---- label2 ----
        label2.setText(bundle.getString("MainPage.label2.text"));
        label2.setFont(label2.getFont().deriveFont(label2.getFont().getSize() + 2f));
        contentPane.add(label2);
        label2.setBounds(new Rectangle(new Point(415, 80), label2.getPreferredSize()));
        contentPane.add(comboBox1);
        comboBox1.setBounds(new Rectangle(new Point(20, 145), comboBox1.getPreferredSize()));

        //---- label3 ----
        label3.setText(bundle.getString("MainPage.label3.text"));
        contentPane.add(label3);
        label3.setBounds(new Rectangle(new Point(20, 125), label3.getPreferredSize()));

        //---- label4 ----
        label4.setText(bundle.getString("MainPage.label4.text"));
        contentPane.add(label4);
        label4.setBounds(new Rectangle(new Point(20, 185), label4.getPreferredSize()));

        //---- label5 ----
        label5.setText(bundle.getString("MainPage.label5.text"));
        contentPane.add(label5);
        label5.setBounds(new Rectangle(new Point(20, 245), label5.getPreferredSize()));

        //---- label6 ----
        label6.setText(bundle.getString("MainPage.label6.text"));
        contentPane.add(label6);
        label6.setBounds(new Rectangle(new Point(20, 300), label6.getPreferredSize()));

        //---- label7 ----
        label7.setText(bundle.getString("MainPage.label7.text"));
        contentPane.add(label7);
        label7.setBounds(new Rectangle(new Point(20, 355), label7.getPreferredSize()));
        contentPane.add(comboBox2);
        comboBox2.setBounds(new Rectangle(new Point(20, 205), comboBox2.getPreferredSize()));
        contentPane.add(comboBox3);
        comboBox3.setBounds(new Rectangle(new Point(20, 265), comboBox3.getPreferredSize()));
        contentPane.add(comboBox4);
        comboBox4.setBounds(new Rectangle(new Point(20, 320), comboBox4.getPreferredSize()));
        contentPane.add(comboBox5);
        comboBox5.setBounds(new Rectangle(new Point(20, 375), comboBox5.getPreferredSize()));

        //---- label8 ----
        label8.setText(bundle.getString("MainPage.label8.text"));
        contentPane.add(label8);
        label8.setBounds(170, 125, label8.getPreferredSize().width, 15);
        contentPane.add(textField1);
        textField1.setBounds(170, 145, 195, textField1.getPreferredSize().height);

        //---- button1 ----
        button1.setText(bundle.getString("MainPage.button1.text"));
        button1.setBackground(new Color(0x204688));
        button1.setForeground(new Color(0xe0e2e8));
        contentPane.add(button1);
        button1.setBounds(375, 145, 65, button1.getPreferredSize().height);

        //======== panel8 ========
        {
            panel8.setBorder(new LineBorder(new Color(0x002c7b), 2, true));
            panel8.setLayout(null);

            //---- label9 ----
            label9.setText(bundle.getString("MainPage.label9.text"));
            label9.setFont(label9.getFont().deriveFont(label9.getFont().getSize() + 4f));
            label9.setHorizontalAlignment(SwingConstants.CENTER);
            label9.setBorder(new LineBorder(Color.black, 2));
            panel8.add(label9);
            label9.setBounds(0, 105, 190, 30);

            //---- label10 ----
            label10.setText(bundle.getString("MainPage.label10.text"));
            label10.setFont(label10.getFont().deriveFont(label10.getFont().getSize() + 7f));
            panel8.add(label10);
            label10.setBounds(new Rectangle(new Point(20, 140), label10.getPreferredSize()));

            //---- spinner1 ----
            spinner1.setModel(new SpinnerNumberModel(1, 1, null, 1));
            panel8.add(spinner1);
            spinner1.setBounds(105, 140, 65, 25);

            //---- button2 ----
            button2.setFont(button2.getFont().deriveFont(button2.getFont().getSize() - 1f));
            button2.setText(bundle.getString("MainPage.button2.text"));
            button2.setBackground(new Color(0x55a15a));
            button2.setForeground(new Color(0xe0e2e8));
            panel8.add(button2);
            button2.setBounds(10, 175, 75, 35);

            //---- button5 ----
            button5.setFont(button5.getFont().deriveFont(button5.getFont().getSize() - 1f));
            button5.setText(bundle.getString("MainPage.button5.text"));
            button5.setBackground(new Color(0x3da2e7));
            button5.setForeground(new Color(0xe0e2e8));
            panel8.add(button5);
            button5.setBounds(105, 175, 75, 35);

            //---- label15 ----
            label15.setText(bundle.getString("MainPage.label15.text"));
            label15.setIcon(new ImageIcon("D:\\TrainShop\\src\\main\\images\\tgv.jpeg"));
            panel8.add(label15);
            label15.setBounds(3, 3, 184, 101);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < panel8.getComponentCount(); i++) {
                    Rectangle bounds = panel8.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = panel8.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                panel8.setMinimumSize(preferredSize);
                panel8.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(panel8);
        panel8.setBounds(170, 190, 190, 220);

        //======== panel9 ========
        {
            panel9.setBorder(new LineBorder(new Color(0x002c7b), 2, true));
            panel9.setLayout(null);

            //---- label11 ----
            label11.setText(bundle.getString("MainPage.label11.text"));
            label11.setFont(label11.getFont().deriveFont(label11.getFont().getSize() + 4f));
            label11.setHorizontalAlignment(SwingConstants.CENTER);
            label11.setBorder(new LineBorder(Color.black, 2));
            panel9.add(label11);
            label11.setBounds(0, 105, 190, 30);

            //---- label12 ----
            label12.setText(bundle.getString("MainPage.label12.text"));
            label12.setFont(label12.getFont().deriveFont(label12.getFont().getSize() + 7f));
            panel9.add(label12);
            label12.setBounds(new Rectangle(new Point(20, 140), label12.getPreferredSize()));

            //---- spinner2 ----
            spinner2.setModel(new SpinnerNumberModel(1, 1, null, 1));
            panel9.add(spinner2);
            spinner2.setBounds(105, 140, 65, 25);

            //---- button6 ----
            button6.setFont(button6.getFont().deriveFont(button6.getFont().getSize() - 1f));
            button6.setText(bundle.getString("MainPage.button6.text"));
            button6.setBackground(new Color(0x55a15a));
            button6.setForeground(new Color(0xe0e2e8));
            panel9.add(button6);
            button6.setBounds(10, 175, 75, 35);

            //---- button7 ----
            button7.setFont(button7.getFont().deriveFont(button7.getFont().getSize() - 1f));
            button7.setText(bundle.getString("MainPage.button7.text"));
            button7.setBackground(new Color(0x3da2e7));
            button7.setForeground(new Color(0xe0e2e8));
            panel9.add(button7);
            button7.setBounds(105, 175, 75, 35);

            //---- label18 ----
            label18.setText(bundle.getString("MainPage.label18.text"));
            label18.setIcon(new ImageIcon("D:\\TrainShop\\src\\main\\images\\tgv.jpeg"));
            panel9.add(label18);
            label18.setBounds(3, 3, 184, 101);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < panel9.getComponentCount(); i++) {
                    Rectangle bounds = panel9.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = panel9.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                panel9.setMinimumSize(preferredSize);
                panel9.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(panel9);
        panel9.setBounds(380, 190, 190, 220);

        //======== panel10 ========
        {
            panel10.setBorder(new LineBorder(new Color(0x002c7b), 2, true));
            panel10.setLayout(null);

            //---- label13 ----
            label13.setText(bundle.getString("MainPage.label13.text"));
            label13.setFont(label13.getFont().deriveFont(label13.getFont().getSize() + 4f));
            label13.setHorizontalAlignment(SwingConstants.CENTER);
            label13.setBorder(new LineBorder(Color.black, 2));
            panel10.add(label13);
            label13.setBounds(0, 105, 190, 30);

            //---- label14 ----
            label14.setText(bundle.getString("MainPage.label14.text"));
            label14.setFont(label14.getFont().deriveFont(label14.getFont().getSize() + 7f));
            panel10.add(label14);
            label14.setBounds(new Rectangle(new Point(20, 140), label14.getPreferredSize()));

            //---- spinner3 ----
            spinner3.setModel(new SpinnerNumberModel(1, 1, null, 1));
            panel10.add(spinner3);
            spinner3.setBounds(105, 140, 65, 25);

            //---- button8 ----
            button8.setFont(button8.getFont().deriveFont(button8.getFont().getSize() - 1f));
            button8.setText(bundle.getString("MainPage.button8.text"));
            button8.setBackground(new Color(0x55a15a));
            button8.setForeground(new Color(0xe0e2e8));
            panel10.add(button8);
            button8.setBounds(10, 175, 75, 35);

            //---- button9 ----
            button9.setFont(button9.getFont().deriveFont(button9.getFont().getSize() - 1f));
            button9.setText(bundle.getString("MainPage.button9.text"));
            button9.setBackground(new Color(0x3da2e7));
            button9.setForeground(new Color(0xe0e2e8));
            panel10.add(button9);
            button9.setBounds(105, 175, 75, 35);

            //---- label19 ----
            label19.setText(bundle.getString("MainPage.label19.text"));
            label19.setIcon(new ImageIcon("D:\\TrainShop\\src\\main\\images\\tgv.jpeg"));
            panel10.add(label19);
            label19.setBounds(3, 3, 184, 101);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < panel10.getComponentCount(); i++) {
                    Rectangle bounds = panel10.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = panel10.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                panel10.setMinimumSize(preferredSize);
                panel10.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(panel10);
        panel10.setBounds(590, 190, 190, 220);
        contentPane.add(separator2);
        separator2.setBounds(0, 660, 1000, 30);

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
    private JLabel label1;
    private JButton button_account;
    private JComponent separator1;
    private JButton button_cart;
    private JLabel label2;
    private JComboBox comboBox1;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JLabel label6;
    private JLabel label7;
    private JComboBox comboBox2;
    private JComboBox comboBox3;
    private JComboBox comboBox4;
    private JComboBox comboBox5;
    private JLabel label8;
    private JTextField textField1;
    private JButton button1;
    private JPanel panel8;
    private JLabel label9;
    private JLabel label10;
    private JSpinner spinner1;
    private JButton button2;
    private JButton button5;
    private JLabel label15;
    private JPanel panel9;
    private JLabel label11;
    private JLabel label12;
    private JSpinner spinner2;
    private JButton button6;
    private JButton button7;
    private JLabel label18;
    private JPanel panel10;
    private JLabel label13;
    private JLabel label14;
    private JSpinner spinner3;
    private JButton button8;
    private JButton button9;
    private JLabel label19;
    private JComponent separator2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on

    private void customizeComponents() {
        // 在这里添加自定义组件设置代码
        ImageIcon originalIcon = new ImageIcon("D:\\TrainShop\\src\\main\\images\\tgv.jpeg");
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(label15.getWidth(), label15.getHeight(), Image.SCALE_SMOOTH);
        label15.setIcon(new ImageIcon(resizedImage));

        Image resizedImage18 = originalImage.getScaledInstance(label18.getWidth(), label18.getHeight(), Image.SCALE_SMOOTH);
        label18.setIcon(new ImageIcon(resizedImage18));

        Image resizedImage19 = originalImage.getScaledInstance(label19.getWidth(), label19.getHeight(), Image.SCALE_SMOOTH);
        label19.setIcon(new ImageIcon(resizedImage19));

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                MainPage frame = new MainPage();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
