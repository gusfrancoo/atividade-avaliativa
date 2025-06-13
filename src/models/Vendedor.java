package models;

public class Vendedor {
    private int id;
    private String nome;
    private Supervisor supervisor;  // relacionamento 1:N

    public Vendedor() {}

    public Vendedor(int id, String nome, Supervisor supervisor) {
        this.id = id;
        this.nome = nome;
        this.supervisor = supervisor;
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

    public Supervisor getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(Supervisor supervisor) {
        this.supervisor = supervisor;
    }

    @Override
    public String toString() {
        return "Vendedor{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", supervisor=" + supervisor +
                '}';
    }
}