package server.DAOs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Utils {
    public static ResultSet execute(HashMap<Integer,Object> sets,String query){
        try {
            Connection conn = DataSource.getInstance().getConnection();
            PreparedStatement statement = conn.prepareStatement(query);
            sets.forEach((key,value)->{
                try {
                    if(value instanceof String){
                        statement.setString(key,(String)value);
                    }
                    else if(value instanceof Integer){
                        statement.setInt(key,(Integer)value);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            ResultSet resultSet = statement.executeQuery();
            return resultSet;

        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }

    }
}
