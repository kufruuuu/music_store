package app;

import dao.*;
import model.Cliente;
import model.Disco;
import model.Pedido;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Sessao
{
    public static void sessaoCliente()
    {
        Scanner input = new Scanner(System.in);
        Cliente cliente = new Cliente();

        System.out.print("Digite o seu login: ");
        cliente.setLogin(input.nextLine());
        System.out.print("Digite a sua senha: ");
        cliente.setSenha(input.nextLine());

        try {
            DAOFactory daoFactory = new DAOFactory();
            ClienteDAO clienteDAO = daoFactory.getCLienteDAO();
            clienteDAO.authenticate(cliente);
        } catch (SecurityException ex) {
            System.out.println("ERRO: Login ou senha incorretos");
            return;
        } catch (IOException | ClassNotFoundException | SQLException ex) {
            System.out.println("ERRO: Problemas com o servidor");
            return;
        }

        boolean executando = true;

        while(executando){
            System.out.println("------------------------------------------------------");
            System.out.println("                  Lea Record Shop");
            System.out.println("------------------------------------------------------");
            System.out.println("Bem vindo, " + cliente.getNomeCompleto());
            System.out.println("O que deseja fazer?");
            System.out.println("1 - Ver dados cadastrais");
            System.out.println("2 - Alterar dados cadastrais");
            System.out.println("3 - Buscar discos");
            System.out.println("4 - Meus pedidos");
            System.out.println("5 - Sair");
            System.out.print("Escolha: ");
            String escolha = input.nextLine();

            switch (escolha) {
                case "1":
                    Sessao.verCadastro(cliente.getLogin());
                    break;
                case "2":
                    Sessao.alterarCadastro(cliente);
                    break;
                case "3":
                    Sessao.explorarDiscos(cliente);
                    break;
                case "4":
                    Sessao.listarPedidos(cliente);
                    break;
                case "5":
                    executando = false;
                    break;
                default:
                    System.out.println("ERRO: opção inválida!");
                    break;
            }
        }
    }

    private static void verCadastro(String login)
    {
        try {
            DAOFactory daoFactory = new DAOFactory();
            ClienteDAO clienteDAO = daoFactory.getCLienteDAO();
            List<Cliente> clienteList = clienteDAO.read(new String[]{login});
            System.out.println("Nome completo: " + clienteList.get(0).getNomeCompleto());
            System.out.println("Documento: " + clienteList.get(0).getDocumento());
            System.out.println("Data de nascimento: " + clienteList.get(0).getDataNascimento());
            System.out.println("Email: " + clienteList.get(0).getEmail());
            System.out.println("Telefone: " + clienteList.get(0).getTelefone());
            System.out.println("Status da conta: " + (clienteList.get(0).getAtivado() ? "ativada" : "desativada"));
        } catch (SQLException ex) {
            System.out.println();
        } catch ( IOException | ClassNotFoundException ex) {
            System.out.println("Erro: problemas com o servidor");
        }
    }

    private static void alterarCadastro(Cliente cliente)
    {
        Scanner input = new Scanner(System.in);

        System.out.println("------------------------------------------------------");
        System.out.println("                  Lea Record Shop");
        System.out.println("------------------------------------------------------");
        System.out.println("Qual dado você deseja alterar em seu cadastro, " + cliente.getNomeCompleto() + '?');
        System.out.println("1 - Senha");
        System.out.println("2 - Nome Completo");
        System.out.println("3 - Documento");
        System.out.println("4 - Data de Nascimento");
        System.out.println("5 - Endereço de e-mail");
        System.out.println("6 - Número de telefone");
        System.out.println("7 - " + (cliente.getAtivado() ? "Desativar " : "Ativar ") + "conta");
        System.out.println("8 - Voltar");
        System.out.print("Escolha: ");
        String escolha = input.nextLine();

        Cliente dummy = new Cliente();
        dummy.setLogin(cliente.getLogin());

        switch (escolha) {
            case "1":
                try {
                    DAOFactory daoFactory = new DAOFactory();
                    ClienteDAO clienteDAO = daoFactory.getCLienteDAO();
                    System.out.print("Digite a sua senha atual: ");
                    dummy.setSenha(input.nextLine());
                    clienteDAO.authenticate(dummy);
                    dummy.setNomeCompleto(null);
                    System.out.print("Digite a nova senha: ");
                    dummy.setSenha(input.nextLine());
                    clienteDAO.update(dummy);
                    System.out.println("Senha alteradada com sucesso!");
                } catch (SQLException ex) {
                    System.out.println("Erro ao alterar a senha");
                } catch (ClassNotFoundException | IOException ex) {
                    System.out.println("Erro: problemas com o servidor");
                }
                break;
            case "2":
                try {
                    DAOFactory daoFactory = new DAOFactory();
                    ClienteDAO clienteDAO = daoFactory.getCLienteDAO();
                    System.out.print("Digite o novo nome completo: ");
                    dummy.setNomeCompleto(input.nextLine());
                    clienteDAO.update(dummy);
                    cliente.setNomeCompleto(dummy.getNomeCompleto());
                    System.out.println("Nome completo alterado com sucesso!");
                } catch (SQLException ex) {
                    System.out.println("Erro ao alterar o nome completo");
                } catch (ClassNotFoundException | IOException ex) {
                    System.out.println("Erro: problemas com o servidor");
                }
                break;
            case "3":
                try {
                    DAOFactory daoFactory = new DAOFactory();
                    ClienteDAO clienteDAO = daoFactory.getCLienteDAO();
                    System.out.print("Digite o novo documento: ");
                    dummy.setDocumento(input.nextLine());
                    clienteDAO.update(dummy);
                    System.out.println("Documento alterado com sucesso!");
                } catch (SQLException ex) {
                    System.out.println("Erro ao alterar o documento");
                } catch (ClassNotFoundException | IOException ex) {
                    System.out.println("Erro: problemas com o servidor");
                }
                break;
            case "4":
                try {
                    DAOFactory daoFactory = new DAOFactory();
                    ClienteDAO clienteDAO = daoFactory.getCLienteDAO();
                    System.out.print("Digite a nova data de nascimento no formato (yyyy-MM-dd): ");
                    dummy.setDataNascimento(input.nextLine());
                    clienteDAO.update(dummy);
                    System.out.println("Data de nascimento alterada com sucesso!");
                } catch (SQLException ex) {
                    if (ex.getMessage().equals("Erro: formato de data invalido!"))
                        System.out.println(ex.getMessage());
                    else
                        System.out.println(ex.getMessage());
                } catch (ClassNotFoundException | IOException ex) {
                    System.out.println("Erro: problemas com o servidor");
                }
                break;
            case "5":
                try {
                    DAOFactory daoFactory = new DAOFactory();
                    ClienteDAO clienteDAO = daoFactory.getCLienteDAO();
                    System.out.print("Digite o novo endereço de email: ");
                    dummy.setEmail(input.nextLine());
                    clienteDAO.update(dummy);
                    System.out.println("Endereço de email alterado com sucesso!");
                } catch (SQLException ex) {
                    System.out.println("Erro ao alterar o email");
                } catch (ClassNotFoundException | IOException ex) {
                    System.out.println("Erro: problemas com o servidor");
                }
                break;
            case "6":
                try {
                    DAOFactory daoFactory = new DAOFactory();
                    ClienteDAO clienteDAO = daoFactory.getCLienteDAO();
                    System.out.print("Digite o novo número de telefone: ");
                    dummy.setTelefone(input.nextLine());
                    clienteDAO.update(dummy);
                    System.out.println("Número de telefone alterado com sucesso!");
                } catch (SQLException ex) {
                    System.out.println("Erro ao alterar o telefone");
                } catch (ClassNotFoundException | IOException ex) {
                    System.out.println("Erro: problemas com o servidor");
                }
                break;
            case "7":
                try {
                    DAOFactory daoFactory = new DAOFactory();
                    ClienteDAO clienteDAO = daoFactory.getCLienteDAO();
                    dummy.setAtivado(!cliente.getAtivado());
                    clienteDAO.update(dummy);
                    System.out.println("Cliente " + (cliente.getAtivado() ? "desativado" : "ativado") + " com sucesso!");
                    cliente.setAtivado(!cliente.getAtivado());
                } catch (SQLException ex) {
                    System.out.println("Erro ao ativar/desativar conta");
                } catch (ClassNotFoundException | IOException ex) {
                    System.out.println("Erro: problemas com o servidor");
                }
                break;
            default:
                System.out.println("Erro: opção inválida!");
                break;
        }
    }

    private static void listarPedidos(Cliente cliente)
    {
        try {
            DAOFactory daoFactory = new DAOFactory();
            PedidoDAO pedidoDAO = daoFactory.getPedidoDAO();
            List<Pedido> pedidoList = pedidoDAO.read(new String[]{cliente.getLogin()});
            if(pedidoList.size() != 0){
                System.out.println("Pedidos encontrados:");
                for(Pedido pedido: pedidoList) {
                    System.out.println("Nota fiscal: " + pedido.getNotaFiscal()
                            + ", " + pedido.getDisco().getNome() + " do artista "
                            + pedido.getDisco().getArtista());
                }
            } else
                System.out.println("Não há pedidos na sua lista");
        } catch (SQLException ex) {
            System.out.println("Erro ao listar pedidos");
        } catch (ClassNotFoundException | IOException ex) {
            System.out.println("Erro: problemas com o servidor");
        }
    }

    private static void explorarDiscos(Cliente cliente)
    {
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.println("------------------------------------------------------");
            System.out.println("                  Lea Record Shop");
            System.out.println("------------------------------------------------------");
            System.out.println("O que procura?");
            System.out.println("1 - Buscar discos pelo nome");
            System.out.println("2 - Buscar discos pelo artista");
            System.out.println("3 - Buscar discos pelo estilo");
            System.out.println("4 - Buscar discos pelo ano de lançamento");
            System.out.println("5 - Buscar todos os discos");
            System.out.println("6 - Voltar");
            System.out.print("Escolha: ");

            String escolha = input.nextLine();
            String[] args = new String[2];

            switch(escolha){
                case "1":
                    System.out.print("Digite o nome do disco: ");
                    args[0] = input.nextLine();
                    args[1] = "0";
                    break;
                case "2":
                    System.out.print("Digite o nome do artista do disco: ");
                    args[0] = input.nextLine();
                    args[1] = "1";
                    break;
                case "3":
                    System.out.print("Digite o estilo do disco: ");
                    args[0] = input.nextLine();
                    args[1] = "2";
                    break;
                case "4":
                    System.out.print("Digite o ano de lançamento do disco: ");
                    args[0] = input.nextLine();
                    args[1] = "3";
                    break;
                case "5":
                    args[1] = "-1";
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Erro: opção inválida!");
                    break;
            }

            try {
                DAOFactory daoFactory = new DAOFactory();
                DiscoDAO discoDAO = daoFactory.getDiscoDAO();
                List<Disco> discoList;

                if(!args[1].equals("-1"))
                    discoList = discoDAO.read(args);
                else
                    discoList = discoDAO.all();

                if(discoList.size() == 0)
                    System.out.println("Nenhum disco encontrado");
                else
                    efetuarPedido(cliente, discoList);
            } catch (SQLException ex) {
                System.out.println("Erro ao listar discos");
            } catch (ClassNotFoundException | IOException ex) {
                System.out.println("Erro: problemas com o servidor");
            }
        }
    }

    private static void efetuarPedido(Cliente cliente, List<Disco> discoList)
    {
        Scanner input = new Scanner(System.in);
        boolean executando = true;

        while(executando) {
            System.out.println("------------------------------------------------------");
            System.out.println("                  Lea Record Shop");
            System.out.println("------------------------------------------------------");
            System.out.println("Discos encontrados:");
            for(int i = 0; i < discoList.size(); i++){
                System.out.println(i + 1 + " - " + discoList.get(i).getNome() + " de " + discoList.get(i).getArtista());
            }

            System.out.print("Digite a sua escolha ou insira \"0\" para voltar: ");
            int escolha = Integer.parseInt(input.nextLine());

            if(escolha == 0)
                executando = false;
            else if(!cliente.getAtivado()) {
                System.out.println("Conta desativada, reative-a para poder comprar disco");
                executando = false;
            } else if(escolha >= 0 && escolha <= discoList.size()) {
                try {
                    DAOFactory daoFactory = new DAOFactory();
                    PedidoDAO pedidoDAO = daoFactory.getPedidoDAO();
                    Pedido pedido = new Pedido();
                    pedido.setCliente(cliente);
                    pedido.setDisco(discoList.get(escolha - 1));
                    pedidoDAO.create(pedido);
                    System.out.println("Pedido efetuado com sucesso!");
                } catch (SQLException ex) {
                    if(ex.getMessage().contains("unidades restantes"))
                        System.out.println("Erro: não há mais unidades restantes!");
                    else
                        System.out.println("Erro ao efetuar pedido");
                } catch (IOException | ClassNotFoundException ex) {
                    System.out.println("Erro: problemas com o servidor");
                }
            } else
                System.out.println("Erro: opção inválida!");
        }
    }
}
