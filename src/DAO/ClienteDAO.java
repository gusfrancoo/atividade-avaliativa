// src/DAO/ClienteDAO.java
package DAO;

import models.Cliente;
import persistence.Conn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public ClienteDAO() {
        try (Connection c = Conn.getConnection();
             Statement s = c.createStatement()) {
            s.execute("""
                CREATE TABLE IF NOT EXISTS cliente (
                  id SERIAL PRIMARY KEY,
                  nome VARCHAR(100) NOT NULL,
                  cpf CHAR(11) UNIQUE NOT NULL,
                  telefone VARCHAR(20),
                  email VARCHAR(100)
                )
                """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void create(Cliente c) throws SQLException {
        String sql = "INSERT INTO cliente(nome,cpf,telefone,email) VALUES (?,?,?,?)";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, c.getNome());
            pst.setString(2, c.getCpf());
            pst.setString(3, c.getTelefone());
            pst.setString(4, c.getEmail());
            pst.executeUpdate();
        }
    }

    public Cliente read(int id) throws SQLException {
        String sql = "SELECT * FROM cliente WHERE id = ?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Cliente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("telefone"),
                        rs.getString("email")
                );
            }
        }
        return null;
    }

    public List<Cliente> readAll() throws SQLException {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                lista.add(new Cliente(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cpf"),
                        rs.getString("telefone"),
                        rs.getString("email")
                ));
            }
        }
        return lista;
    }

    public void update(Cliente c) throws SQLException {
        String sql = "UPDATE cliente SET nome=?,cpf=?,telefone=?,email=? WHERE id=?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, c.getNome());
            pst.setString(2, c.getCpf());
            pst.setString(3, c.getTelefone());
            pst.setString(4, c.getEmail());
            pst.setInt(5, c.getId());
            pst.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM cliente WHERE id = ?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }
}
