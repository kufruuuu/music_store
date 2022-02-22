package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory
{
    private static ConnectionFactory instance = null;

    private final String dbHost = "localhost";
    private final String dbPort = "5432";
    private final String dbName = "postgres";
    private final String dbUser = "shalashaska";
    private final String dbPassword = "rebolation";

    protected ConnectionFactory() {}

    public static ConnectionFactory getInstance()
    {
        if (instance == null) {
            instance = new ConnectionFactory();
        }

        return instance;
    }

    public Connection getConnection() throws ClassNotFoundException, SQLException
    {
        Connection connection = null;

        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;
            connection = DriverManager.getConnection(url, dbUser, dbPassword);
        } catch (ClassNotFoundException ex) {
            System.err.println(ex.getMessage());
            throw new ClassNotFoundException("Erro ao iniciar o driver.");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            throw new SQLException("Erro de conexao ao banco de dados.");
        }

        return connection;
    }
}
