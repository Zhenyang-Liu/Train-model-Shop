
import gui.LoginPage;
import gui.MainPage;
import helper.Logging;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import DAO.DatabaseConnectionHandler;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Welcome to TrainShop!");
            Logging.Init(false);

            LoginPage loginPage = new LoginPage();
            loginPage.setLoginSuccessListener(() -> {
                MainPage mainPage = new MainPage();
                mainPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainPage.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        DatabaseConnectionHandler.shutdown();
                        Logging.Close();
                    }
                });
                mainPage.setVisible(true);

                loginPage.setVisible(false);
            });

            loginPage.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
