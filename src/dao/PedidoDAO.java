package dao;

import model.Cliente;
import model.Disco;
import model.Pedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO extends DAO<Pedido>
{
    public PedidoDAO(Connection connection) {
        super(connection);
    }

    private final String CREATE_QUERY =
            "INSERT INTO music_store.pedido(cliente_login, disco_nome, disco_artista) VALUES (?, ?, ?)";

    private final String READ_QUERY =
            "SELECT * FROM music_store.pedido WHERE cliente_login = ?";

    @Override
    public void create(Pedido pedido) throws SQLException {
        try {
            PreparedStatement statement = connection.prepareStatement(CREATE_QUERY);
            statement.setString(1, pedido.getCliente().getLogin());
            statement.setString(2, pedido.getDisco().getNome());
            statement.setString(3, pedido.getDisco().getArtista());
            int rows = statement.executeUpdate();
        } catch (SQLException ex) {
            if(ex.getMessage().contains("chk_pedido_disco_unidades"))
                throw new SQLException("Erro: não há unidades restantes!");
            else
                throw new SQLException(ex.getMessage());
        }
    }

    @Override
    public List<Pedido> read(String[] args) throws SQLException
    {
        List<Pedido> pedidoList = new ArrayList<>();

        try {
            PreparedStatement statement = connection.prepareStatement(READ_QUERY);
            statement.setString(1, args[0]);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Pedido pedido = new Pedido();
                pedido.setCliente(new Cliente());
                pedido.getCliente().setLogin(result.getString("cliente_login"));
                pedido.setDisco(new Disco());
                pedido.getDisco().setNome(result.getString("disco_nome"));
                pedido.getDisco().setArtista(result.getString("disco_artista"));
                pedido.setNotaFiscal(result.getInt("nota_fiscal"));
                pedidoList.add(pedido);
            }
        } catch (SQLException ex) {
            throw new SQLException(ex.getMessage());
        }
        return pedidoList;
    }

    @Override
    public void update(Pedido pedido) throws SQLException {
        System.out.println("ERRO: método não implementado!");
    }

    @Override
    public void delete(String arg) throws SQLException {
        System.out.println("ERRO: método não implementado!");
    }

    @Override
    public List<Pedido> all() throws SQLException {
        System.out.println("ERRO: método não implementado!");
        return null;
    }

    @Override
    public void authenticate(Pedido pedido) throws SQLException, SecurityException {
        System.out.println("ERRO: método não implementado!");
    }
}
