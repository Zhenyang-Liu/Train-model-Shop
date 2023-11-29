/*
 * Created by JFormDesigner on Wed Nov 29 15:22:59 GMT 2023
 */

package gui;

import java.awt.event.*;
import model.Order;

import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.*;

import static service.OrderService.getUserAllOrder;

/**
 * @author Zhenyang Liu
 */
public class OrderHistory extends JFrame {
    private ArrayList<Order> allOrders = getUserAllOrder();

    public OrderHistory() {
        initComponents();
        if (getUserAllOrder() != null) {
            ordersPanel.removeAll();
            loadOrderCards(allOrders);
        }else {
//            this.dispose();
//            LoginPage loginPage = new LoginPage();
//            loginPage.setVisible(true);
        }

    }

    public void loadOrderCards(ArrayList<Order> orders) {
        if (!orders.isEmpty()){
            for (Order order : orders) {
                JPanel cardPanel = createOrderCardPanel(order);
                addOrderCardToPanel(cardPanel);
            }
        }else {
            ordersPanel.revalidate();
            ordersPanel.repaint();
        }
    }

    private JPanel createOrderCardPanel(Order order) {
        JPanel orderCardPanel = new JPanel();
        orderCardPanel.setLayout(new BoxLayout(orderCardPanel, BoxLayout.X_AXIS));
        orderCardPanel.setBorder(new MatteBorder(3, 3, 3, 3, new Color(0x003762)));
        orderCardPanel.setPreferredSize(new Dimension(590, 120));
        orderCardPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));


        JLabel orderImage = new JLabel(new ImageIcon(getClass().getResource("/images/tgv.jpeg")));
        orderImage.setPreferredSize(new Dimension(150, 120));
        orderImage.setMaximumSize(new Dimension(160, 120));
        orderImage.setMinimumSize(new Dimension(160, 120));
        orderCardPanel.add(orderImage);


        JPanel orderInfoPanel = new JPanel(new GridBagLayout());
        JPanel panel_orderNum = new JPanel();
        panel_orderNum.setLayout(new BoxLayout(panel_orderNum, BoxLayout.X_AXIS));

        //======== panel_orderNum ========
        JLabel label_orderNum = new JLabel("Order Number: ");
        label_orderNum.setMaximumSize(new Dimension(120, 17));
        label_orderNum.setMinimumSize(new Dimension(120, 17));
        label_orderNum.setPreferredSize(new Dimension(120, 17));
        label_orderNum.setFont(label_orderNum.getFont().deriveFont(label_orderNum.getFont().getSize() + 2f));
        panel_orderNum.add(label_orderNum);

        JLabel orderNumLabel = new JLabel(String.valueOf(order.getOrderID()));
        orderNumLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        orderNumLabel.setMaximumSize(new Dimension(130, 17));
        orderNumLabel.setMinimumSize(new Dimension(130, 17));
        orderNumLabel.setPreferredSize(new Dimension(130, 17));
        panel_orderNum.add(orderNumLabel);

        orderInfoPanel.add(panel_orderNum, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.3,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 10, 0, 0), 0, 0));

        //======== panel_cost ========
        JPanel panel_cost = new JPanel();
        panel_cost.setLayout(new BoxLayout(panel_cost, BoxLayout.X_AXIS));

        JLabel label_cost = new JLabel();
        label_cost.setText("Total Cost");
        label_cost.setPreferredSize(new Dimension(100, 17));
        label_cost.setFont(label_cost.getFont().deriveFont(label_cost.getFont().getSize() + 2f));
        label_cost.setMinimumSize(new Dimension(100, 19));
        label_cost.setMaximumSize(new Dimension(100, 19));
        panel_cost.add(label_cost);

        JLabel totalCostLabel = new JLabel();
        totalCostLabel.setText(String.format("\u00A3%.2f", order.getTotalCost()));
        totalCostLabel.setMaximumSize(new Dimension(150, 17));
        totalCostLabel.setMinimumSize(new Dimension(150, 17));
        totalCostLabel.setPreferredSize(new Dimension(150, 17));
        totalCostLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        panel_cost.add(totalCostLabel);

        orderInfoPanel.add(panel_cost, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.3,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 10, 0, 0), 0, 0));

        //======== panel_date ========
        JPanel panel_date = new JPanel();
        panel_date.setMaximumSize(new Dimension(222, 17));
        panel_date.setMinimumSize(new Dimension(222, 17));
        panel_date.setPreferredSize(new Dimension(222, 17));
        panel_date.setLayout(new BoxLayout(panel_date, BoxLayout.X_AXIS));

        JLabel label_date = new JLabel();
        label_date.setText("Update at");
        label_date.setMaximumSize(new Dimension(100, 17));
        label_date.setMinimumSize(new Dimension(100, 17));
        label_date.setFont(label_date.getFont().deriveFont(label_date.getFont().getSize() + 2f));
        label_date.setPreferredSize(new Dimension(100, 19));
        panel_date.add(label_date);

        JLabel timeStampLabel = new JLabel();
        timeStampLabel.setText(order.getFormattedUpdateTime());
        timeStampLabel.setMaximumSize(new Dimension(150, 17));
        timeStampLabel.setMinimumSize(new Dimension(150, 17));
        timeStampLabel.setPreferredSize(new Dimension(150, 17));
        timeStampLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        panel_date.add(timeStampLabel);

        orderInfoPanel.add(panel_date, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.3,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 10, 0, 0), 0, 0));


        orderCardPanel.add(orderInfoPanel);

        JPanel detailPanel = new JPanel(new GridBagLayout());
        ((GridBagLayout)detailPanel.getLayout()).columnWidths = new int[] {0, 0};
        ((GridBagLayout)detailPanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
        ((GridBagLayout)detailPanel.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
        ((GridBagLayout)detailPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

        JLabel statusLabel = new JLabel("Status: ");
        detailPanel.add(statusLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.2,
                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                new Insets(5, 15, 0, 0), 0, 0));

        JLabel order_status = new JLabel();
        order_status.setText(order.getStatusString());
        order_status.setHorizontalAlignment(SwingConstants.CENTER);
        order_status.setFont(order_status.getFont().deriveFont(order_status.getFont().getStyle() | Font.BOLD, order_status.getFont().getSize() + 5f));
        order_status.setPreferredSize(new Dimension(100, 22));
        order_status.setMaximumSize(new Dimension(150, 22));
        order_status.setMinimumSize(new Dimension(150, 22));

        Order.OrderStatus status = order.getStatus();
        Color textColor;
        if (status == Order.OrderStatus.CONFIRMED) {
            textColor = new Color(0x315e90);
        } else if (status == Order.OrderStatus.CANCELLED) {
            textColor = new Color(0x7c2459);
        } else if (status == Order.OrderStatus.FULFILLED) {
            textColor = new Color(0x316357);
        } else if (status == Order.OrderStatus.PENDING) {
            textColor = new Color(0x2c8fb1);
        } else {
            textColor = Color.BLACK;
        }
        order_status.setForeground(textColor);

        detailPanel.add(order_status, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.5,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 5, 0), 0, 0));


        JButton button_detail = new JButton("Details");
        button_detail.addActionListener(e -> showOrderDetails(order));
        button_detail.setMaximumSize(new Dimension(150, 23));
        button_detail.setMinimumSize(new Dimension(150, 23));
        button_detail.setPreferredSize(new Dimension(100, 23));
        button_detail.setBackground(new Color(0x003366));
        button_detail.setForeground(new Color(0xe9e4e3));
        button_detail.setFont(button_detail.getFont().deriveFont(button_detail.getFont().getStyle() | Font.BOLD, button_detail.getFont().getSize() + 1f));
        detailPanel.add(button_detail, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.1,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 5, 0), 0, 0));


        orderCardPanel.add(detailPanel);

        return orderCardPanel;
    }

    private void addOrderCardToPanel(JPanel cardPanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.insets = new Insets(10, 5, 10, 10);


        ordersPanel.add(cardPanel, gbc);
        ordersPanel.revalidate();
        ordersPanel.repaint();
    }

    private void showOrderDetails(Order order) {
        JOptionPane.showMessageDialog(this, "Order details for order ID: " + order.getOrderID());
    }

    private void button_allMouseClicked(MouseEvent e) {
        ordersPanel.removeAll();
        loadOrderCards(allOrders);
    }

    private void button_pendingMouseClicked(MouseEvent e) {
        ordersPanel.removeAll();
        loadOrderCards(filterOrdersByStatus(allOrders, Order.OrderStatus.PENDING));
    }

    private void button_confirmedMouseClicked(MouseEvent e) {
        ordersPanel.removeAll();
        loadOrderCards(filterOrdersByStatus(allOrders, Order.OrderStatus.CONFIRMED));
    }

    private void button_fulfilledMouseClicked(MouseEvent e) {
        ordersPanel.removeAll();
        loadOrderCards(filterOrdersByStatus(allOrders, Order.OrderStatus.FULFILLED));
    }

    private void button_cancelledMouseClicked(MouseEvent e) {
        ordersPanel.removeAll();
        loadOrderCards(filterOrdersByStatus(allOrders, Order.OrderStatus.CANCELLED));
    }

    public ArrayList<Order> filterOrdersByStatus(ArrayList<Order> allOrders, Order.OrderStatus desiredStatus) {
        return allOrders.stream()
                .filter(order -> order.getStatus() == desiredStatus)
                .collect(Collectors.toCollection(ArrayList::new));
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        ResourceBundle bundle = ResourceBundle.getBundle("gui.form");
        titleLabel = new JLabel();
        pageContentPanel = new JPanel();
        typePanel = new JPanel();
        typeSelectionPanel = new JPanel();
        orderLabel = new JLabel();
        button_all = new JButton();
        typeLabel = new JLabel();
        button_pending = new JButton();
        button_confirmed = new JButton();
        button_fulfilled = new JButton();
        button_cancelled = new JButton();
        scrollPane = new JScrollPane();
        ordersPanel = new JPanel();
        orderCardPanel1 = new JPanel();
        orderImage1 = new JLabel();
        orderInfoPanel = new JPanel();
        panel_orderNum = new JPanel();
        label_orderNum = new JLabel();
        orderNumLabel = new JLabel();
        panel_cost = new JPanel();
        label_cost = new JLabel();
        totalCostLabel = new JLabel();
        panel_date = new JPanel();
        label_date = new JLabel();
        timeStampLabel = new JLabel();
        detailPanel = new JPanel();
        label_status = new JLabel();
        order_status = new JLabel();
        button_detail = new JButton();

        //======== this ========
        setMinimumSize(new Dimension(850, 700));
        setPreferredSize(new Dimension(850, 700));
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(5, 5));

        //---- titleLabel ----
        titleLabel.setText("Order History");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(titleLabel.getFont().deriveFont(titleLabel.getFont().getStyle() | Font.BOLD, titleLabel.getFont().getSize() + 14f));
        titleLabel.setForeground(new Color(0x003366));
        contentPane.add(titleLabel, BorderLayout.NORTH);

        //======== pageContentPanel ========
        {
            pageContentPanel.setLayout(new GridBagLayout());
            ((GridBagLayout)pageContentPanel.getLayout()).columnWidths = new int[] {0, 0, 0};
            ((GridBagLayout)pageContentPanel.getLayout()).rowHeights = new int[] {0, 0};
            ((GridBagLayout)pageContentPanel.getLayout()).columnWeights = new double[] {0.0, 0.0, 1.0E-4};
            ((GridBagLayout)pageContentPanel.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

            //======== typePanel ========
            {
                typePanel.setLayout(new GridBagLayout());
                ((GridBagLayout)typePanel.getLayout()).columnWidths = new int[] {0, 0};
                ((GridBagLayout)typePanel.getLayout()).rowHeights = new int[] {0, 0};
                ((GridBagLayout)typePanel.getLayout()).columnWeights = new double[] {0.0, 1.0E-4};
                ((GridBagLayout)typePanel.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

                //======== typeSelectionPanel ========
                {
                    typeSelectionPanel.setLayout(new GridBagLayout());
                    ((GridBagLayout)typeSelectionPanel.getLayout()).columnWidths = new int[] {0, 0};
                    ((GridBagLayout)typeSelectionPanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0};
                    ((GridBagLayout)typeSelectionPanel.getLayout()).columnWeights = new double[] {0.0, 1.0E-4};
                    ((GridBagLayout)typeSelectionPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

                    //---- orderLabel ----
                    orderLabel.setText("Your Orders");
                    orderLabel.setForeground(new Color(0x003366));
                    orderLabel.setMaximumSize(new Dimension(108, 25));
                    orderLabel.setMinimumSize(new Dimension(108, 25));
                    orderLabel.setPreferredSize(new Dimension(108, 25));
                    orderLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    orderLabel.setFont(orderLabel.getFont().deriveFont(orderLabel.getFont().getStyle() | Font.BOLD, orderLabel.getFont().getSize() + 4f));
                    typeSelectionPanel.add(orderLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(5, 0, 10, 0), 0, 0));

                    //---- button_all ----
                    button_all.setText("All Orders");
                    button_all.setBackground(new Color(0x6255bf));
                    button_all.setForeground(new Color(0xe9e4e3));
                    button_all.setFont(button_all.getFont().deriveFont(button_all.getFont().getStyle() | Font.BOLD));
                    button_all.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            button_allMouseClicked(e);
                        }
                    });
                    typeSelectionPanel.add(button_all, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 10, 0), 0, 0));

                    //---- typeLabel ----
                    typeLabel.setText("Order type");
                    typeLabel.setForeground(new Color(0x003366));
                    typeLabel.setMaximumSize(new Dimension(108, 25));
                    typeLabel.setMinimumSize(new Dimension(108, 25));
                    typeLabel.setPreferredSize(new Dimension(108, 25));
                    typeLabel.setHorizontalAlignment(SwingConstants.CENTER);
                    typeLabel.setFont(typeLabel.getFont().deriveFont(typeLabel.getFont().getStyle() | Font.BOLD, typeLabel.getFont().getSize() + 4f));
                    typeSelectionPanel.add(typeLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(5, 0, 10, 0), 0, 0));

                    //---- button_pending ----
                    button_pending.setText("Pending Orders");
                    button_pending.setBackground(new Color(0x2c8fb1));
                    button_pending.setForeground(new Color(0xe9e4e3));
                    button_pending.setFont(button_pending.getFont().deriveFont(button_pending.getFont().getStyle() | Font.BOLD));
                    button_pending.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            button_pendingMouseClicked(e);
                        }
                    });
                    typeSelectionPanel.add(button_pending, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 10, 0), 0, 0));

                    //---- button_confirmed ----
                    button_confirmed.setText("Confirmed Orders");
                    button_confirmed.setBackground(new Color(0x315e90));
                    button_confirmed.setForeground(new Color(0xe9e4e3));
                    button_confirmed.setFont(button_confirmed.getFont().deriveFont(button_confirmed.getFont().getStyle() | Font.BOLD));
                    button_confirmed.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            button_confirmedMouseClicked(e);
                        }
                    });
                    typeSelectionPanel.add(button_confirmed, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 10, 0), 0, 0));

                    //---- button_fulfilled ----
                    button_fulfilled.setText("Fulfilled Orders");
                    button_fulfilled.setBackground(new Color(0x316357));
                    button_fulfilled.setForeground(new Color(0xe9e4e3));
                    button_fulfilled.setFont(button_fulfilled.getFont().deriveFont(button_fulfilled.getFont().getStyle() | Font.BOLD));
                    button_fulfilled.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            button_fulfilledMouseClicked(e);
                        }
                    });
                    typeSelectionPanel.add(button_fulfilled, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 10, 0), 0, 0));

                    //---- button_cancelled ----
                    button_cancelled.setText("Cancelled Orders");
                    button_cancelled.setBackground(new Color(0x7c2459));
                    button_cancelled.setForeground(new Color(0xe9e4e3));
                    button_cancelled.setFont(button_cancelled.getFont().deriveFont(button_cancelled.getFont().getStyle() | Font.BOLD));
                    button_cancelled.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            button_cancelledMouseClicked(e);
                        }
                    });
                    typeSelectionPanel.add(button_cancelled, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
                }
                typePanel.add(typeSelectionPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 10, 0, 10), 0, 0));
            }
            pageContentPanel.add(typePanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 0, 5), 0, 0));

            //======== scrollPane ========
            {
                scrollPane.setMinimumSize(new Dimension(620, 172));
                scrollPane.setPreferredSize(new Dimension(620, 172));
                scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

                //======== ordersPanel ========
                {
                    ordersPanel.setMinimumSize(new Dimension(600, 176));
                    ordersPanel.setLayout(new GridBagLayout());
                    ((GridBagLayout)ordersPanel.getLayout()).columnWidths = new int[] {0, 0};
                    ((GridBagLayout)ordersPanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
                    ((GridBagLayout)ordersPanel.getLayout()).columnWeights = new double[] {0.0, 1.0E-4};
                    ((GridBagLayout)ordersPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

                    //======== orderCardPanel1 ========
                    {
                        orderCardPanel1.setBorder(new MatteBorder(3, 3, 3, 3, new Color(0x003762)));
                        orderCardPanel1.setForeground(new Color(0x003366));
                        orderCardPanel1.setPreferredSize(new Dimension(576, 120));
                        orderCardPanel1.setMaximumSize(new Dimension(2147483647, 100));
                        orderCardPanel1.setLayout(new BoxLayout(orderCardPanel1, BoxLayout.X_AXIS));

                        //---- orderImage1 ----
                        orderImage1.setText(bundle.getString("OrderHistory.orderImage1.text"));
                        orderImage1.setIcon(new ImageIcon(getClass().getResource("/images/tgv.jpeg")));
                        orderImage1.setPreferredSize(new Dimension(150, 120));
                        orderImage1.setMaximumSize(new Dimension(160, 120));
                        orderImage1.setMinimumSize(new Dimension(160, 120));
                        orderCardPanel1.add(orderImage1);

                        //======== orderInfoPanel ========
                        {
                            orderInfoPanel.setLayout(new GridBagLayout());
                            ((GridBagLayout)orderInfoPanel.getLayout()).columnWidths = new int[] {0, 0};
                            ((GridBagLayout)orderInfoPanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
                            ((GridBagLayout)orderInfoPanel.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
                            ((GridBagLayout)orderInfoPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

                            //======== panel_orderNum ========
                            {
                                panel_orderNum.setLayout(new BoxLayout(panel_orderNum, BoxLayout.X_AXIS));

                                //---- label_orderNum ----
                                label_orderNum.setText("Order number");
                                label_orderNum.setMaximumSize(new Dimension(100, 17));
                                label_orderNum.setMinimumSize(new Dimension(100, 17));
                                label_orderNum.setPreferredSize(new Dimension(100, 17));
                                label_orderNum.setFont(label_orderNum.getFont().deriveFont(label_orderNum.getFont().getSize() + 2f));
                                panel_orderNum.add(label_orderNum);

                                //---- orderNumLabel ----
                                orderNumLabel.setText("541176208");
                                orderNumLabel.setHorizontalAlignment(SwingConstants.RIGHT);
                                orderNumLabel.setMaximumSize(new Dimension(150, 17));
                                orderNumLabel.setMinimumSize(new Dimension(150, 17));
                                orderNumLabel.setPreferredSize(new Dimension(150, 17));
                                panel_orderNum.add(orderNumLabel);
                            }
                            orderInfoPanel.add(panel_orderNum, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.3,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 10, 0, 0), 0, 0));

                            //======== panel_cost ========
                            {
                                panel_cost.setLayout(new BoxLayout(panel_cost, BoxLayout.X_AXIS));

                                //---- label_cost ----
                                label_cost.setText("Total Cost");
                                label_cost.setPreferredSize(new Dimension(100, 17));
                                label_cost.setFont(label_cost.getFont().deriveFont(label_cost.getFont().getSize() + 2f));
                                label_cost.setMinimumSize(new Dimension(100, 19));
                                label_cost.setMaximumSize(new Dimension(100, 19));
                                panel_cost.add(label_cost);

                                //---- totalCostLabel ----
                                totalCostLabel.setText("\u00a3139.76");
                                totalCostLabel.setMaximumSize(new Dimension(150, 17));
                                totalCostLabel.setMinimumSize(new Dimension(150, 17));
                                totalCostLabel.setPreferredSize(new Dimension(150, 17));
                                totalCostLabel.setHorizontalAlignment(SwingConstants.RIGHT);
                                panel_cost.add(totalCostLabel);
                            }
                            orderInfoPanel.add(panel_cost, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.3,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 10, 0, 0), 0, 0));

                            //======== panel_date ========
                            {
                                panel_date.setMaximumSize(new Dimension(222, 17));
                                panel_date.setMinimumSize(new Dimension(222, 17));
                                panel_date.setPreferredSize(new Dimension(222, 17));
                                panel_date.setLayout(new BoxLayout(panel_date, BoxLayout.X_AXIS));

                                //---- label_date ----
                                label_date.setText("Update at");
                                label_date.setMaximumSize(new Dimension(100, 17));
                                label_date.setMinimumSize(new Dimension(100, 17));
                                label_date.setFont(label_date.getFont().deriveFont(label_date.getFont().getSize() + 2f));
                                label_date.setPreferredSize(new Dimension(100, 19));
                                panel_date.add(label_date);

                                //---- timeStampLabel ----
                                timeStampLabel.setText("25/11/2023, 03:27");
                                timeStampLabel.setMaximumSize(new Dimension(150, 17));
                                timeStampLabel.setMinimumSize(new Dimension(150, 17));
                                timeStampLabel.setPreferredSize(new Dimension(150, 17));
                                timeStampLabel.setHorizontalAlignment(SwingConstants.RIGHT);
                                panel_date.add(timeStampLabel);
                            }
                            orderInfoPanel.add(panel_date, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.3,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 10, 0, 0), 0, 0));
                        }
                        orderCardPanel1.add(orderInfoPanel);

                        //======== detailPanel ========
                        {
                            detailPanel.setLayout(new GridBagLayout());
                            ((GridBagLayout)detailPanel.getLayout()).columnWidths = new int[] {0, 0};
                            ((GridBagLayout)detailPanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
                            ((GridBagLayout)detailPanel.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
                            ((GridBagLayout)detailPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

                            //---- label_status ----
                            label_status.setText("Status:");
                            detailPanel.add(label_status, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.2,
                                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                                new Insets(5, 15, 0, 0), 0, 0));

                            //---- order_status ----
                            order_status.setText("Confirmed");
                            order_status.setHorizontalAlignment(SwingConstants.CENTER);
                            order_status.setFont(order_status.getFont().deriveFont(order_status.getFont().getStyle() | Font.BOLD, order_status.getFont().getSize() + 5f));
                            order_status.setPreferredSize(new Dimension(100, 22));
                            order_status.setMaximumSize(new Dimension(150, 22));
                            order_status.setMinimumSize(new Dimension(150, 22));
                            order_status.setForeground(new Color(0x009746));
                            detailPanel.add(order_status, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.5,
                                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                                new Insets(0, 0, 5, 0), 0, 0));

                            //---- button_detail ----
                            button_detail.setText("Details");
                            button_detail.setMaximumSize(new Dimension(150, 23));
                            button_detail.setMinimumSize(new Dimension(150, 23));
                            button_detail.setPreferredSize(new Dimension(100, 23));
                            button_detail.setBackground(new Color(0x003366));
                            button_detail.setForeground(new Color(0xe9e4e3));
                            button_detail.setFont(button_detail.getFont().deriveFont(button_detail.getFont().getStyle() | Font.BOLD, button_detail.getFont().getSize() + 1f));
                            detailPanel.add(button_detail, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.1,
                                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                                new Insets(0, 0, 5, 0), 0, 0));
                        }
                        orderCardPanel1.add(detailPanel);
                    }
                    ordersPanel.add(orderCardPanel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 0), 0, 0));
                }
                scrollPane.setViewportView(ordersPanel);
            }
            pageContentPanel.add(scrollPane, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0,
                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                new Insets(5, 0, 0, 0), 0, 0));
        }
        contentPane.add(pageContentPanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JLabel titleLabel;
    private JPanel pageContentPanel;
    private JPanel typePanel;
    private JPanel typeSelectionPanel;
    private JLabel orderLabel;
    private JButton button_all;
    private JLabel typeLabel;
    private JButton button_pending;
    private JButton button_confirmed;
    private JButton button_fulfilled;
    private JButton button_cancelled;
    private JScrollPane scrollPane;
    private JPanel ordersPanel;
    private JPanel orderCardPanel1;
    private JLabel orderImage1;
    private JPanel orderInfoPanel;
    private JPanel panel_orderNum;
    private JLabel label_orderNum;
    private JLabel orderNumLabel;
    private JPanel panel_cost;
    private JLabel label_cost;
    private JLabel totalCostLabel;
    private JPanel panel_date;
    private JLabel label_date;
    private JLabel timeStampLabel;
    private JPanel detailPanel;
    private JLabel label_status;
    private JLabel order_status;
    private JButton button_detail;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}

