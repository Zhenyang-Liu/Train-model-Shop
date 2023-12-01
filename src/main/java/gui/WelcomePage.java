/*
 * Created by JFormDesigner on Thu Nov 30 21:56:28 GMT 2023
 */

package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.*;
import javax.swing.*;
import javax.swing.plaf.BorderUIResource;

import DAO.DatabaseConnectionHandler;
import DAO.UserDAO;
import com.formdev.flatlaf.extras.*;
import helper.ImageUtils;
import helper.Logging;
import helper.UserSession;
import model.User;

/**
 * @author Zhenyang Liu
 */
public class WelcomePage extends JFrame {
    private static WelcomePage instance;
    public WelcomePage() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        initComponents();
        createUIComponents();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onWindowClosing();
            }
        });
    }

    private void onWindowClosing() {
        // disconnect DB
        DatabaseConnectionHandler.shutdown();
        Logging.Close();
        // Stop program
        System.exit(0);
    }

    public static WelcomePage getInstance() {
        if (instance == null) {
            instance = new WelcomePage();
        }
        return instance;
    }
    private void createUIComponents() {
        URL imageUrl = getClass().getResource("/images/TrainshopLogo.png");
        ImageIcon originalIcon = new ImageIcon(imageUrl);
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(960, 540, Image.SCALE_SMOOTH);
        label_logo.setIcon(new ImageIcon(resizedImage));
    }

    private void button_loginMouseClicked() {
        LoginPage loginPage = new LoginPage();
        loginPage.setLoginSuccessListener(() -> {
            this.setVisible(false);

            MainPage mainPage = new MainPage();
            mainPage.setVisible(true);

            loginPage.dispose();
        });

        loginPage.setVisible(true);
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        titlePanel = new JPanel();
        label_welcome = new JLabel();
        logoPanel = new JPanel();
        label_logo = new JLabel();
        buttonPanel = new JPanel();
        button_login = new JButton();

        //======== this ========
        setPreferredSize(new Dimension(1200, 900));
        setMinimumSize(new Dimension(1200, 900));
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== titlePanel ========
        {
            titlePanel.setLayout(new GridBagLayout());
            ((GridBagLayout)titlePanel.getLayout()).columnWidths = new int[] {0, 0};
            ((GridBagLayout)titlePanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
            ((GridBagLayout)titlePanel.getLayout()).columnWeights = new double[] {0.0, 1.0E-4};
            ((GridBagLayout)titlePanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

            //---- label_welcome ----
            label_welcome.setText("WELCOME TO TRAINSHOP");
            label_welcome.setForeground(new Color(0x003366));
            label_welcome.setFont(label_welcome.getFont().deriveFont(Font.BOLD|Font.ITALIC, label_welcome.getFont().getSize() + 30f));
            label_welcome.setPreferredSize(new Dimension(580, 42));
            label_welcome.setMaximumSize(new Dimension(460, 42));
            label_welcome.setMinimumSize(new Dimension(460, 42));
            titlePanel.add(label_welcome, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.5,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                new Insets(10, 0, 5, 0), 0, 0));

            //======== logoPanel ========
            {
                logoPanel.setLayout(new FlowLayout());

                //---- label_logo ----
                label_logo.setIcon(null);
                label_logo.setPreferredSize(new Dimension(960, 540));
                label_logo.setHorizontalAlignment(SwingConstants.CENTER);
                label_logo.setDisabledIcon(null);
                logoPanel.add(label_logo);
            }
            titlePanel.add(logoPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 0), 0, 0));

            //======== buttonPanel ========
            {
                buttonPanel.setLayout(new FlowLayout());

                //---- button_login ----
                button_login.setText("START SHOPPING");
                button_login.setBackground(new Color(0x003366));
                button_login.setForeground(new Color(0xe9e4e3));
                button_login.setPreferredSize(new Dimension(200, 50));
                button_login.setFont(button_login.getFont().deriveFont(button_login.getFont().getStyle() | Font.BOLD, button_login.getFont().getSize() + 5f));
                button_login.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        button_loginMouseClicked();
                    }
                });
                buttonPanel.add(button_login);
            }
            titlePanel.add(buttonPanel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.5,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        }
        contentPane.add(titlePanel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel titlePanel;
    private JLabel label_welcome;
    private JPanel logoPanel;
    private JLabel label_logo;
    private JPanel buttonPanel;
    private JButton button_login;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on

    public static void main(String[] args) {
        WelcomePage frame = new WelcomePage();
        frame.setVisible(true);
    }
}
