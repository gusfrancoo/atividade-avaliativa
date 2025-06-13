// src/DAO/EmpresaDAO.java
package DAO;

import models.Empresa;
import persistence.Conn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpresaDAO {

    public EmpresaDAO() {
        try (Connection c = Conn.getConnection();
             Statement s = c.createStatement()) {
            s.execute("""
                CREATE TABLE IF NOT EXISTS empresa (
                  id SERIAL PRIMARY KEY,
                  nome VARCHAR(100) NOT NULL,
                  cnpj CHAR(14) UNIQUE NOT NULL,
                  endereco VARCHAR(200),
                  telefone VARCHAR(20)
                )
                """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void create(Empresa e) throws SQLException {
        String sql = "INSERT INTO empresa(nome,cnpj,endereco,telefone) VALUES (?,?,?,?)";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, e.getNome());
            pst.setString(2, e.getCnpj());
            pst.setString(3, e.getEndereco());
            pst.setString(4, e.getTelefone());
            pst.executeUpdate();
        }
    }

    public Empresa read(int id) throws SQLException {
        String sql = "SELECT * FROM empresa WHERE id = ?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Empresa(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cnpj"),
                        rs.getString("endereco"),
                        rs.getString("telefone")
                );
            }
        }
        return null;
    }

    public List<Empresa> readAll() throws SQLException {
        List<Empresa> lista = new ArrayList<>();
        String sql = "SELECT * FROM empresa";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                lista.add(new Empresa(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("cnpj"),
                        rs.getString("endereco"),
                        rs.getString("telefone")
                ));
            }
        }
        return lista;
    }

    public void update(Empresa e) throws SQLException {
        String sql = "UPDATE empresa SET nome=?,cnpj=?,endereco=?,telefone=? WHERE id=?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, e.getNome());
            pst.setString(2, e.getCnpj());
            pst.setString(3, e.getEndereco());
            pst.setString(4, e.getTelefone());
            pst.setInt(5, e.getId());
            pst.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM empresa WHERE id = ?";
        try (Connection conn = Conn.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }
}
