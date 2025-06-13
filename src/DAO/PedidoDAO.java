// src/DAO/PedidoDAO.java
package DAO;

import models.Pedido;
import models.Cliente;
import models.Vendedor;
import models.TipoPagamento;
import persistence.Conn;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    private final ClienteDAO       clienteDAO       = new ClienteDAO();
    private final VendedorDAO      vendedorDAO      = new VendedorDAO();
    private final TipoPagamentoDAO tpDao            = new TipoPagamentoDAO();

    public PedidoDAO() {
        System.out.println("⏳ Iniciando criação da tabela 'pedido'...");
        try (Connection c = Conn.getConnection();
             Statement s = c.createStatement()) {

            String ddl = """
                CREATE TABLE IF NOT EXISTS pedido (
                  id SERIAL PRIMARY KEY,
                  cliente_id INT REFERENCES cliente(id),
                  vendedor_id INT REFERENCES vendedor(id),
                  data DATE,
                  tipo_pagamento_id INT REFERENCES tipo_pagamento(id)
                )
                """;
            s.execute(ddl);

            // LOG de sucesso
            System.out.println("✅ Tabela 'pedido' criada com sucesso.");
        } catch (SQLException e) {
            // LOG de erro e relança
            System.err.println("❌ Erro criando tabela 'pedido': " + e.getMessage());
            throw new RuntimeException("Erro criando tabela pedido", e);
        }
    }


    public void create(Pedido p) throws SQLException {
        String sql = """
            INSERT INTO pedido
              (cliente_id, vendedor_id, data, tipo_pagamento_id)
            VALUES (?,?,?,?)
            """;
        try (Connection c = Conn.getConnection();
             PreparedStatement pst = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setInt(1, p.getCliente().getId());
            pst.setInt(2, p.getVendedor().getId());
            pst.setDate(3, Date.valueOf(p.getData()));
            pst.setInt(4, p.getTipoPagamento().getId());
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) p.setId(rs.getInt(1));
            }
        }
    }

    public Pedido read(int id) throws SQLException {
        String sql = "SELECT * FROM pedido WHERE id = ?";
        try (Connection c = Conn.getConnection();
             PreparedStatement pst = c.prepareStatement(sql)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Cliente cli   = clienteDAO.read(rs.getInt("cliente_id"));
                    Vendedor ven  = vendedorDAO.read(rs.getInt("vendedor_id"));
                    LocalDate dt  = rs.getDate("data").toLocalDate();
                    TipoPagamento tp = tpDao.read(rs.getInt("tipo_pagamento_id"));
                    // Itens podem ser carregados via itemPedidoDAO.readAll() e filtrados por pedido_id
                    return new Pedido(id, cli, ven, dt, tp, new ArrayList<>());
                }
            }
        }
        return null;
    }

    public List<Pedido> readAll() throws SQLException {
        List<Pedido> lista = new ArrayList<>();
        String sql = "SELECT id FROM pedido";
        try (Connection c = Conn.getConnection();
             PreparedStatement pst = c.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                lista.add(read(rs.getInt("id")));
            }
        }
        return lista;
    }

    public void update(Pedido p) throws SQLException {
        String sql = """
            UPDATE pedido
               SET cliente_id=?, vendedor_id=?, data=?, tipo_pagamento_id=?
             WHERE id=?
            """;
        try (Connection c = Conn.getConnection();
             PreparedStatement pst = c.prepareStatement(sql)) {

            pst.setInt(1, p.getCliente().getId());
            pst.setInt(2, p.getVendedor().getId());
            pst.setDate(3, Date.valueOf(p.getData()));
            pst.setInt(4, p.getTipoPagamento().getId());
            pst.setInt(5, p.getId());
            pst.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM pedido WHERE id = ?";
        try (Connection c = Conn.getConnection();
             PreparedStatement pst = c.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }
}
