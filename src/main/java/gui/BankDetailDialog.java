package gui;

import service.BankDetailService;
import helper.UserSession;

import javax.swing.*;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import java.awt.*;

public class BankDetailDialog extends JDialog {
    private JTextField cardHolderField;
    private JTextField cardNumberField;
    private JTextField expiryDateField;
    private JTextField expiryYearField;
    private JTextField securityCodeField;
    private JButton confirmButton;
    private JButton cancelButton;

    private boolean inputValid = false;

    public BankDetailDialog(Frame parent) {
        super(parent, "Add Bank Detail", true);
        initializeComponents();
        setLocationRelativeTo(parent);
    }
    
    private void initializeComponents() {
        setLayout(new GridLayout(0, 2));
    
        add(new JLabel("Card Holder:"));
        cardHolderField = new JTextField(20);
        add(cardHolderField);
    
        add(new JLabel("Card Number:"));
        cardNumberField = new JTextField(16);
        ((PlainDocument) cardNumberField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        add(cardNumberField);
    
        add(new JLabel("Expiry Date:"));
    
        JPanel expiryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        expiryDateField = new JTextField(2); // month
        expiryYearField = new JTextField(2); // year
        ((PlainDocument) expiryDateField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        ((PlainDocument) expiryYearField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        expiryPanel.add(expiryDateField);
        expiryPanel.add(new JLabel("/"));
        expiryPanel.add(expiryYearField);
        add(expiryPanel);
    
        add(new JLabel("Security Code:"));
        securityCodeField = new JTextField(3);
        ((PlainDocument) securityCodeField.getDocument()).setDocumentFilter(new NumericDocumentFilter());
        add(securityCodeField);
    
        confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> onConfirm());
        add(confirmButton);
    
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton);
    
        pack();
    }
    
    class NumericDocumentFilter extends javax.swing.text.DocumentFilter {
        @Override
        public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string.matches("\\d*")) {
                super.insertString(fb, offset, string, attr);
            }
        }
    
        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text.matches("\\d*")) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }
    

    private void onConfirm() {
        String message = BankDetailService.checkBankDetail(getCardHolder(), getCardNumber(), getExpiryDate(), getSecurityCode());
        
        if (!"Bank details are valid.".equals(message)) {
            JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            int currentUserID = UserSession.getInstance().getCurrentUser().getUserID();
            String insertBankDetail = BankDetailService.addBankDetail(currentUserID, getCardHolder(), getCardNumber(), getExpiryDate(), getSecurityCode());
            if (insertBankDetail != null) {
                JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Operation was successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
                inputValid = true;
                dispose();
            }
        }
    }
    
    public String getCardHolder() { return cardHolderField.getText(); }
    public String getCardNumber() { return cardNumberField.getText(); }
    public String getExpiryDate() { return expiryDateField.getText()+"/"+ expiryYearField.getText(); }
    public String getSecurityCode() { return securityCodeField.getText(); }
    public boolean isInputValid() { return inputValid; }
}
