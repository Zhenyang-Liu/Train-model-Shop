package gui;

import model.Order;
import service.OrderService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class CancelOrderDialog extends JDialog {
    private JTextArea cancelReasonField;
    private JButton actionButton;
    private JButton cancelButton;

    private boolean isEditMode;
    private Order order;
    private boolean isInputValid;

    public CancelOrderDialog(Frame parent, Order order, boolean isEditMode) {
        super(parent, isEditMode ? "Cancel Reason" : "See Reason", true);
        this.isEditMode = isEditMode;
        this.order = order;
        this.isInputValid = false;
        initializeComponents();
        setLocationRelativeTo(parent);
        if (!isEditMode) {
            populateFields();
        }
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancelButtonClicked();
            }
        });
    }
    
    private void initializeComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
    
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
    
        add(new JLabel("Cancel Reason:"), gbc);
    
        cancelReasonField = new JTextArea();
        cancelReasonField.setRows(3);
        cancelReasonField.setColumns(30);
        cancelReasonField.setLineWrap(true);
        gbc.gridy++;
        add(cancelReasonField, gbc);
    
        gbc.gridwidth = 1;
        gbc.gridy++;
    
        if (isEditMode) {
            actionButton = new JButton("Cancel");
            actionButton.addActionListener(e -> onAdd());
            actionButton.setFont(new Font("Arial", Font.BOLD, 12));
            actionButton.setForeground(Color.WHITE);
            actionButton.setBackground(Color.RED);
            gbc.gridx = 0;
            add(actionButton, gbc);
        }
    
        cancelButton = new JButton("Return");
        cancelButton.addActionListener(e -> onCancelButtonClicked());
        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBackground(new Color(34, 139, 34));
        gbc.gridx = 1;
        add(cancelButton, gbc);
    
        pack();
    }    

    private void populateFields() {
        if (order.getReason() != null) {
            cancelReasonField.setText(order.getReason());
            cancelReasonField.setEditable(false);
        }
    }

    private void onAdd() {
        if (cancelReasonField.getText() == null || cancelReasonField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a reason", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            boolean isCancelSuccess = OrderService.cancelOrder(order, getCancelReason());
            if (!isCancelSuccess) {
                JOptionPane.showMessageDialog(null, "Add reason failed", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                isInputValid = true;
                dispose();
            }
        }
    }

    private void onCancelButtonClicked() {
        isInputValid = false;
        dispose();
    }

    public String getCancelReason() { return cancelReasonField.getText(); }
    public boolean isInputValid() { return isInputValid; }
}
