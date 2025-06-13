// src/DAO/CategoriaDAO.java
package DAO;

import models.Categoria;
import persistence.Conn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public CategoriaDAO() {
        // Garante existência da tabela categoria com auto-relacionamento
        try (Connection conn = Conn.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS categoria (
                  id SERIAL PRIMARY KEY,
                  nome VARCHAR(100) NOT NULL,
                  categoria_pai_id INT,
                  FOREIGN KEY (categoria_pai_id) REFERENCES categoria(id)
                )
                """);
        } catch (SQLException e) {
            throw new RuntimeException("Erro criando tabela categoria", e);
        }
    }

    public void create(Categoria c) throws SQLException {
        String sql = "INSERT INTO categoria(nome, categoria_pai_id) VALUES (?, ?)";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, c.getNome());
            if (c.getCategoriaPai() != null) {
                pst.setInt(2, c.getCategoriaPai().getId());
            } else {
                pst.setNull(2, Types.INTEGER);
            }
            pst.executeUpdate();

            // recupera o ID gerado
            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    c.setId(rs.getInt(1));
                }
            }
        }
    }

    public Categoria read(int id) throws SQLException {
        String sql = "SELECT id, nome, categoria_pai_id FROM categoria WHERE id = ?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Integer paiId = rs.getObject("categoria_pai_id", Integer.class);
                    Categoria pai = (paiId != null) ? read(paiId) : null;
                    return new Categoria(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            pai
                    );
                }
            }
        }
        return null;
    }

    public List<Categoria> readAll() throws SQLException {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT id FROM categoria";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                lista.add(read(rs.getInt("id")));
            }
        }
        return lista;
    }

    public void update(Categoria c) throws SQLException {
        String sql = "UPDATE categoria SET nome = ?, categoria_pai_id = ? WHERE id = ?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, c.getNome());
            if (c.getCategoriaPai() != null) {
                pst.setInt(2, c.getCategoriaPai().getId());
            } else {
                pst.setNull(2, Types.INTEGER);
            }
            pst.setInt(3, c.getId());
            pst.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM categoria WHERE id = ?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }
}
