package helper;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Logging {

    private static Logger l = null;
    private static FileHandler fn = null;

    /**
     * Initialises logging. By default, the logfile is in TrainShop/log.log
     */
    public static void Init(){
        LogManager.getLogManager().reset();
        l = Logger.getLogger("Train Logger");
        try {
            fn = new FileHandler("log.log");
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
        l.addHandler(fn);
        SimpleFormatter f = new SimpleFormatter();
        fn.setFormatter(f);

        l.info("Initialised Logging");
    }

    /**
     * Gets the application's file logger.
     * Calls {@code Logger.Init()} if the logger is not already initialised
     * @return The logging instance
     */
    public static Logger getLogger(){
        if (l == null)
            Logging.Init();
        return l;
    }

    /**
     * Closes the logging file handle
     */
    public static void Close(){
        fn.close();
    }
}
