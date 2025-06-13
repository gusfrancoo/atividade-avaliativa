package models;

import java.time.LocalDate;
import java.util.List;

public class Pedido {
    private int id;
    private Cliente cliente;
    private Vendedor vendedor;
    private LocalDate data;
    private TipoPagamento tipoPagamento;
    private List<ItemPedido> itens;  // relacionamento 1:N

    public Pedido() {}

    public Pedido(int id, Cliente cliente, Vendedor vendedor,
                  LocalDate data, TipoPagamento tipoPagamento,
                  List<ItemPedido> itens) {
        this.id = id;
        this.cliente = cliente;
        this.vendedor = vendedor;
        this.data = data;
        this.tipoPagamento = tipoPagamento;
        this.itens = itens;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public TipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(TipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", cliente=" + cliente +
                ", vendedor=" + vendedor +
                ", data=" + data +
                ", tipoPagamento=" + tipoPagamento +
                ", itens=" + itens +
                '}';
    }
}
