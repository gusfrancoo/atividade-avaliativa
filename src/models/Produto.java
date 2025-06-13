package models;

import java.math.BigDecimal;

public class Produto {
    private int id;
    private String nome;
    private BigDecimal preco;
    private Empresa empresa;
    private Fabricante fabricante;
    private Categoria categoria;

    public Produto() {}

    public Produto(
        int id, String nome, BigDecimal preco,
        Empresa empresa, Fabricante fabricante,Categoria categoria
    ) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.empresa = empresa;
        this.fabricante = fabricante;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Fabricante getFabricante() {
        return fabricante;
    }

    public void setFabricante(Fabricante fabricante) {
        this.fabricante = fabricante;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                ", empresa=" + empresa +
                ", fabricante=" + fabricante +
                ", categoria=" + categoria +
                '}';
    }
}
