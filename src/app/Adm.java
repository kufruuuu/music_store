package app;

import dao.DAOFactory;
import dao.DiscoDAO;
import model.Disco;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Adm
{
    private static void listarDiscos()
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

            switch (escolha) {
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

                if (!args[1].equals("-1"))
                    discoList = discoDAO.read(args);
                else
                    discoList = discoDAO.all();

                if (discoList.size() == 0)
                    System.out.println("Nenhum disco encontrado");
                else {
                    System.out.println("Discos encontrados:");
                    for(int i = 0; i < discoList.size(); i++){
                        System.out.println(i + 1 + " - " + discoList.get(i).getNome() + " de " + discoList.get(i).getArtista());
                    }
                }
            } catch (SQLException ex) {
                System.out.println("Erro ao listar discos");
            } catch (ClassNotFoundException | IOException ex) {
                System.out.println("Erro: problemas com o servidor");
            }
        }
    }

    private static void inserirDisco()
    {
        Scanner input = new Scanner(System.in);
        Disco disco = new Disco();

        System.out.print("Insira o nome do novo disco: ");
        disco.setNome(input.nextLine());
        System.out.print("Insira o nome do artista: ");
        disco.setArtista(input.nextLine());
        System.out.print("Insira o estilo musical: ");
        disco.setEstilo(input.nextLine());
        System.out.print("Insira o ano de lançamento: ");
        disco.setAnoLancamento(input.nextLine());
        System.out.print("Insira o número de unidades a serem inseridas: ");
        disco.setUnidades(Integer.parseInt(input.nextLine()));

        try {
            DAOFactory daoFactory = new DAOFactory();
            DiscoDAO discoDAO = daoFactory.getDiscoDAO();
            discoDAO.create(disco);
            System.out.println("Inventário atualizado com sucesso!");
        } catch (SQLException ex) {
            if(ex.getMessage().contains("incosistentes"))
                System.out.println("Erro: tentativa de inserir discos inconsistentes, consulte o README.md");
            else
                System.out.println(ex.getMessage());
        } catch (ClassNotFoundException | IOException ex) {
            System.out.println("Erro com o servidor");
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
            System.out.println("1 - Buscar discos");
            System.out.println("2 - Inserir discos na loja");
            System.out.println("3 - Sair do programa");
            System.out.print("Escolha: ");
            String escolha = input.nextLine();

            switch (escolha) {
                case "1":
                    listarDiscos();
                    break;
                case "2":
                    inserirDisco();
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
