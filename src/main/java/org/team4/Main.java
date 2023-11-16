package org.team4;
import gui.MainPage;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting gui...");

        // MainPage
        MainPage mainPage = new MainPage();
        
        mainPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPage.setVisible(true);
    }
}
