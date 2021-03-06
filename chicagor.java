import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.print.DocFlavor.STRING;
 

public class chicagor {
 
    /**
     * Connect to the test.db database
     * @return the Connection object
     */
    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:/Users/linfengx/crime.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
 
    
    /**
     * select all rows in the warehouses table
     
    public void selectAll(){
        String sql = "SELECT * FROM chicagor limit 5";
        
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            System.out.println("id " + "minX " + "maxX " + "minY " + "maxY");
            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" + 
                                   rs.getDouble("minX") + "\t" +
                                   rs.getDouble("maxX") + "\t" +
                                   rs.getDouble("minY") + "\t" +
                                   rs.getDouble("maxY"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }*/
    //function that find the range of crime map
    /*
        select min(minX) from chicagor;
        36.6194458007812

        select min(maxX) from chicagor;
        36.6194496154785

        select max(minX) from chicagor;
        42.0229034423828

        select max(maxX) from chicagor;
        42.0229110717773

        select min(minY) from chicagor;
        -91.6865692138672

        select min(maxY) from chicagor;
        -91.6865539550781

        select max(minY) from chicagor;
        -87.5245361328125

        select max(maxY) from chicagor;
        -87.5245208740234
    */
    double length;
    double width;
    double bucketlen;
    double bucketwid;
    double xdiff;
    double ydiff;
    double xbound;
    double ybound;
    String sqlminX;
    String sqlmaxX;
    String sqlminY;
    String sqlmaxY;
    String sql;
    String where;
    public void mapRange(){
        sqlminX = "select min(minX) from chicagor;";
        sqlmaxX = "select max(maxX) from chicagor;";
        sqlminY = "select min(minY) from chicagor;";
        sqlmaxY = "select max(maxY) from chicagor;";
        
       // String query = "select " + sql + " from chicagor";
        try (Connection conn = this.connect();
             //Statement stat  = conn.createStatement();
             //ResultSet rs  = stat.executeQuery(query);
             Statement min1  = conn.createStatement();
             ResultSet minX  = min1.executeQuery(sqlminX);
             Statement max1  = conn.createStatement();
             ResultSet maxX  = max1.executeQuery(sqlmaxX);
             Statement min2  = conn.createStatement();
             ResultSet minY  = min2.executeQuery(sqlminY);
             Statement max2  = conn.createStatement();
             ResultSet maxY  = max2.executeQuery(sqlmaxY)){
           // System.out.println("lowerleft corner is " + rs.getDouble(sql));
             //loop through the result set
            /*System.out.println("minX is " + minX.getDouble("min(minX)") + "\n" +
                               "minY is " + minY.getDouble("min(minY)") + "\n" +
                               "maxX is " + maxX.getDouble("max(maxX)") + "\n" +
                               "maxY is " + maxY.getDouble("max(maxY)"));
           /* while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" + 
                                   rs.getDouble("minX") + "\t" +
                                   rs.getDouble("maxX") + "\t" +
                                   rs.getDouble("minY") + "\t" +
                                   rs.getDouble("maxY"));
            }*/
            /*double [] lowel = {minX.getDouble("min(minX)"), minY.getDouble("min(minY)")};
            double [] lower = {maxX.getDouble("max(maxX)"), minY.getDouble("min(minY)")};
            double [] upl = {minX.getDouble("min(minX)"), maxY.getDouble("max(maxY)")};
            double [] upr = {maxX.getDouble("max(maxX)"), maxY.getDouble("max(maxY)")};*/
            //System.out.println(lowel[0]+ "\n" + lowel[1]);
            xbound = minX.getDouble("min(minX)");
            ybound = minY.getDouble("min(minY)");
            length = maxX.getDouble("max(maxX)") - minX.getDouble("min(minX)");
            width = maxY.getDouble("max(maxY)") - minY.getDouble("min(minY)");
            bucketlen = length/100;
            bucketwid = width/100;
            xdiff = bucketlen/10;
            ydiff = bucketwid/10;
            //System.out.println(bucketlen + "\n" + bucketwid);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        for (int i = 0; i < 100; i++){
            sql = "select count(*) from chicagor ";
            where = "where maxX < " + (xbound + bucketlen + (i * xdiff)) + " and maxY < " + (ybound + bucketwid + (i * ydiff))
                    + " and minX > " + (xbound + (i * xdiff)) + " and minY > " + (ybound + (i * ydiff));
        
            try (Connection conn2 = this.connect();
                 Statement bucket = conn2.createStatement();
                 ResultSet num = bucket.executeQuery(sql + where)){
                    System.out.println("bucket " + i + " has  " + num.getDouble("count(*)"));
            } catch (SQLException e2){
                    System.out.println(e2.getMessage());
            }
        }
    }

    public static void main(String[] args) {
       chicagor map = new chicagor();
       map.mapRange();
       
    }
 
}