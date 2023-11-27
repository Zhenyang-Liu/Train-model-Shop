package gui;

import service.BankDetailService;
import helper.UserSession;
import model.BankDetail;

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
    private JButton actionButton;
    private JButton cancelButton;

    private BankDetail bankDetail;
    private boolean isEditMode;
    private boolean isInputValid; // flag for whether do a valid action

    /**
     * Constructs a BankDetailDialog.
     *
     * @param parent The parent frame to which this dialog is attached.
     * @param bankDetail The BankDetail object to be edited; if null, a new bank detail is being added.
     * @param isEditMode True if editing existing bank details, false if adding new details.
     */
    public BankDetailDialog(Frame parent, BankDetail bankDetail, boolean isEditMode) {
        super(parent, isEditMode ? "Edit Bank Detail" : "Add Bank Detail", true);
        this.bankDetail = bankDetail;
        this.isEditMode = isEditMode;
        this.isInputValid = false;
        initializeComponents();
        if (isEditMode) {
            populateFields();
        }
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
    
        actionButton = new JButton(isEditMode ? "Update" : "Add");
        actionButton.addActionListener(isEditMode ? e -> onUpdate() : e -> onAdd());
        add(actionButton);

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
    
    /**
     * Handles the "Add" action.
     *
     * Validates the user input and adds the new bank details to the database. Closes the dialog if the operation is successful.
     */
    private void onAdd() {
        String message = BankDetailService.checkBankDetail(getCardHolder(), getCardNumber(), getExpiryDate(), getSecurityCode());
        
        if (!"Bank details are valid.".equals(message)) {
            JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            int currentUserID = UserSession.getInstance().getCurrentUser().getUserID();
            String insertBankDetail = BankDetailService.addBankDetail(currentUserID, getCardHolder(), getCardNumber(), getExpiryDate(), getSecurityCode());
            if (insertBankDetail != "success") {
                JOptionPane.showMessageDialog(this, insertBankDetail, "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                isInputValid = true;
                dispose();
            }
        }
    }

    /**
     * Handles the "Update" action.
     *
     * Validates the user input and updates the existing bank details in the database. Closes the dialog if the operation is successful.
     */
    private void onUpdate() {
        String message = BankDetailService.checkBankDetail(getCardHolder(), getCardNumber(), getExpiryDate(), getSecurityCode());
        
        if (!"Bank details are valid.".equals(message)) {
            JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            int currentUserID = UserSession.getInstance().getCurrentUser().getUserID();
            String updateBankDetail = BankDetailService.updateBankDetail(currentUserID, getCardHolder(), getCardNumber(), getExpiryDate(), getSecurityCode());
            if (updateBankDetail != "success") {
                JOptionPane.showMessageDialog(this, updateBankDetail, "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                isInputValid = true;
                dispose();
            }
        }
    }

    private void populateFields() {
        if (bankDetail != null) {
            cardHolderField.setText(bankDetail.getCardHolderName());
            cardNumberField.setText(bankDetail.getCardNumber());
    
            String[] expiry = bankDetail.getExpiryDate().split("/");
            if (expiry.length == 2) {
                expiryDateField.setText(expiry[0]);
                expiryYearField.setText(expiry[1]); 
            }
    
            securityCodeField.setText(bankDetail.getSecurityCode());
        }
    }
    
    public String getCardHolder() { return cardHolderField.getText(); }
    public String getCardNumber() { return cardNumberField.getText(); }
    public String getExpiryDate() { return expiryDateField.getText()+"/"+ expiryYearField.getText(); }
    public String getSecurityCode() { return securityCodeField.getText(); }
    public boolean isInputValid() { return isInputValid; }
}
