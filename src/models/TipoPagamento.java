package models;

public class TipoPagamento {
    private int id;
    private String descricao;

    public TipoPagamento() {}

    public TipoPagamento(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "TipoPagamento{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
