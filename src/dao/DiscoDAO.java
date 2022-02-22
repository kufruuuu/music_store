package dao;

import model.Disco;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiscoDAO extends DAO<Disco>
{
    DiscoDAO(Connection connection) {
        super(connection);
    }

    private final String READ_QUERY =
            "SELECT * FROM music_store.read_discos(?, ?)";

    private final String ALL_QUERY =
            "SELECT * FROM music_store.disco";

    private final String CREATE_QUERY =
            "SELECT * FROM music_store.create_disco(?, ?, ?, ?, ?)";

    @Override
    public void create(Disco disco) throws SQLException
    {
        try {
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY);
            statement.setString(1, disco.getNome());
            statement.setString(2, disco.getArtista());
            statement.setString(3, disco.getEstilo());
            statement.setString(4, disco.getAnoLancamento());
            statement.setInt(5, disco.getUnidades());
            ResultSet result = statement.executeQuery();
            result.next();
            boolean status = result.getBoolean("result");
            if(!status)
                throw new SQLException("Erro: tentativa de criar discos incosistentes");
        } catch (SQLException ex) {
            throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public List<Disco> read(String[] args) throws SQLException
    {
        List<Disco> discoList = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement(READ_QUERY);
            statement.setInt(2, Integer.parseInt(args[1]));
            statement.setString(1, args[0]);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Disco disco = new Disco();
                disco.setNome(result.getString("nome"));
                disco.setArtista(result.getString("artista"));
                disco.setEstilo(result.getString("estilo"));
                disco.setAnoLancamento(result.getString("ano_lancamento"));
                disco.setUnidades(result.getInt("unidades"));
                discoList.add(disco);
            }
        } catch (SQLException ex) {
            throw new SQLException(ex.getMessage());
        }

        return discoList;
    }

    @Override
    public void update(Disco disco) throws SQLException {
        System.out.println("ERRO: método não implementado!");
    }

    @Override
    public void delete(String arg) throws SQLException {
        System.out.println("ERRO: método não implementado!");
    }

    @Override
    public List<Disco> all() throws SQLException
    {
        List<Disco> discoList = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Disco disco = new Disco();
                disco.setNome(result.getString("nome"));
                disco.setArtista(result.getString("artista"));
                disco.setEstilo(result.getString("estilo"));
                disco.setAnoLancamento(result.getString("ano_lancamento"));
                disco.setUnidades(result.getInt("unidades"));
                discoList.add(disco);
            }
        } catch (SQLException ex) {
            throw new SQLException(ex.getMessage());
        }

        return discoList;
    }

    @Override
    public void authenticate(Disco disco) throws SQLException, SecurityException {
        System.out.println("ERRO: método não implementado!");
    }
}
