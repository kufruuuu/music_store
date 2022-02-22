package dao;

import model.Cliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO extends DAO<Cliente>
{
    private static final String AUTHENTICATE_QUERY =
            "SELECT nome_completo, ativado FROM music_store.cliente WHERE login = ? AND senha = md5(?)";

    private final String CREATE_QUERY =
            "INSERT INTO music_store.cliente(login, senha, nome_completo, documento, data_nascimento, email, telefone, ativado) VALUES" +
            "(?, md5(?), ?, ?, ?, ?, ?, TRUE)";

    private final String UPDATE_QUERY =
            "CALL music_store.update_cliente(?, ?, ?)";

    private final String UPDATE_QUERY_NASC =
            "UPDATE music_store.cliente SET data_nascimento = ? WHERE login = ?";

    private final String UPDATE_QUERY_ACTIVATED =
            "UPDATE music_store.cliente SET ativado = ? WHERE login = ?";

    private static final String READ_QUERY =
            "SELECT * FROM music_store.cliente WHERE login = ?";

    public ClienteDAO(Connection connection) {
        super(connection);
    }

    @Override
    public void create(Cliente cliente) throws SQLException
    {
        try {
            SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsed = date_format.parse(cliente.getDataNascimento());
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY);

            statement.setString(1, cliente.getLogin());
            statement.setString(2, cliente.getSenha());
            statement.setString(3, cliente.getNomeCompleto());
            statement.setString(4, cliente.getDocumento());
            statement.setDate(5, new java.sql.Date(parsed.getTime()));
            statement.setString(6, cliente.getEmail());
            statement.setString(7, cliente.getTelefone());

            statement.executeUpdate();
        } catch (SQLException ex) {
            if(ex.getMessage().contains("already exists"))
                throw new SQLException("Erro: chave primaria violada");
            throw new SQLException("Erro ao criar cliente");
        } catch (ParseException ex) {
            throw new SQLException("Erro: formato de data invalido!");
        }
    }

    @Override
    public List<Cliente> read(String[] args) throws SQLException
    {
        List<Cliente> clienteList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setString(1, args[0]);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                do {
                    Cliente cliente = new Cliente();
                    cliente.setLogin(result.getString("login"));
                    cliente.setNomeCompleto(result.getString("nome_completo"));
                    cliente.setDocumento(result.getString("documento"));
                    cliente.setDataNascimento(result.getDate("data_nascimento").toString());
                    cliente.setEmail(result.getString("email"));
                    cliente.setTelefone(result.getString("telefone"));
                    cliente.setAtivado(result.getBoolean("ativado"));
                    clienteList.add(cliente);
                } while (result.next());
            } else
                throw new SecurityException("Login nao encontrado.");
        } catch (SQLException ex) {
            throw new SQLException("Erro ao ler cliente.");
        }
        return clienteList;
    }

    @Override
    public void update(Cliente cliente) throws SQLException
    {
        try {
            String query = cliente.getDataNascimento() == null ? cliente.getAtivado() == null ?
                    UPDATE_QUERY : UPDATE_QUERY_ACTIVATED : UPDATE_QUERY_NASC;
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(2, cliente.getLogin());

            if(cliente.getSenha() != null) {
                statement.setString(1, cliente.getSenha());
                statement.setInt(3, 0);
            } else if(cliente.getNomeCompleto() != null) {
                statement.setString(1, cliente.getNomeCompleto());
                statement.setInt(3, 1);
            } else if(cliente.getDocumento() != null) {
                statement.setString(1, cliente.getDocumento());
                statement.setInt(3, 2);
            } else if(cliente.getEmail() != null) {
                statement.setString(1, cliente.getEmail());
                statement.setInt(3, 3);
            } else if(cliente.getTelefone() != null) {
                statement.setString(1, cliente.getTelefone());
                statement.setInt(3, 4);
            } else if(cliente.getDataNascimento() != null) {
                SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date parsed = date_format.parse(cliente.getDataNascimento());
                statement.setDate(1, new java.sql.Date(parsed.getTime()));
            } else if(cliente.getAtivado() != null){
                statement.setBoolean(1, cliente.getAtivado());
            }

            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException(ex.getMessage());
        } catch (ParseException ex) {
            throw new SQLException("Erro: formato de data invalido!");
        }
    }

    @Override
    public void delete(String arg) throws SQLException {
        System.out.println("ERRO: método nao implementado!");
    }

    @Override
    public List<Cliente> all() throws SQLException {
        System.out.println("ERRO: método nao implementado!");
        return null;
    }

    @Override
    public void authenticate(Cliente cliente) throws SQLException, SecurityException
    {
        try (PreparedStatement statement = connection.prepareStatement(AUTHENTICATE_QUERY)) {
            statement.setString(1, cliente.getLogin());
            statement.setString(2, cliente.getSenha());

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    cliente.setNomeCompleto(result.getString("nome_completo"));
                    cliente.setAtivado(result.getBoolean("ativado"));
                } else
                    throw new SecurityException("Login ou senha incorretos.");
            }
        } catch (SQLException ex) {
            throw new SQLException("Erro ao autenticar cliente.");
        }
    }
}
