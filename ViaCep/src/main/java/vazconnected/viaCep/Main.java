package vazconnected.viaCep;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("API ViaCep (https://viacep.com.br/)");

        Scanner systemInScanner = new Scanner(System.in);
        int menuOption = 0;
        do {
            System.out.println("\nSelecione uma das opções abaixo:\n" +
                "1 - Pesquisar cep;\n" +
                "2 - Consultar resultados armazenados;\n" +
                "0 - Sair.\n");

            menuOption =  systemInScanner.nextInt();
            switch(menuOption) {
                case(1): // Pesquisar cep
                    System.out.println("Pesquisa por cep selecionada\n");
                    Main.search();
                    break;
                case(2): // Consultar resultados armazenados
                    Main.listResults();
                    break;
                case(0): // Sair
                    System.out.println("Saindo...");
                    System.exit(0);
                    break;
                default:
                    System.err.println("Opção inválida. Tente novamente.");
                    break;
            }
        } while (menuOption != 0);
        systemInScanner.close();
    }

    private static void search() {
        Scanner cepSearchScanner = new Scanner(System.in);
        System.out.println("Para realizar a consulta, informe o cep a ser pesquisado.");

        String cep = "";
        boolean cepIsValid = false;
        do {
            System.out.print("\nCep: ");
            cep = ViaCep.formatCep( cepSearchScanner.next() );

            cepIsValid = ViaCep.cepIsValid(cep);
            if (!cepIsValid) System.err.println("Cep inválido. Tente novamente.");
        } while (!cepIsValid);
        //cepSearchScanner.close();

        Address queryResult = ViaCep.callApi(cep);
        if (queryResult.cep() == null) {
            System.out.println("Nenhum resultado encontrado para o cep solicitado!");
            return;
        }

        System.out.println("Resultados obtidos para o cep " + queryResult.cep() + ":\n" + queryResult);
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileManager.writeFile(cep + ".json", gson.toJson(queryResult));
            System.out.println("O arquivo " + cep + ".json" + " foi criado");
        } catch (IOException e) {
            System.err.println("Não foi possível armazenar o resultado em um arquivo. Erro:\n" +
                    e.getMessage());
        }

    }

    private static void listResults() {
        // Todo: ler diretorio de resultados, listar opções e informar resultados do cep selecionado
    }
}