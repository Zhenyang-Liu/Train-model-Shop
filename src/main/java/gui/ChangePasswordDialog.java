package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import model.Login;
import DAO.LoginDAO;
import helper.UserSession;

public class ChangePasswordDialog extends JDialog {
    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JButton actionButton;
    private JButton cancelButton;
    private int userID;

    public ChangePasswordDialog(Frame parent) {
        super(parent, "Change Password", true);
        this.userID = UserSession.getInstance().getCurrentUser().getUserID();
        initializeComponents();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initializeComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        oldPasswordField = new JPasswordField(10);
        newPasswordField = new JPasswordField(10);
        confirmPasswordField = new JPasswordField(10);

        addComponentWithLabel("Old Password:", oldPasswordField, gbc);
        addComponentWithLabel("New Password:", newPasswordField, gbc);
        addComponentWithLabel("Confirm Password:", confirmPasswordField, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        actionButton = createButton("Change");
        actionButton.addActionListener(e -> onChangePassword());
        add(actionButton, gbc);

        gbc.gridx = 1;
        cancelButton = createButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton, gbc);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    private void addComponentWithLabel(String labelText, JComponent component, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText);
        add(label, gbc);
        gbc.gridx++;
        add(component, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(0x204688));
        button.setForeground(Color.WHITE);
        return button;
    }

    private void onChangePassword() {
        String oldPassword = new String(oldPasswordField.getPassword());
        String newPassword = new String(newPasswordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "New password and confirm password do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Login userLogin = LoginDAO.findLoginByUserID(userID);
            if (userLogin.doesPasswordMatch(oldPassword)) {
                userLogin.setPassword(newPassword);
                if (LoginDAO.updateLoginDetails(userLogin)) {
                    JOptionPane.showMessageDialog(this, "Password successfully changed.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update password.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Old password is incorrect.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error occurred while changing password.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
