package gui;

import model.Address;
import service.AddressService;

import javax.swing.*;

import helper.Logging;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AddressDialog extends JDialog {
    private JTextField houseNumberField;
    private JTextField roadNameField;
    private JTextField cityField;
    private JTextField postcodeField;
    private JButton actionButton;
    private JButton cancelButton;

    private boolean isEditMode;
    private boolean isNewUser;
    private Address address;
    private Address newAddress;
    private boolean isInputValid;

    public AddressDialog(Frame parent, Address address, boolean isEditMode, boolean isNewUser) {
        super(parent, isEditMode ? "Edit Address" : "Add Address", true);
        this.isEditMode = isEditMode;
        this.isNewUser = isNewUser;
        this.address = address;
        this.isInputValid = false;
        initializeComponents();
        setLocationRelativeTo(parent);
        if (isEditMode) {
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
        gbc.fill = GridBagConstraints.HORIZONTAL;

        houseNumberField = new JTextField(10);
        roadNameField = new JTextField(20);
        cityField = new JTextField(20);
        postcodeField = new JTextField(10);

        addComponentWithLabel("House Number:", houseNumberField, gbc);
        addComponentWithLabel("Road Name:", roadNameField, gbc);
        addComponentWithLabel("City:", cityField, gbc);
        addComponentWithLabel("Postcode:", postcodeField, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        actionButton = createButton(isEditMode ? "Update" : "Add");
        actionButton.addActionListener(isEditMode ? e -> onUpdate() : e -> onAdd());
        add(actionButton, gbc);

        gbc.gridx = 1;
        cancelButton = createButton("Cancel");
        cancelButton.addActionListener(e -> onCancelButtonClicked());
        add(cancelButton, gbc);

        pack();
    }

    private void addComponentWithLabel(String labelText, JComponent component, GridBagConstraints gbc) {
        JLabel label = createLabel(labelText);
        add(label, gbc);
        gbc.gridx++;
        add(component, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(0x003366));
        label.setFont(new Font("Arial", Font.BOLD, 12));
        return label;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(text.equals("Update") ? new Color(34, 139, 34) : new Color(0x204688));
        return button;
    }

    private void populateFields() {
        if (address != null) {
            houseNumberField.setText(address.getHouseNumber());
            roadNameField.setText(address.getRoadName());
            cityField.setText(address.getCity());
            postcodeField.setText(address.getPostcode());
        }
    }

    private void onAdd() {
        if (!AddressService.isValidUKPostcode(getPostcode())) {
            Logging.getLogger().warning("Invalid postcode supplied");
            JOptionPane.showMessageDialog(this, "Invalid Post Code", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            newAddress = new Address(getHouseNumber(), getRoadName(), getCity(), getPostcode());
            if (!isNewUser) {
                boolean isAddSuccess = AddressService.addAddress(newAddress);
                if (!isAddSuccess) {
                    Logging.getLogger().warning("Could not add address to database");
                    JOptionPane.showMessageDialog(this, "Add Address failed", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    isInputValid = true;
                    dispose();
                }
            } else {
                dispose();
            }
        }
    }

    private void onUpdate() {
        if (!AddressService.isValidUKPostcode(getPostcode())) {
            JOptionPane.showMessageDialog(this, "Invalid Post Code, must be in form XX XXX", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            Address address = new Address(getHouseNumber(), getRoadName(), getCity(), getPostcode());
            boolean isAddSuccess = AddressService.updateAddress(address);
            if (!isAddSuccess) {
                JOptionPane.showMessageDialog(this, "Add Address failed", "Error", JOptionPane.ERROR_MESSAGE);
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

    public String getHouseNumber() { return houseNumberField.getText(); }
    public String getRoadName() { return roadNameField.getText(); }
    public String getCity() { return cityField.getText(); }
    public String getPostcode() { return postcodeField.getText().toUpperCase(); }
    public Address getNewAddress() { return newAddress; }
    public boolean isInputValid() { return isInputValid; }
}
