import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import DAO.*;
import models.Categoria;
import models.Cliente;
import models.Empresa;
import models.Fabricante;
import models.ItemPedido;
import models.Pedido;
import models.Supervisor;
import models.TipoPagamento;
import models.Vendedor;

public class Main {
    private static final Scanner sc = new Scanner(System.in);

    private static final CategoriaDAO categoriaDAO       = new CategoriaDAO();
    private static final ClienteDAO   clienteDAO         = new ClienteDAO();
    private static final EmpresaDAO   empresaDAO         = new EmpresaDAO();
    private static final FabricanteDAO fabricanteDAO     = new FabricanteDAO();
    private static final SupervisorDAO supervisorDAO     = new SupervisorDAO();
    private static final VendedorDAO  vendedorDAO        = new VendedorDAO();
    private static final TipoPagamentoDAO tpDAO          = new TipoPagamentoDAO();
    private static final ProdutoDAO produtoDAO         = new ProdutoDAO();
    private static final PedidoDAO    pedidoDAO          = new PedidoDAO();
    private static final ItemPedidoDAO itemPedidoDAO     = new ItemPedidoDAO();

    public static void main(String[] args) throws SQLException {
        System.out.println("=== Iniciando aplicação ===");
        runMainMenu();
        System.out.println("=== Encerrando aplicação ===");
        sc.close();
    }

    private static void runMainMenu() throws SQLException {
        int op;
        do {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Categoria");
            System.out.println("2. Cliente");
            System.out.println("3. Empresa");
            System.out.println("4. Fabricante");
            System.out.println("5. Supervisor");
            System.out.println("6. Vendedor");
            System.out.println("7. TipoPagamento");
            System.out.println("8. Produto");
            System.out.println("9. Pedido");
            System.out.println("10. ItemPedido");
            System.out.println("0. Sair");
            System.out.print("Opção: ");
            op = Integer.parseInt(sc.nextLine());
            switch (op) {
                case 1  -> menuCategoria();
                case 2  -> menuCliente();
                case 3  -> menuEmpresa();
                case 4  -> menuFabricante();
                case 5  -> menuSupervisor();
                case 6  -> menuVendedor();
                case 7  -> menuTipoPagamento();
                case 8  -> menuProduto();
                case 9  -> menuPedido();
                case 10 -> menuItemPedido();
                case 0  -> { /* sair */ }
                default -> System.out.println("Opção inválida!");
            }
        } while (op != 0);
    }

    private static void menuCategoria() throws SQLException {
        int op;
        do {
            System.out.println("\n--- CRUD Categoria ---");
            System.out.println("1.Criar 2.Listar 3.Atualizar 4.Excluir 0.Voltar");
            System.out.print("Opção: ");
            op = Integer.parseInt(sc.nextLine());
            switch (op) {
                case 1 -> {
                    System.out.print("Nome: ");
                    String nome = sc.nextLine();
                    System.out.print("ID pai (ou ENTER): ");
                    String pid = sc.nextLine();
                    Categoria pai = pid.isBlank() ? null : categoriaDAO.read(Integer.parseInt(pid));
                    Categoria c = new Categoria(0, nome, pai);
                    categoriaDAO.create(c);
                    System.out.println("Criado: " + c);
                }
                case 2 -> {
                    List<Categoria> list = categoriaDAO.readAll();
                    list.forEach(System.out::println);
                }
                case 3 -> {
                    System.out.print("ID para atualizar: ");
                    int id = Integer.parseInt(sc.nextLine());
                    Categoria c = categoriaDAO.read(id);
                    if (c!=null) {
                        System.out.print("Novo nome ["+c.getNome()+"]: ");
                        String nn = sc.nextLine();
                        if (!nn.isBlank()) c.setNome(nn);
                        System.out.print("Novo pai ID [" +
                                (c.getCategoriaPai()!=null?c.getCategoriaPai().getId():"nenhum") + "]: ");
                        String np = sc.nextLine();
                        c.setCategoriaPai(np.isBlank()?null:categoriaDAO.read(Integer.parseInt(np)));
                        categoriaDAO.update(c);
                        System.out.println("Atualizado: "+c);
                    } else System.out.println("Não encontrado.");
                }
                case 4 -> {
                    System.out.print("ID para excluir: ");
                    categoriaDAO.delete(Integer.parseInt(sc.nextLine()));
                    System.out.println("Excluído.");
                }
                case 0 -> {}
                default -> System.out.println("Inválido.");
            }
        } while (op!=0);
    }

    private static void menuCliente() throws SQLException {
        int op;
        do {
            System.out.println("\n--- CRUD Cliente ---");
            System.out.println("1.Criar 2.Listar 3.Atualizar 4.Excluir 0.Voltar");
            System.out.print("Opção: ");
            op = Integer.parseInt(sc.nextLine());
            switch (op) {
                case 1 -> {
                    System.out.print("Nome: ");      String n = sc.nextLine();
                    System.out.print("CPF: ");       String cpf = sc.nextLine();
                    System.out.print("Telefone: ");  String tel = sc.nextLine();
                    System.out.print("Email: ");     String em = sc.nextLine();
                    Cliente cl = new Cliente(0,n,cpf,tel,em);
                    clienteDAO.create(cl);
                    System.out.println("Criado: "+cl);
                }
                case 2 -> clienteDAO.readAll().forEach(System.out::println);
                case 3 -> {
                    System.out.print("ID atualizar: "); int id = Integer.parseInt(sc.nextLine());
                    Cliente cl = clienteDAO.read(id);
                    if (cl!=null) {
                        System.out.print("Nome ["+cl.getNome()+"]: "); String nn = sc.nextLine();
                        if (!nn.isBlank()) cl.setNome(nn);
                        System.out.print("CPF ["+cl.getCpf()+"]: "); String nc = sc.nextLine();
                        if (!nc.isBlank()) cl.setCpf(nc);
                        System.out.print("Tel ["+cl.getTelefone()+"]: "); String nt = sc.nextLine();
                        if (!nt.isBlank()) cl.setTelefone(nt);
                        System.out.print("Email ["+cl.getEmail()+"]: "); String ne = sc.nextLine();
                        if (!ne.isBlank()) cl.setEmail(ne);
                        clienteDAO.update(cl);
                        System.out.println("Atualizado: "+cl);
                    } else System.out.println("Não existe.");
                }
                case 4 -> {
                    System.out.print("ID excluir: ");
                    clienteDAO.delete(Integer.parseInt(sc.nextLine()));
                    System.out.println("Excluído.");
                }
                case 0 -> {}
                default -> System.out.println("Inválido.");
            }
        } while (op!=0);
    }

    private static void menuEmpresa() throws SQLException {
        int op;
        do {
            System.out.println("\n--- CRUD Empresa ---");
            System.out.println("1.Criar 2.Listar 3.Atualizar 4.Excluir 0.Voltar");
            System.out.print("Opção: "); op=Integer.parseInt(sc.nextLine());
            switch (op) {
                case 1 -> {
                    System.out.print("Nome: "); String n=sc.nextLine();
                    System.out.print("CNPJ: "); String cnpj=sc.nextLine();
                    System.out.print("End.: "); String end=sc.nextLine();
                    System.out.print("Tel.: "); String t=sc.nextLine();
                    Empresa e=new Empresa(0,n,cnpj,end,t);
                    empresaDAO.create(e);
                    System.out.println("Criado: "+e);
                }
                case 2 -> empresaDAO.readAll().forEach(System.out::println);
                case 3 -> {
                    System.out.print("ID: "); int id=Integer.parseInt(sc.nextLine());
                    Empresa e=empresaDAO.read(id);
                    if(e!=null){
                        System.out.print("Nome ["+e.getNome()+"]: "); String nn=sc.nextLine();
                        if(!nn.isBlank()) e.setNome(nn);
                        System.out.print("CNPJ ["+e.getCnpj()+"]: "); String nc=sc.nextLine();
                        if(!nc.isBlank()) e.setCnpj(nc);
                        System.out.print("End ["+e.getEndereco()+"]: "); String ne=sc.nextLine();
                        if(!ne.isBlank()) e.setEndereco(ne);
                        System.out.print("Tel ["+e.getTelefone()+"]: "); String nt=sc.nextLine();
                        if(!nt.isBlank()) e.setTelefone(nt);
                        empresaDAO.update(e);
                        System.out.println("Atualizado: "+e);
                    } else System.out.println("Não existe.");
                }
                case 4 -> {
                    System.out.print("ID excluir: ");
                    empresaDAO.delete(Integer.parseInt(sc.nextLine()));
                    System.out.println("Excluído.");
                }
                case 0-> {}
                default-> System.out.println("Inválido.");
            }
        } while(op!=0);
    }

    private static void menuFabricante() throws SQLException {
        int op;
        do {
            System.out.println("\n--- CRUD Fabricante ---");
            System.out.println("1.Criar 2.Listar 3.Atualizar 4.Excluir 0.Voltar");
            System.out.print("Opção: "); op=Integer.parseInt(sc.nextLine());
            switch(op){
                case 1->{
                    System.out.print("Nome: "); String n=sc.nextLine();
                    System.out.print("País: "); String p=sc.nextLine();
                    Fabricante f=new Fabricante(0,n,p);
                    fabricanteDAO.create(f);
                    System.out.println("Criado: "+f);
                }
                case 2-> fabricanteDAO.readAll().forEach(System.out::println);
                case 3->{
                    System.out.print("ID: "); int id=Integer.parseInt(sc.nextLine());
                    Fabricante f=fabricanteDAO.read(id);
                    if(f!=null){
                        System.out.print("Nome["+f.getNome()+"]: "); String nn=sc.nextLine();
                        if(!nn.isBlank()) f.setNome(nn);
                        System.out.print("País["+f.getPaisOrigem()+"]: "); String np=sc.nextLine();
                        if(!np.isBlank()) f.setPaisOrigem(np);
                        fabricanteDAO.update(f);
                        System.out.println("Atualizado: "+f);
                    }else System.out.println("Não existe.");
                }
                case 4->{
                    System.out.print("ID: ");
                    fabricanteDAO.delete(Integer.parseInt(sc.nextLine()));
                    System.out.println("Excluído.");
                }
                case 0->{}
                default->System.out.println("Inválido.");
            }
        } while(op!=0);
    }

    private static void menuSupervisor() throws SQLException {
        int op;
        do {
            System.out.println("\n--- CRUD Supervisor ---");
            System.out.println("1.Criar 2.Listar 3.Atualizar 4.Excluir 0.Voltar");
            System.out.print("Opção: "); op=Integer.parseInt(sc.nextLine());
            switch(op){
                case 1->{
                    System.out.print("Nome: "); String n=sc.nextLine();
                    Supervisor s=new Supervisor(0,n);
                    supervisorDAO.create(s);
                    System.out.println("Criado: "+s);
                }
                case 2-> supervisorDAO.readAll().forEach(System.out::println);
                case 3->{
                    System.out.print("ID: "); int id=Integer.parseInt(sc.nextLine());
                    Supervisor s=supervisorDAO.read(id);
                    if(s!=null){
                        System.out.print("Nome["+s.getNome()+"]: "); String nn=sc.nextLine();
                        if(!nn.isBlank()) s.setNome(nn);
                        supervisorDAO.update(s);
                        System.out.println("Atualizado: "+s);
                    }else System.out.println("Não existe.");
                }
                case 4->{
                    System.out.print("ID: ");
                    supervisorDAO.delete(Integer.parseInt(sc.nextLine()));
                    System.out.println("Excluído.");
                }
                case 0->{} default->System.out.println("Inválido.");
            }
        } while(op!=0);
    }

    private static void menuVendedor() throws SQLException {
        int op;
        do {
            System.out.println("\n--- CRUD Vendedor ---");
            System.out.println("1.Criar 2.Listar 3.Atualizar 4.Excluir 0.Voltar");
            System.out.print("Opção: "); op=Integer.parseInt(sc.nextLine());
            switch(op){
                case 1->{
                    System.out.print("Nome: "); String n=sc.nextLine();
                    System.out.print("Supervisor ID: "); int sid=Integer.parseInt(sc.nextLine());
                    Vendedor v=new Vendedor(0,n,supervisorDAO.read(sid));
                    vendedorDAO.create(v);
                    System.out.println("Criado: "+v);
                }
                case 2-> vendedorDAO.readAll().forEach(System.out::println);
                case 3->{
                    System.out.print("ID: "); int id=Integer.parseInt(sc.nextLine());
                    Vendedor v=vendedorDAO.read(id);
                    if(v!=null){
                        System.out.print("Nome["+v.getNome()+"]: "); String nn=sc.nextLine();
                        if(!nn.isBlank()) v.setNome(nn);
                        System.out.print("Supervisor ID["+v.getSupervisor().getId()+"]: ");
                        String ns=sc.nextLine();
                        if(!ns.isBlank()) v.setSupervisor(supervisorDAO.read(Integer.parseInt(ns)));
                        vendedorDAO.update(v);
                        System.out.println("Atualizado: "+v);
                    }else System.out.println("Não existe.");
                }
                case 4->{
                    System.out.print("ID: ");
                    vendedorDAO.delete(Integer.parseInt(sc.nextLine()));
                    System.out.println("Excluído.");
                }
                case 0->{} default->System.out.println("Inválido.");
            }
        } while(op!=0);
    }

    private static void menuTipoPagamento() throws SQLException {
        int op;
        do {
            System.out.println("\n--- CRUD TipoPagamento ---");
            System.out.println("1.Criar 2.Listar 3.Atualizar 4.Excluir 0.Voltar");
            System.out.print("Opção: "); op=Integer.parseInt(sc.nextLine());
            switch(op){
                case 1->{
                    System.out.print("Descrição: "); String d=sc.nextLine();
                    TipoPagamento t=new TipoPagamento(0,d);
                    tpDAO.create(t);
                    System.out.println("Criado: "+t);
                }
                case 2-> tpDAO.readAll().forEach(System.out::println);
                case 3->{
                    System.out.print("ID: "); int id=Integer.parseInt(sc.nextLine());
                    TipoPagamento t=tpDAO.read(id);
                    if(t!=null){
                        System.out.print("Desc["+t.getDescricao()+"]: "); String nd=sc.nextLine();
                        if(!nd.isBlank()) t.setDescricao(nd);
                        tpDAO.update(t);
                        System.out.println("Atualizado: "+t);
                    }else System.out.println("Não existe.");
                }
                case 4->{
                    System.out.print("ID: ");
                    tpDAO.delete(Integer.parseInt(sc.nextLine()));
                    System.out.println("Excluído.");
                }
                case 0->{} default->System.out.println("Inválido.");
            }
        } while(op!=0);
    }

    private static void menuProduto() throws SQLException {
        int op;
        do {
            System.out.println("\n--- CRUD Produto ---");
            System.out.println("1.Criar 2.Listar 3.Atualizar 4.Excluir 0.Voltar");
            System.out.print("Opção: "); op=Integer.parseInt(sc.nextLine());
            switch(op){
                case 1->{
                    System.out.print("Nome: "); String n=sc.nextLine();
                    System.out.print("Preço: "); BigDecimal p=new BigDecimal(sc.nextLine());
                    System.out.print("Empresa ID: "); int eid=Integer.parseInt(sc.nextLine());
                    System.out.print("Fabricante ID: "); int fid=Integer.parseInt(sc.nextLine());
                    System.out.print("Categoria ID: "); int cid=Integer.parseInt(sc.nextLine());
                    var prod = new models.Produto(0,n,p,empresaDAO.read(eid),fabricanteDAO.read(fid),categoriaDAO.read(cid));
                    produtoDAO.create(prod);
                    System.out.println("Criado: "+prod);
                }
                case 2-> produtoDAO.readAll().forEach(System.out::println);
                case 3->{
                    System.out.print("ID: "); int id=Integer.parseInt(sc.nextLine());
                    var prod=produtoDAO.read(id);
                    if(prod!=null){
                        System.out.print("Nome["+prod.getNome()+"]: "); String nn=sc.nextLine();
                        if(!nn.isBlank()) prod.setNome(nn);
                        System.out.print("Preço["+prod.getPreco()+"]: "); String np=sc.nextLine();
                        if(!np.isBlank()) prod.setPreco(new BigDecimal(np));
                        // similar para empresa, fabricante, categoria
                        produtoDAO.update(prod);
                        System.out.println("Atualizado: "+prod);
                    }else System.out.println("Não existe.");
                }
                case 4->{
                    System.out.print("ID: ");
                    produtoDAO.delete(Integer.parseInt(sc.nextLine()));
                    System.out.println("Excluído.");
                }
                case 0->{} default->System.out.println("Inválido.");
            }
        } while(op!=0);
    }

    private static void menuPedido() throws SQLException {
        int op;
        do {
            System.out.println("\n--- CRUD Pedido ---");
            System.out.println("1.Criar 2.Listar 3.Atualizar 4.Excluir 0.Voltar");
            System.out.print("Opção: "); op=Integer.parseInt(sc.nextLine());
            switch(op){
                case 1->{
                    System.out.print("Cliente ID: "); int cid=Integer.parseInt(sc.nextLine());
                    System.out.print("Vendedor ID: "); int vid=Integer.parseInt(sc.nextLine());
                    System.out.print("Data (YYYY-MM-DD): "); LocalDate d=LocalDate.parse(sc.nextLine());
                    System.out.print("TipoPagamento ID: "); int tp=Integer.parseInt(sc.nextLine());
                    Pedido pe = new Pedido(0, clienteDAO.read(cid), vendedorDAO.read(vid), d, tpDAO.read(tp), List.of());
                    pedidoDAO.create(pe);
                    System.out.println("Criado: "+pe);
                }
                case 2-> pedidoDAO.readAll().forEach(System.out::println);
                case 3->{
                    System.out.print("ID: "); int id=Integer.parseInt(sc.nextLine());
                    Pedido pe=pedidoDAO.read(id);
                    if(pe!=null){
                        // similar prompts to update fields...
                        pedidoDAO.update(pe);
                        System.out.println("Atualizado: "+pe);
                    }else System.out.println("Não existe.");
                }
                case 4->{
                    System.out.print("ID: ");
                    pedidoDAO.delete(Integer.parseInt(sc.nextLine()));
                    System.out.println("Excluído.");
                }
                case 0->{} default->System.out.println("Inválido.");
            }
        } while(op!=0);
    }

    private static void menuItemPedido() throws SQLException {
        int op;
        do {
            System.out.println("\n--- CRUD ItemPedido ---");
            System.out.println("1.Criar 2.Listar 3.Atualizar 4.Excluir 0.Voltar");
            System.out.print("Opção: "); op=Integer.parseInt(sc.nextLine());
            switch(op){
                case 1->{
                    System.out.print("Pedido ID: "); int pid=Integer.parseInt(sc.nextLine());
                    System.out.print("Produto ID: "); int prid=Integer.parseInt(sc.nextLine());
                    System.out.print("Quantidade: "); int q=Integer.parseInt(sc.nextLine());
                    System.out.print("Valor unitário: "); double v=Double.parseDouble(sc.nextLine());
                    ItemPedido ip = new ItemPedido(0, pedidoDAO.read(pid), produtoDAO.read(prid), q, v);
                    itemPedidoDAO.create(ip);
                    System.out.println("Criado: "+ip);
                }
                case 2-> itemPedidoDAO.readAll().forEach(System.out::println);
                case 3->{
                    System.out.print("ID: "); int id=Integer.parseInt(sc.nextLine());
                    ItemPedido ip=itemPedidoDAO.read(id);
                    if(ip!=null){
                        // prompts to update...
                        itemPedidoDAO.update(ip);
                        System.out.println("Atualizado: "+ip);
                    }else System.out.println("Não existe.");
                }
                case 4->{
                    System.out.print("ID: ");
                    itemPedidoDAO.delete(Integer.parseInt(sc.nextLine()));
                    System.out.println("Excluído.");
                }
                case 0->{} default->System.out.println("Inválido.");
            }
        } while(op!=0);
    }
}
