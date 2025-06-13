// src/DAO/SupervisorDAO.java
package DAO;

import models.Supervisor;
import persistence.Conn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupervisorDAO {

    public SupervisorDAO() {
        try (Connection c = Conn.getConnection();
             Statement s = c.createStatement()) {
            s.execute("""
                CREATE TABLE IF NOT EXISTS supervisor (
                  id SERIAL PRIMARY KEY,
                  nome VARCHAR(100) NOT NULL
                )
                """);
        } catch (SQLException e) {
            throw new RuntimeException("Erro criando tabela supervisor", e);
        }
    }

    public void create(Supervisor s) throws SQLException {
        String sql = "INSERT INTO supervisor(nome) VALUES (?)";
        try (Connection c = Conn.getConnection();
             PreparedStatement pst = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, s.getNome());
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) s.setId(rs.getInt(1));
            }
        }
    }

    public Supervisor read(int id) throws SQLException {
        String sql = "SELECT * FROM supervisor WHERE id = ?";
        try (Connection c = Conn.getConnection();
             PreparedStatement pst = c.prepareStatement(sql)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Supervisor(rs.getInt("id"), rs.getString("nome"));
                }
            }
        }
        return null;
    }

    public List<Supervisor> readAll() throws SQLException {
        List<Supervisor> lista = new ArrayList<>();
        String sql = "SELECT id FROM supervisor";
        try (Connection c = Conn.getConnection();
             PreparedStatement pst = c.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                lista.add(read(rs.getInt("id")));
            }
        }
        return lista;
    }

    public void update(Supervisor s) throws SQLException {
        String sql = "UPDATE supervisor SET nome = ? WHERE id = ?";
        try (Connection c = Conn.getConnection();
             PreparedStatement pst = c.prepareStatement(sql)) {

            pst.setString(1, s.getNome());
            pst.setInt(2, s.getId());
            pst.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM supervisor WHERE id = ?";
        try (Connection c = Conn.getConnection();
             PreparedStatement pst = c.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }
}
