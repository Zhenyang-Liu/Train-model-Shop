
import gui.LoginPage;
import gui.MainPage;
import gui.WelcomePage;
import helper.Logging;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import DAO.DatabaseConnectionHandler;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                System.out.println("Welcome to TrainShop!");
                Logging.Init(false);

                WelcomePage welcomePage = new WelcomePage();
                welcomePage.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
