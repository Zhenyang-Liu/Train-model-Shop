package controller;
import gui.MainPage;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import DAO.DatabaseConnectionHandler;

public class Main {
    public static void main(String[] args) {
        try{
            System.out.println("Welcome to TrainShop!");

            // Create Main Page Object
            MainPage mainPage = new MainPage();

            mainPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainPage.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    DatabaseConnectionHandler.shutdown();
                }
            });
            mainPage.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
