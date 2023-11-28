package gui;

import model.Address;
import service.AddressService;

import javax.swing.*;
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
    private Address address;
    private boolean isInputValid;

    public AddressDialog(Frame parent, Address address, boolean isEditMode) {
        super(parent, isEditMode ? "Edit Address" : "Add Address", true);
        this.isEditMode = isEditMode;
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
        setLayout(new GridLayout(0, 2));
    
        add(new JLabel("House Number:"));
        houseNumberField = new JTextField(10);
        add(houseNumberField);

        add(new JLabel("Road Name:"));
        roadNameField = new JTextField(20);
        add(roadNameField);

        add(new JLabel("City:"));
        cityField = new JTextField(20);
        add(cityField);

        add(new JLabel("Postcode:"));
        postcodeField = new JTextField(10);
        add(postcodeField);

        actionButton = new JButton(isEditMode ? "Update" : "Add");
        actionButton.addActionListener(isEditMode ? e -> onUpdate() : e -> onAdd());
        add(actionButton);

        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> onCancelButtonClicked());
        add(cancelButton);

        pack();
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
            JOptionPane.showMessageDialog(null, "Invalid Post Code", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            Address address = new Address(getHouseNumber(), getRoadName(), getCity(), getPostcode());
            boolean isAddSuccess = AddressService.addAddress(address);
            if (!isAddSuccess) {
                JOptionPane.showMessageDialog(null, "Add Address failed", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                isInputValid = true;
                dispose();
            }
        }
    }

    private void onUpdate() {
        if (!AddressService.isValidUKPostcode(getPostcode())) {
            JOptionPane.showMessageDialog(null, "Invalid Post Code", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            Address address = new Address(getHouseNumber(), getRoadName(), getCity(), getPostcode());
            boolean isAddSuccess = AddressService.updateAddress(address);
            if (!isAddSuccess) {
                JOptionPane.showMessageDialog(null, "Add Address failed", "Error", JOptionPane.ERROR_MESSAGE);
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
    public String getPostcode() { return postcodeField.getText(); }
    public boolean isInputValid() { return isInputValid; }
}
