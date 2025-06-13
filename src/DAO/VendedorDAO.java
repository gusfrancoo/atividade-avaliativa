// src/DAO/VendedorDAO.java
package DAO;

import models.Vendedor;
import models.Supervisor;
import persistence.Conn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendedorDAO {

    private final SupervisorDAO supDAO = new SupervisorDAO();

    public VendedorDAO() {
        try (Connection c = Conn.getConnection();
             Statement s = c.createStatement()) {
            s.execute("""
                CREATE TABLE IF NOT EXISTS vendedor (
                  id SERIAL PRIMARY KEY,
                  nome VARCHAR(100) NOT NULL,
                  supervisor_id INT,
                  FOREIGN KEY (supervisor_id) REFERENCES supervisor(id)
                )
                """);
        } catch (SQLException e) {
            throw new RuntimeException("Erro criando tabela vendedor", e);
        }
    }

    public void create(Vendedor v) throws SQLException {
        String sql = "INSERT INTO vendedor(nome, supervisor_id) VALUES (?, ?)";
        try (Connection c = Conn.getConnection();
             PreparedStatement pst = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, v.getNome());
            pst.setInt(2, v.getSupervisor().getId());
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) v.setId(rs.getInt(1));
            }
        }
    }

    public Vendedor read(int id) throws SQLException {
        String sql = "SELECT * FROM vendedor WHERE id = ?";
        try (Connection c = Conn.getConnection();
             PreparedStatement pst = c.prepareStatement(sql)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Supervisor sup = supDAO.read(rs.getInt("supervisor_id"));
                    return new Vendedor(rs.getInt("id"), rs.getString("nome"), sup);
                }
            }
        }
        return null;
    }

    public List<Vendedor> readAll() throws SQLException {
        List<Vendedor> lista = new ArrayList<>();
        String sql = "SELECT id FROM vendedor";
        try (Connection c = Conn.getConnection();
             PreparedStatement pst = c.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                lista.add(read(rs.getInt("id")));
            }
        }
        return lista;
    }

    public void update(Vendedor v) throws SQLException {
        String sql = "UPDATE vendedor SET nome = ?, supervisor_id = ? WHERE id = ?";
        try (Connection c = Conn.getConnection();
             PreparedStatement pst = c.prepareStatement(sql)) {

            pst.setString(1, v.getNome());
            pst.setInt(2, v.getSupervisor().getId());
            pst.setInt(3, v.getId());
            pst.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM vendedor WHERE id = ?";
        try (Connection c = Conn.getConnection();
             PreparedStatement pst = c.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }
}
