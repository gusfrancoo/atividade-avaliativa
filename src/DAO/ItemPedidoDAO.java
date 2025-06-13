// src/DAO/ItemPedidoDAO.java
package DAO;

import models.ItemPedido;
import models.Pedido;
import models.Produto;
import persistence.Conn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemPedidoDAO {

    private final PedidoDAO   pedidoDAO   = new PedidoDAO();
    private final ProdutoDAO  produtoDAO  = new ProdutoDAO();

    public ItemPedidoDAO() {
        try (Connection c = Conn.getConnection();
             Statement s = c.createStatement()) {
            s.execute("""
                CREATE TABLE IF NOT EXISTS item_pedido (
                  id SERIAL PRIMARY KEY,
                  pedido_id INT,
                  produto_id INT,
                  quantidade INT,
                  valor_unitario DECIMAL(10,2),
                  FOREIGN KEY (pedido_id)  REFERENCES pedido(id),
                  FOREIGN KEY (produto_id) REFERENCES produto(id)
                )
                """);
        } catch (SQLException e) {
            throw new RuntimeException("Erro criando tabela item_pedido", e);
        }
    }

    public void create(ItemPedido ip) throws SQLException {
        String sql = """
            INSERT INTO item_pedido
              (pedido_id, produto_id, quantidade, valor_unitario)
            VALUES (?,?,?,?)
            """;
        try (Connection c = Conn.getConnection();
             PreparedStatement pst = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setInt(1, ip.getPedido().getId());
            pst.setInt(2, ip.getProduto().getId());
            pst.setInt(3, ip.getQuantidade());
            pst.setDouble(4, ip.getValorUnitario());
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) ip.setId(rs.getInt(1));
            }
        }
    }

    public ItemPedido read(int id) throws SQLException {
        String sql = "SELECT * FROM item_pedido WHERE id = ?";
        try (Connection c = Conn.getConnection();
             PreparedStatement pst = c.prepareStatement(sql)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Pedido p   = pedidoDAO.read(rs.getInt("pedido_id"));
                    Produto pr = produtoDAO.read(rs.getInt("produto_id"));
                    return new ItemPedido(
                            rs.getInt("id"),
                            p,
                            pr,
                            rs.getInt("quantidade"),
                            rs.getDouble("valor_unitario")
                    );
                }
            }
        }
        return null;
    }

    public List<ItemPedido> readAll() throws SQLException {
        List<ItemPedido> lista = new ArrayList<>();
        String sql = "SELECT id FROM item_pedido";
        try (Connection c = Conn.getConnection();
             PreparedStatement pst = c.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                lista.add(read(rs.getInt("id")));
            }
        }
        return lista;
    }

    public void update(ItemPedido ip) throws SQLException {
        String sql = """
            UPDATE item_pedido
               SET pedido_id = ?, produto_id = ?, quantidade = ?, valor_unitario = ?
             WHERE id = ?
            """;
        try (Connection c = Conn.getConnection();
             PreparedStatement pst = c.prepareStatement(sql)) {

            pst.setInt(1, ip.getPedido().getId());
            pst.setInt(2, ip.getProduto().getId());
            pst.setInt(3, ip.getQuantidade());
            pst.setDouble(4, ip.getValorUnitario());
            pst.setInt(5, ip.getId());
            pst.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM item_pedido WHERE id = ?";
        try (Connection c = Conn.getConnection();
             PreparedStatement pst = c.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }
}
