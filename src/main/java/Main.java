
import gui.WelcomePage;
import helper.Logging;

import java.awt.*;

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
