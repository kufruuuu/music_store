package dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import jdbc.ConnectionFactory;

public class DAOFactory implements AutoCloseable
{
    private Connection connection = null;

    public DAOFactory() throws ClassNotFoundException, IOException, SQLException {
        connection = ConnectionFactory.getInstance().getConnection();
    }

    public void beginTransaction() throws SQLException {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());

            throw new SQLException("Erro ao abrir transacao.");
        }
    }

    public void commitTransaction() throws SQLException {
        try {
            connection.commit();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());

            throw new SQLException("Erro ao finalizar transacao.");
        }
    }

    public void rollbackTransaction() throws SQLException {
        try {
            connection.rollback();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());

            throw new SQLException("Erro ao executar transacao.");
        }
    }

    public void endTransaction() throws SQLException {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());

            throw new SQLException("Erro ao finalizar transacao.");
        }
    }

    public void closeConnection() throws SQLException {
        try {
            connection.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());

            throw new SQLException("Erro ao fechar conexao ao banco de dados.");
        }
    }

    @Override
    public void close() throws SQLException {
        closeConnection();
    }

    public ClienteDAO getCLienteDAO() {
        return new ClienteDAO(connection);
    }

    public DiscoDAO getDiscoDAO() {
        return new DiscoDAO(connection);
    }

    public PedidoDAO getPedidoDAO() {
        return new PedidoDAO(connection);
    }
}
