package helper;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Logging {
    private static Logger l = null;
    private static FileHandler fn = null;
    public static void Init(){
        LogManager.getLogManager().reset();
        l = Logger.getLogger("Train Logger");
        try {
            fn = new FileHandler("C:/Users/joant/Documents/log.log");
        } catch (SecurityException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        l.addHandler(fn);
        SimpleFormatter f = new SimpleFormatter();
        fn.setFormatter(f);

        l.info("Initialised Logging");
    }

    public static Logger getLogger(){
        if (l == null)
            Logging.Init();
        return l;
    }

    public static void Close(){
        fn.close();
    }
}
