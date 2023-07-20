package vazconnected.viaCep;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("API ViaCep (https://viacep.com.br/)");

        Scanner systemInScanner = new Scanner(System.in);
        Character menuOption = 'x';
        do {
            System.out.println("Selecione uma das opções abaixo:\n" +
                "1 - Pesquisar cep;" +
                "2 - Consultar resultados armazenados;" +
                "X - Sair.");

            menuOption =  systemInScanner.next().toLowerCase().charAt(0);

            switch(menuOption) {
                case('1'): // Pesquisar cep
                    Main.search();
                    break;
                case('2'): // Consultar resultados armazenados
                    Main.listResults();
                    break;
                case('x'): // Sair
                    System.out.println("Saindo...");
                    System.exit(0);
                    break;
                default:
                    System.err.println("Opção inválida. Tente novamente.");
                    break;
            }

        } while (menuOption.equals("x"));
    }

    private static void search() {
        // Todo: Chamar API, obter resultados e salvar arquivo
    }

    private static void listResults() {
        // Todo: ler diretorio de resultados, listar opções e informar resultados do cep selecionado
    }
}