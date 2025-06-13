// src/DAO/TipoPagamentoDAO.java
package DAO;

import models.TipoPagamento;
import persistence.Conn;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoPagamentoDAO {

    public TipoPagamentoDAO() {
        try (Connection c = Conn.getConnection();
             Statement s = c.createStatement()) {
            s.execute("""
                CREATE TABLE IF NOT EXISTS tipo_pagamento (
                  id SERIAL PRIMARY KEY,
                  descricao VARCHAR(50) NOT NULL
                )
                """);
        } catch (SQLException e) {
            throw new RuntimeException("Erro criando tabela tipo_pagamento", e);
        }
    }

    public void create(TipoPagamento t) throws SQLException {
        String sql = "INSERT INTO tipo_pagamento(descricao) VALUES (?)";
        try (Connection c = Conn.getConnection();
             PreparedStatement pst = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, t.getDescricao());
            pst.executeUpdate();
            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) t.setId(rs.getInt(1));
            }
        }
    }

    public TipoPagamento read(int id) throws SQLException {
        String sql = "SELECT * FROM tipo_pagamento WHERE id = ?";
        try (Connection c = Conn.getConnection();
             PreparedStatement pst = c.prepareStatement(sql)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new TipoPagamento(rs.getInt("id"), rs.getString("descricao"));
                }
            }
        }
        return null;
    }

    public List<TipoPagamento> readAll() throws SQLException {
        List<TipoPagamento> lista = new ArrayList<>();
        String sql = "SELECT id FROM tipo_pagamento";
        try (Connection c = Conn.getConnection();
             PreparedStatement pst = c.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                lista.add(read(rs.getInt("id")));
            }
        }
        return lista;
    }

    public void update(TipoPagamento t) throws SQLException {
        String sql = "UPDATE tipo_pagamento SET descricao = ? WHERE id = ?";
        try (Connection c = Conn.getConnection();
             PreparedStatement pst = c.prepareStatement(sql)) {

            pst.setString(1, t.getDescricao());
            pst.setInt(2, t.getId());
            pst.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM tipo_pagamento WHERE id = ?";
        try (Connection c = Conn.getConnection();
             PreparedStatement pst = c.prepareStatement(sql)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }
}
