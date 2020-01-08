package server.DAO;

import java.sql.Connection;
import java.sql.DriverManager;

// This Class is used to establish conn with DB  :
// it's a singleton class !!

public class JDBCUtils {


    private static JDBCUtils con;
    private static Connection ConnSQL;

    private JDBCUtils()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Driver established !!  ");
            ConnSQL= DriverManager.getConnection("jdbc:mysql://localhost:3306/online_exams","root","");
            System.out.println("Connexion established !! ");
        }
        catch (Exception ex)
        {
            System.err.println(ex.getMessage());
        }
    }

    // synchronized to prevent threads interferences
    synchronized public static Connection ConnectMySQL()
        {
            if(con==null)
            {
                con=new JDBCUtils();
            }
            return con.ConnSQL;
        }
    }