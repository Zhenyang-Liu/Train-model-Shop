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
import controller.GlobalState;

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
        SwingUtilities.invokeLater(() -> {
            if (!GlobalState.isLoggedIn()) {
                LoginPage loginPage = new LoginPage();
                loginPage.setVisible(true);
            } else {
                // 用户已登录的情况
            }
        });
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        ResourceBundle bundle = ResourceBundle.getBundle("gui.form");
        DefaultComponentFactory compFactory = DefaultComponentFactory.getInstance();
        panel3 = new JPanel();
        panel4 = new JPanel();
        button_account = new JButton();
        button_cart = new JButton();
        separator1 = compFactory.createSeparator("");
        panel6 = new JPanel();
        label1 = new JLabel();
        label2 = new JLabel();
        panel2 = new JPanel();
        label8 = new JLabel();
        textField1 = new JTextField();
        button1 = new JButton();
        separator2 = compFactory.createSeparator("");
        splitPane1 = new JSplitPane();
        panel1 = new JPanel();
        label3 = new JLabel();
        comboBox1 = new JComboBox();
        label4 = new JLabel();
        comboBox2 = new JComboBox();
        label5 = new JLabel();
        comboBox3 = new JComboBox();
        label6 = new JLabel();
        comboBox4 = new JComboBox();
        label7 = new JLabel();
        comboBox5 = new JComboBox();
        panel9 = new JPanel();
        productCardPanel14 = new JPanel();
        label48 = new JLabel();
        label49 = new JLabel();
        purchasePanel14 = new JPanel();
        label50 = new JLabel();
        spinner14 = new JSpinner();
        button28 = new JButton();
        button29 = new JButton();

        //======== this ========
        setPreferredSize(new Dimension(1080, 720));
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panel3 ========
        {
            panel3.setLayout(new BoxLayout(panel3, BoxLayout.Y_AXIS));

            //======== panel4 ========
            {
                panel4.setLayout(new BorderLayout());

                //---- button_account ----
                button_account.setIcon(new FlatSVGIcon(new File("D:\\TrainShop\\src\\main\\images\\person_black_24dp.svg")));
                button_account.setBackground(new Color(0xf2f2f2));
                button_account.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        button_accountMouseClicked(e);
                    }
                });
                panel4.add(button_account, BorderLayout.WEST);

                //---- button_cart ----
                button_cart.setSelectedIcon(null);
                button_cart.setIcon(new FlatSVGIcon(new File("D:\\TrainShop\\src\\main\\images\\shopping_cart_black_24dp.svg")));
                button_cart.setBackground(new Color(0xf2f2f2));
                button_cart.setHorizontalAlignment(SwingConstants.RIGHT);
                panel4.add(button_cart, BorderLayout.EAST);
            }
            panel3.add(panel4);
            panel3.add(separator1);

            //======== panel6 ========
            {
                panel6.setMinimumSize(new Dimension(251, 90));
                panel6.setPreferredSize(new Dimension(251, 80));
                panel6.setLayout(new GridLayout(2, 1, 5, 0));

                //---- label1 ----
                label1.setText(bundle.getString("MainPage.label1.text"));
                label1.setFont(label1.getFont().deriveFont(label1.getFont().getStyle() | Font.BOLD, label1.getFont().getSize() + 26f));
                label1.setForeground(new Color(0x003366));
                label1.setHorizontalAlignment(SwingConstants.CENTER);
                label1.setMaximumSize(null);
                label1.setBorder(new EmptyBorder(10, 0, 0, 0));
                panel6.add(label1);

                //---- label2 ----
                label2.setText(bundle.getString("MainPage.label2.text"));
                label2.setFont(label2.getFont().deriveFont(label2.getFont().getSize() + 5f));
                label2.setHorizontalAlignment(SwingConstants.CENTER);
                label2.setMaximumSize(new Dimension(163, 12));
                label2.setMinimumSize(new Dimension(163, 12));
                label2.setPreferredSize(new Dimension(163, 12));
                panel6.add(label2);
            }
            panel3.add(panel6);

            //======== panel2 ========
            {
                panel2.setMaximumSize(new Dimension(305, 30));
                panel2.setPreferredSize(new Dimension(305, 30));
                panel2.setMinimumSize(new Dimension(305, 30));
                panel2.setLayout(new GridBagLayout());
                ((GridBagLayout)panel2.getLayout()).columnWidths = new int[] {0, 0, 0, 0};
                ((GridBagLayout)panel2.getLayout()).rowHeights = new int[] {0, 0};
                ((GridBagLayout)panel2.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};
                ((GridBagLayout)panel2.getLayout()).rowWeights = new double[] {1.0, 1.0E-4};

                //---- label8 ----
                label8.setText(bundle.getString("MainPage.label8.text"));
                label8.setFont(label8.getFont().deriveFont(label8.getFont().getSize() + 2f));
                panel2.add(label8, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- textField1 ----
                textField1.setPreferredSize(new Dimension(160, 23));
                textField1.setMaximumSize(new Dimension(160, 23));
                textField1.setMinimumSize(new Dimension(160, 23));
                panel2.add(textField1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 5, 0, 5), 0, 0));

                //---- button1 ----
                button1.setText(bundle.getString("MainPage.button1.text"));
                button1.setBackground(new Color(0x204688));
                button1.setForeground(new Color(0xe0e2e8));
                panel2.add(button1, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 5, 0, 0), 0, 0));
            }
            panel3.add(panel2);
        }
        contentPane.add(panel3, BorderLayout.PAGE_START);
        contentPane.add(separator2, BorderLayout.PAGE_END);

        //======== splitPane1 ========
        {

            //======== panel1 ========
            {
                panel1.setAlignmentX(-0.6949153F);
                panel1.setBorder(new EmptyBorder(10, 10, 0, 10));
                panel1.setPreferredSize(null);
                panel1.setFont(panel1.getFont().deriveFont(panel1.getFont().getSize() + 2f));
                panel1.setLayout(new GridBagLayout());
                ((GridBagLayout)panel1.getLayout()).columnWidths = new int[] {0, 0};
                ((GridBagLayout)panel1.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                ((GridBagLayout)panel1.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
                ((GridBagLayout)panel1.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

                //---- label3 ----
                label3.setText(bundle.getString("MainPage.label3.text"));
                label3.setHorizontalTextPosition(SwingConstants.LEFT);
                label3.setHorizontalAlignment(SwingConstants.TRAILING);
                label3.setPreferredSize(null);
                panel1.add(label3, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 5, 0), 0, 0));
                panel1.add(comboBox1, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- label4 ----
                label4.setText(bundle.getString("MainPage.label4.text"));
                panel1.add(label4, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                    new Insets(5, 0, 5, 0), 0, 0));
                panel1.add(comboBox2, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- label5 ----
                label5.setText(bundle.getString("MainPage.label5.text"));
                panel1.add(label5, new GridBagConstraints(0, 4, 1, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                    new Insets(5, 0, 5, 0), 0, 0));
                panel1.add(comboBox3, new GridBagConstraints(0, 5, 1, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- label6 ----
                label6.setText(bundle.getString("MainPage.label6.text"));
                panel1.add(label6, new GridBagConstraints(0, 6, 1, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                    new Insets(5, 0, 5, 0), 0, 0));
                panel1.add(comboBox4, new GridBagConstraints(0, 7, 1, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- label7 ----
                label7.setText(bundle.getString("MainPage.label7.text"));
                panel1.add(label7, new GridBagConstraints(0, 8, 1, 1, 1.0, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                    new Insets(5, 0, 5, 0), 0, 0));
                panel1.add(comboBox5, new GridBagConstraints(0, 9, 1, 1, 1.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));
            }
            splitPane1.setLeftComponent(panel1);

            //======== panel9 ========
            {
                panel9.setMaximumSize(new Dimension(300, 32767));
                panel9.setLayout(new FlowLayout(FlowLayout.LEADING));

                //======== productCardPanel14 ========
                {
                    productCardPanel14.setBorder(new LineBorder(new Color(0x002c7b), 2, true));
                    productCardPanel14.setPreferredSize(new Dimension(260, 280));
                    productCardPanel14.setMaximumSize(new Dimension(230, 240));
                    productCardPanel14.setMinimumSize(new Dimension(230, 240));
                    productCardPanel14.setLayout(new GridBagLayout());
                    ((GridBagLayout)productCardPanel14.getLayout()).columnWidths = new int[] {0, 0};
                    ((GridBagLayout)productCardPanel14.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
                    ((GridBagLayout)productCardPanel14.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
                    ((GridBagLayout)productCardPanel14.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

                    //---- label48 ----
                    label48.setText(bundle.getString("MainPage.label48.text"));
                    label48.setIcon(new ImageIcon("D:\\TrainShop\\src\\main\\images\\tgv.jpeg"));
                    label48.setPreferredSize(new Dimension(216, 120));
                    label48.setAlignmentY(0.0F);
                    label48.setMaximumSize(new Dimension(216, 120));
                    label48.setMinimumSize(new Dimension(216, 120));
                    productCardPanel14.add(label48, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

                    //---- label49 ----
                    label49.setText(bundle.getString("MainPage.label49.text"));
                    label49.setFont(label49.getFont().deriveFont(label49.getFont().getSize() + 4f));
                    label49.setHorizontalAlignment(SwingConstants.CENTER);
                    label49.setBorder(new MatteBorder(3, 0, 3, 0, Color.black));
                    label49.setPreferredSize(new Dimension(220, 30));
                    productCardPanel14.add(label49, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.4,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(1, 0, 0, 0), 0, 0));

                    //======== purchasePanel14 ========
                    {
                        purchasePanel14.setBorder(new EmptyBorder(10, 5, 5, 5));
                        purchasePanel14.setMaximumSize(new Dimension(190, 85));
                        purchasePanel14.setLayout(new GridLayout(2, 2, 20, 10));

                        //---- label50 ----
                        label50.setText(bundle.getString("MainPage.label50.text"));
                        label50.setFont(label50.getFont().deriveFont(label50.getFont().getSize() + 7f));
                        label50.setPreferredSize(new Dimension(80, 25));
                        purchasePanel14.add(label50);

                        //---- spinner14 ----
                        spinner14.setModel(new SpinnerNumberModel(1, 1, null, 1));
                        spinner14.setPreferredSize(new Dimension(50, 23));
                        spinner14.setFont(spinner14.getFont().deriveFont(spinner14.getFont().getSize() + 4f));
                        purchasePanel14.add(spinner14);

                        //---- button28 ----
                        button28.setText(bundle.getString("MainPage.button28.text"));
                        button28.setBackground(new Color(0x55a15a));
                        button28.setForeground(new Color(0xe0e2e8));
                        button28.setPreferredSize(new Dimension(70, 30));
                        purchasePanel14.add(button28);

                        //---- button29 ----
                        button29.setText(bundle.getString("MainPage.button29.text"));
                        button29.setBackground(new Color(0x3da2e7));
                        button29.setForeground(new Color(0xe0e2e8));
                        button29.setPreferredSize(new Dimension(70, 30));
                        purchasePanel14.add(button29);
                    }
                    productCardPanel14.add(purchasePanel14, new GridBagConstraints(0, 2, 1, 1, 0.0, 1.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 10, 0, 10), 0, 0));
                }
                panel9.add(productCardPanel14);
            }
            splitPane1.setRightComponent(panel9);
        }
        contentPane.add(splitPane1, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel panel3;
    private JPanel panel4;
    private JButton button_account;
    private JButton button_cart;
    private JComponent separator1;
    private JPanel panel6;
    private JLabel label1;
    private JLabel label2;
    private JPanel panel2;
    private JLabel label8;
    private JTextField textField1;
    private JButton button1;
    private JComponent separator2;
    private JSplitPane splitPane1;
    private JPanel panel1;
    private JLabel label3;
    private JComboBox comboBox1;
    private JLabel label4;
    private JComboBox comboBox2;
    private JLabel label5;
    private JComboBox comboBox3;
    private JLabel label6;
    private JComboBox comboBox4;
    private JLabel label7;
    private JComboBox comboBox5;
    private JPanel panel9;
    private JPanel productCardPanel14;
    private JLabel label48;
    private JLabel label49;
    private JPanel purchasePanel14;
    private JLabel label50;
    private JSpinner spinner14;
    private JButton button28;
    private JButton button29;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on

    private void customizeComponents() {
        // 在这里添加自定义组件设置代码
        ImageIcon originalIcon = new ImageIcon("D:\\TrainShop\\src\\main\\images\\tgv.jpeg");
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(label48.getWidth(), label48.getHeight(), Image.SCALE_SMOOTH);
        label48.setIcon(new ImageIcon(resizedImage));

        //Image resizedImage18 = originalImage.getScaledInstance(label18.getWidth(), label18.getHeight(), Image.SCALE_SMOOTH);
        //label18.setIcon(new ImageIcon(resizedImage18));

        //Image resizedImage19 = originalImage.getScaledInstance(label19.getWidth(), label19.getHeight(), Image.SCALE_SMOOTH);
        //label19.setIcon(new ImageIcon(resizedImage19));

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
