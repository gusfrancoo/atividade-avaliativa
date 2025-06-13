// src/DAO/FabricanteDAO.java
package DAO;

import models.Fabricante;
import persistence.Conn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FabricanteDAO {

    public FabricanteDAO() {
        try (Connection c = Conn.getConnection();
             Statement s = c.createStatement()) {
            s.execute("""
                CREATE TABLE IF NOT EXISTS fabricante (
                  id SERIAL PRIMARY KEY,
                  nome VARCHAR(100) NOT NULL,
                  pais_origem VARCHAR(50)
                )
                """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void create(Fabricante f) throws SQLException {
        String sql = "INSERT INTO fabricante(nome,pais_origem) VALUES (?,?)";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, f.getNome());
            pst.setString(2, f.getPaisOrigem());
            pst.executeUpdate();
        }
    }

    public Fabricante read(int id) throws SQLException {
        String sql = "SELECT * FROM fabricante WHERE id = ?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Fabricante(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("pais_origem")
                );
            }
        }
        return null;
    }

    public List<Fabricante> readAll() throws SQLException {
        List<Fabricante> lista = new ArrayList<>();
        String sql = "SELECT * FROM fabricante";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                lista.add(new Fabricante(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("pais_origem")
                ));
            }
        }
        return lista;
    }

    public void update(Fabricante f) throws SQLException {
        String sql = "UPDATE fabricante SET nome=?,pais_origem=? WHERE id=?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, f.getNome());
            pst.setString(2, f.getPaisOrigem());
            pst.setInt(3, f.getId());
            pst.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM fabricante WHERE id = ?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }
}
