package org.example;
import gui.MainPage;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        // Create Main Page Object
        MainPage mainPage = new MainPage();
        
        mainPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPage.setVisible(true);
    }
}
