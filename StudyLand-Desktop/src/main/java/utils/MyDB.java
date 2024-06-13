package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDB {

    private final String URL ="jdbc:mysql://localhost:3306/studyland";
    private  final String USER="root";
    private  final String PWD="";

    private Connection connection;
    //3 variable pour stocker linstance
    private static MyDB instance ;

    // 1 rendre le constructeur prive
    private MyDB (){
        try {
            connection = DriverManager.getConnection(URL, USER, PWD);
            System.out.println("coonected to DB");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }}

    //2 creer une methode static por utiliser le constructeur   avec une seule instance : sangleton
    public  static MyDB getInstance(){
        if (instance==null){
            instance = new MyDB();
        }return instance;


    }

    public MyDB(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }
}
