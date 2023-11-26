/*
 * Created by JFormDesigner on Sun Nov 26 15:56:31 GMT 2023
 */

package gui;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * @author Zhenyang Liu
 */
public class PendingOrderPage extends JFrame {
    public PendingOrderPage() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        ResourceBundle bundle = ResourceBundle.getBundle("gui.form");
        pendingOrderTitleLabel = new JLabel();
        pendingOrderContentPanel = new JPanel();
        customerInfoPanel = new JPanel();
        addressPanel = new JPanel();
        addressLabel = new JLabel();
        addressTextField = new JTextField();
        addressEditPanel = new JPanel();
        addressAddButton = new JButton();
        addressEditButton = new JButton();
        paymentPnel = new JPanel();
        paymentLabel = new JLabel();
        paymentTextField = new JTextField();
        paymentEditPanel = new JPanel();
        paymentAddButton = new JButton();
        paymentEditButton = new JButton();
        pendingOrderScrollPanel = new JScrollPane();
        pendingOrderItemsPanel = new JPanel();
        pendingOrderCardPanel = new JPanel();
        itemImage1 = new JLabel();
        itemName1 = new JLabel();
        itemPrice1 = new JLabel();
        itemNumLabel1 = new JLabel();
        pendingOrderButtonPanel = new JPanel();
        cancelButton = new JButton();
        confirmButton = new JButton();

        //======== this ========
        setPreferredSize(new Dimension(900, 700));
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //---- pendingOrderTitleLabel ----
        pendingOrderTitleLabel.setText(bundle.getString("pendingOrderPage.pendingOrderTitleLabel.text"));
        pendingOrderTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pendingOrderTitleLabel.setFont(pendingOrderTitleLabel.getFont().deriveFont(pendingOrderTitleLabel.getFont().getStyle() | Font.BOLD, pendingOrderTitleLabel.getFont().getSize() + 13f));
        pendingOrderTitleLabel.setForeground(new Color(0x003366));
        pendingOrderTitleLabel.setBorder(new EmptyBorder(10, 5, 10, 5));
        contentPane.add(pendingOrderTitleLabel, BorderLayout.NORTH);

        //======== pendingOrderContentPanel ========
        {
            pendingOrderContentPanel.setLayout(new GridBagLayout());
            ((GridBagLayout)pendingOrderContentPanel.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0};
            ((GridBagLayout)pendingOrderContentPanel.getLayout()).rowHeights = new int[] {0, 0};
            ((GridBagLayout)pendingOrderContentPanel.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0E-4};
            ((GridBagLayout)pendingOrderContentPanel.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

            //======== customerInfoPanel ========
            {
                customerInfoPanel.setLayout(new GridBagLayout());
                ((GridBagLayout)customerInfoPanel.getLayout()).columnWidths = new int[] {0, 0};
                ((GridBagLayout)customerInfoPanel.getLayout()).rowHeights = new int[] {0, 0, 0};
                ((GridBagLayout)customerInfoPanel.getLayout()).columnWeights = new double[] {0.0, 1.0E-4};
                ((GridBagLayout)customerInfoPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};

                //======== addressPanel ========
                {
                    addressPanel.setMaximumSize(new Dimension(300, 200));
                    addressPanel.setLayout(new GridLayout(3, 1, 5, 5));

                    //---- addressLabel ----
                    addressLabel.setText("Address");
                    addressLabel.setForeground(new Color(0x003366));
                    addressLabel.setFont(addressLabel.getFont().deriveFont(addressLabel.getFont().getStyle() | Font.BOLD, addressLabel.getFont().getSize() + 5f));
                    addressPanel.add(addressLabel);

                    //---- addressTextField ----
                    addressTextField.setEditable(false);
                    addressPanel.add(addressTextField);

                    //======== addressEditPanel ========
                    {
                        addressEditPanel.setLayout(new FlowLayout());

                        //---- addressAddButton ----
                        addressAddButton.setText("Add");
                        addressAddButton.setForeground(new Color(0xe9e4e3));
                        addressAddButton.setBackground(new Color(0x55a15a));
                        addressAddButton.setFont(addressAddButton.getFont().deriveFont(addressAddButton.getFont().getStyle() & ~Font.BOLD));
                        addressEditPanel.add(addressAddButton);

                        //---- addressEditButton ----
                        addressEditButton.setText("Edit");
                        addressEditButton.setBackground(new Color(0x00a5f3));
                        addressEditButton.setForeground(new Color(0xe9e4e3));
                        addressEditPanel.add(addressEditButton);
                    }
                    addressPanel.add(addressEditPanel);
                }
                customerInfoPanel.add(addressPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 5, 0), 0, 0));

                //======== paymentPnel ========
                {
                    paymentPnel.setMaximumSize(new Dimension(300, 200));
                    paymentPnel.setLayout(new GridLayout(3, 1, 5, 5));

                    //---- paymentLabel ----
                    paymentLabel.setText("Payment method");
                    paymentLabel.setForeground(new Color(0x003366));
                    paymentLabel.setFont(paymentLabel.getFont().deriveFont(paymentLabel.getFont().getStyle() | Font.BOLD, paymentLabel.getFont().getSize() + 5f));
                    paymentPnel.add(paymentLabel);

                    //---- paymentTextField ----
                    paymentTextField.setEditable(false);
                    paymentPnel.add(paymentTextField);

                    //======== paymentEditPanel ========
                    {
                        paymentEditPanel.setLayout(new FlowLayout());

                        //---- paymentAddButton ----
                        paymentAddButton.setText("Add");
                        paymentAddButton.setForeground(new Color(0xe9e4e3));
                        paymentAddButton.setBackground(new Color(0x55a15a));
                        paymentAddButton.setFont(paymentAddButton.getFont().deriveFont(paymentAddButton.getFont().getStyle() & ~Font.BOLD));
                        paymentEditPanel.add(paymentAddButton);

                        //---- paymentEditButton ----
                        paymentEditButton.setText("Edit");
                        paymentEditButton.setBackground(new Color(0x00a5f3));
                        paymentEditButton.setForeground(new Color(0xe9e4e3));
                        paymentEditPanel.add(paymentEditButton);
                    }
                    paymentPnel.add(paymentEditPanel);
                }
                customerInfoPanel.add(paymentPnel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(20, 0, 0, 0), 0, 0));
            }
            pendingOrderContentPanel.add(customerInfoPanel, new GridBagConstraints(0, 0, 1, 1, 0.8, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 10, 0, 25), 0, 0));

            //======== pendingOrderScrollPanel ========
            {
                pendingOrderScrollPanel.setPreferredSize(new Dimension(615, 200));
                pendingOrderScrollPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                pendingOrderScrollPanel.setMaximumSize(new Dimension(600, 32767));
                pendingOrderScrollPanel.setMinimumSize(new Dimension(600, 6));

                //======== pendingOrderItemsPanel ========
                {
                    pendingOrderItemsPanel.setMaximumSize(new Dimension(600, 32767));
                    pendingOrderItemsPanel.setMinimumSize(new Dimension(600, 1096));
                    pendingOrderItemsPanel.setLayout(new GridBagLayout());
                    ((GridBagLayout)pendingOrderItemsPanel.getLayout()).columnWidths = new int[] {0, 0};
                    ((GridBagLayout)pendingOrderItemsPanel.getLayout()).rowHeights = new int[] {0, 0};
                    ((GridBagLayout)pendingOrderItemsPanel.getLayout()).columnWeights = new double[] {0.0, 1.0E-4};
                    ((GridBagLayout)pendingOrderItemsPanel.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

                    //======== pendingOrderCardPanel ========
                    {
                        pendingOrderCardPanel.setBorder(new MatteBorder(3, 3, 3, 3, new Color(0x003762)));
                        pendingOrderCardPanel.setForeground(new Color(0x003366));
                        pendingOrderCardPanel.setPreferredSize(new Dimension(600, 120));
                        pendingOrderCardPanel.setMaximumSize(new Dimension(2147483647, 100));
                        pendingOrderCardPanel.setLayout(new GridBagLayout());
                        ((GridBagLayout)pendingOrderCardPanel.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0};
                        ((GridBagLayout)pendingOrderCardPanel.getLayout()).rowHeights = new int[] {0, 0};
                        ((GridBagLayout)pendingOrderCardPanel.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0E-4};
                        ((GridBagLayout)pendingOrderCardPanel.getLayout()).rowWeights = new double[] {1.0, 1.0E-4};

                        //---- itemImage1 ----
                        itemImage1.setText(bundle.getString("pendingOrderPage.itemImage1.text"));
                        itemImage1.setIcon(new ImageIcon(getClass().getResource("/images/tgv.jpeg")));
                        itemImage1.setPreferredSize(new Dimension(150, 120));
                        itemImage1.setMaximumSize(new Dimension(160, 120));
                        itemImage1.setMinimumSize(new Dimension(160, 120));
                        pendingOrderCardPanel.add(itemImage1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 0, 0, 0), 0, 0));

                        //---- itemName1 ----
                        itemName1.setText(bundle.getString("pendingOrderPage.itemName1.text"));
                        itemName1.setHorizontalAlignment(SwingConstants.CENTER);
                        itemName1.setPreferredSize(new Dimension(200, 17));
                        itemName1.setFont(itemName1.getFont().deriveFont(itemName1.getFont().getSize() + 2f));
                        pendingOrderCardPanel.add(itemName1, new GridBagConstraints(1, 0, 1, 1, 0.3, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 0, 0, 0), 0, 0));

                        //---- itemPrice1 ----
                        itemPrice1.setText(bundle.getString("pendingOrderPage.itemPrice1.text"));
                        itemPrice1.setHorizontalAlignment(SwingConstants.CENTER);
                        itemPrice1.setFont(itemPrice1.getFont().deriveFont(itemPrice1.getFont().getSize() + 4f));
                        itemPrice1.setPreferredSize(new Dimension(100, 22));
                        pendingOrderCardPanel.add(itemPrice1, new GridBagConstraints(2, 0, 1, 1, 0.2, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 0, 0, 0), 0, 0));

                        //---- itemNumLabel1 ----
                        itemNumLabel1.setText("1");
                        itemNumLabel1.setHorizontalAlignment(SwingConstants.CENTER);
                        pendingOrderCardPanel.add(itemNumLabel1, new GridBagConstraints(3, 0, 1, 1, 0.2, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                            new Insets(0, 0, 0, 0), 0, 0));
                    }
                    pendingOrderItemsPanel.add(pendingOrderCardPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                        new Insets(0, 0, 0, 0), 0, 0));
                }
                pendingOrderScrollPanel.setViewportView(pendingOrderItemsPanel);
            }
            pendingOrderContentPanel.add(pendingOrderScrollPanel, new GridBagConstraints(2, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 0, 5), 0, 0));
        }
        contentPane.add(pendingOrderContentPanel, BorderLayout.CENTER);

        //======== pendingOrderButtonPanel ========
        {
            pendingOrderButtonPanel.setLayout(new FlowLayout());

            //---- cancelButton ----
            cancelButton.setText("Cancel order");
            cancelButton.setPreferredSize(new Dimension(150, 40));
            cancelButton.setBackground(new Color(0xd54945));
            cancelButton.setForeground(new Color(0xe9e4e3));
            cancelButton.setFont(cancelButton.getFont().deriveFont(cancelButton.getFont().getStyle() | Font.BOLD, cancelButton.getFont().getSize() + 2f));
            pendingOrderButtonPanel.add(cancelButton);

            //---- confirmButton ----
            confirmButton.setText("Confirm order");
            confirmButton.setPreferredSize(new Dimension(150, 40));
            confirmButton.setBackground(new Color(0x55a15a));
            confirmButton.setForeground(new Color(0xe9e4e3));
            confirmButton.setFont(confirmButton.getFont().deriveFont(confirmButton.getFont().getStyle() | Font.BOLD, confirmButton.getFont().getSize() + 2f));
            pendingOrderButtonPanel.add(confirmButton);
        }
        contentPane.add(pendingOrderButtonPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JLabel pendingOrderTitleLabel;
    private JPanel pendingOrderContentPanel;
    private JPanel customerInfoPanel;
    private JPanel addressPanel;
    private JLabel addressLabel;
    private JTextField addressTextField;
    private JPanel addressEditPanel;
    private JButton addressAddButton;
    private JButton addressEditButton;
    private JPanel paymentPnel;
    private JLabel paymentLabel;
    private JTextField paymentTextField;
    private JPanel paymentEditPanel;
    private JButton paymentAddButton;
    private JButton paymentEditButton;
    private JScrollPane pendingOrderScrollPanel;
    private JPanel pendingOrderItemsPanel;
    private JPanel pendingOrderCardPanel;
    private JLabel itemImage1;
    private JLabel itemName1;
    private JLabel itemPrice1;
    private JLabel itemNumLabel1;
    private JPanel pendingOrderButtonPanel;
    private JButton cancelButton;
    private JButton confirmButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
