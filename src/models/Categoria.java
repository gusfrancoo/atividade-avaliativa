package models;

public class Categoria {
    private int id;
    private String nome;
    private Categoria categoriaPai;

    public Categoria() {}

    public Categoria(int id, String nome, Categoria categoriaPai) {
        this.id = id;
        this.nome = nome;
        this.categoriaPai = categoriaPai;
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

    public Categoria getCategoriaPai() {
        return categoriaPai;
    }

    public void setCategoriaPai(Categoria categoriaPai) {
        this.categoriaPai = categoriaPai;
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", categoriaPai=" + (categoriaPai != null ? categoriaPai.getId() : "null") +
                '}';
    }
}
