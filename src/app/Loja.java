package app;

import dao.ClienteDAO;
import dao.DAOFactory;
import model.Cliente;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Loja
{
    private static void criarContaCLiente()
    {
        Scanner input = new Scanner(System.in);

        Cliente cliente = new Cliente();
        System.out.print("Digite um login para usuário: ");
        cliente.setLogin(input.nextLine());
        System.out.print("Digite uma senha: ");
        cliente.setSenha(input.nextLine());
        System.out.print("Digite o seu nome completo: ");
        cliente.setNomeCompleto(input.nextLine());
        System.out.print("Digite um número de documento: ");
        cliente.setDocumento(input.nextLine());
        System.out.print("Digite sua data de nascimento no fromato (yyyy-MM-dd): ");
        cliente.setDataNascimento(input.nextLine());
        System.out.print("Digite um endereço de e-mail: ");
        cliente.setEmail(input.nextLine());
        System.out.print("Digite um número de telefone: ");
        cliente.setTelefone(input.nextLine());

        try {
            DAOFactory daoFactory = new DAOFactory();
            ClienteDAO clienteDAO = daoFactory.getCLienteDAO();
            clienteDAO.create(cliente);
            System.out.println("Conta criada com sucesso!");
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            if(ex.getMessage().equals("Erro: formato de data invalido!"))
                System.out.println("Erro: formato de data inválido!");
            else if (ex.getMessage().contains("chave primaria violada"))
                System.out.println("Erro: login já é usado por outra conta!");
            else
                System.out.println("Erro: problemas com o servidor");
        }
    }

    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        boolean executando = true;

        while(executando) {
            System.out.println("------------------------------------------------------");
            System.out.println("                  Lea Record Shop");
            System.out.println("------------------------------------------------------");
            System.out.println("Escolha uma das seguintes ações:");
            System.out.println("1 - Criar conta de cliente");
            System.out.println("2 - Fazer login como cliente");
            System.out.println("3 - Sair do programa");
            System.out.print("Escolha: ");
            String escolha = input.nextLine();

            switch (escolha) {
                case "1":
                    criarContaCLiente();
                    break;
                case "2":
                    Sessao.sessaoCliente();
                    break;
                case "3":
                    executando = false;
                    break;
                default:
                    System.out.println("ERRO: opção inválida!");
                    break;
            }
        }
    }
}
