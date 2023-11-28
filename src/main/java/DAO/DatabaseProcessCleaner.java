package DAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import helper.Logging;

public class DatabaseProcessCleaner {

    public static void killSleepProcesses() {
        try (Connection conn = DatabaseConnectionHandler.getConnection();
             Statement stmt = conn.createStatement()) {

            // select all "sleep" process
            String query = "SHOW PROCESSLIST";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String command = rs.getString("Command");
                long id = rs.getLong("Id");

                // If the process is in the "sleep" state, terminate it.
                if ("Sleep".equals(command)) {
                    try (Statement killStmt = conn.createStatement()) {
                        killStmt.execute("KILL " + id);
                        Logging.getLogger().info("Killed process with ID: " + id);
                    } catch (SQLException e) {
                        Logging.getLogger().warning("Failed to kill process with ID: " + id);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        killSleepProcesses();
    }
}
