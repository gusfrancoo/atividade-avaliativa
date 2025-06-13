// src/DAO/ProdutoDAO.java
package DAO;

import models.Produto;
import models.Empresa;
import models.Fabricante;
import models.Categoria;
import persistence.Conn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    private final EmpresaDAO empresaDAO       = new EmpresaDAO();
    private final FabricanteDAO fabricanteDAO = new FabricanteDAO();
    private final CategoriaDAO categoriaDAO   = new CategoriaDAO();

    public ProdutoDAO() {
        try (Connection c = Conn.getConnection();
             Statement s = c.createStatement()) {
            s.execute("""
                CREATE TABLE IF NOT EXISTS produto (
                  id SERIAL PRIMARY KEY,
                  nome VARCHAR(100) NOT NULL,
                  preco DECIMAL(10,2) NOT NULL,
                  empresa_id INT,
                  fabricante_id INT,
                  categoria_id INT,
                  FOREIGN KEY (empresa_id)    REFERENCES empresa(id),
                  FOREIGN KEY (fabricante_id) REFERENCES fabricante(id),
                  FOREIGN KEY (categoria_id)  REFERENCES categoria(id)
                )
                """);
        } catch (SQLException e) {
            throw new RuntimeException("Erro criando tabela produto", e);
        }
    }

    public void create(Produto p) throws SQLException {
        String sql = """
            INSERT INTO produto
              (nome, preco, empresa_id, fabricante_id, categoria_id)
            VALUES (?,?,?,?,?)
            """;
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, p.getNome());
            pst.setBigDecimal(2, p.getPreco());
            pst.setInt(3, p.getEmpresa().getId());
            pst.setInt(4, p.getFabricante().getId());
            pst.setInt(5, p.getCategoria().getId());
            pst.executeUpdate();

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    p.setId(rs.getInt(1));
                }
            }
        }
    }

    public Produto read(int id) throws SQLException {
        String sql = "SELECT * FROM produto WHERE id = ?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Empresa    e = empresaDAO.read(rs.getInt("empresa_id"));
                    Fabricante f = fabricanteDAO.read(rs.getInt("fabricante_id"));
                    Categoria  c = categoriaDAO.read(rs.getInt("categoria_id"));

                    return new Produto(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getBigDecimal("preco"),
                            e, f, c
                    );
                }
            }
        }
        return null;
    }

    public List<Produto> readAll() throws SQLException {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT id FROM produto";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                lista.add(read(rs.getInt("id")));
            }
        }
        return lista;
    }

    public void update(Produto p) throws SQLException {
        String sql = """
            UPDATE produto
               SET nome=?, preco=?, empresa_id=?, fabricante_id=?, categoria_id=?
             WHERE id=?
            """;
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, p.getNome());
            pst.setBigDecimal(2, p.getPreco());
            pst.setInt(3, p.getEmpresa().getId());
            pst.setInt(4, p.getFabricante().getId());
            pst.setInt(5, p.getCategoria().getId());
            pst.setInt(6, p.getId());
            pst.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM produto WHERE id = ?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }
}
