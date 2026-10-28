package agendaapp;

import java.sql.*;

public class ConnexionDB {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
    private static final String USER = "agenda_user";
    private static final String PASSWORD = "agenda123";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Impossible de charger le pilote JDBC !");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}