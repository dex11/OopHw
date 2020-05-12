import java.sql.*;

public class DataBase {


    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        Connection c = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/?useSSL=false",
                "root",
                "109823ნიკა11");
        Statement cDbStm = c.createStatement();
        cDbStm.execute("DROP DATABASE IF EXISTS mydb");
        cDbStm.execute("CREATE DATABASE mydb");
        ResultSet result = cDbStm.executeQuery("SELECT count(*) FROM metropolises;");
        assert(result.next());
        System.out.println(result.getInt(0));
    }
}
