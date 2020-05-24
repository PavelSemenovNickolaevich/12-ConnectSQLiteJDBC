package javabegin.demo;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBUtils {

    private static Connection con;

    public static void openConnection(String path) {
        try {

//            Driver driver = (Driver) Class.forName("org.sqlite.JDBC").newInstance(); // - необязательно в последних версиях драйверов

            // создание подключение к базе данных по пути, указанному в урле
            String url = "jdbc:sqlite:" + path;

            if (con == null) {
                con = DriverManager.getConnection(url);
            }



        } catch (SQLException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static ArrayList<SprObject> getResultList(String sql) {

        ArrayList<SprObject> list = new ArrayList<>();
        
        
        // все объявленные ресурсы в скобках try будут закрываться автоматически после выполнения блока
        try (Statement statement = con.createStatement(); ResultSet rs = statement.executeQuery(sql);){
                        
            while (rs.next()) {

                SprObject obj = new SprObject();
                obj.setId(rs.getInt("id"));
                obj.setName_ru(rs.getString("name_ru"));
                obj.setName_en(rs.getString("name_en"));

                list.add(obj);

            }
        } catch (SQLException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
       

        return list;

    }

    public static void closeConnection() {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void showPrepStatement() {
        
        // все объявленные ресурсы в скобках try будут закрываться автоматически после выполнения блока
        try (PreparedStatement pstmt = con.prepareStatement("select * from spr_Model WHERE name_ru = ? or name_en=?")){
            

            pstmt.setString(1, "Прадо");
            pstmt.setString(2, "Golf");

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("name_ru")+", "+rs.getString("name_en"));
            }
      
            rs.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(DBUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
