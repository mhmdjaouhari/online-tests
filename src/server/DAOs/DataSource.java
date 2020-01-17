package server.DAOs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/* this class holds the database resource (connection object) and use the singleton pattern for optimisation */

public class DataSource{

    private static DataSource instance = new DataSource();
    private Connection connection;

    private DataSource(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver loaded successfully ...");
            connection = DriverManager
                    .getConnection("jdbc:mysql://localhost:3306/online-tests","root","");
            System.out.println("Database Connected");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }

    public static DataSource getInstance() {
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}


