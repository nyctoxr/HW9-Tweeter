package Tweeter;
import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
@Getter
public class Datasource {
    @Getter
    private static final Connection connection;

    private Datasource() {

    }
    static{

        final String url = "jdbc:postgresql://localhost:5432/mytweeter";
        final String user = "postgres";
        final String password = "sajad123";
        try{
            connection= DriverManager.getConnection(url,user,password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // JVM Shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

}
