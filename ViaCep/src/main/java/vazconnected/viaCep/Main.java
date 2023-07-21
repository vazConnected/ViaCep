package vazconnected.viaCep;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class Main {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

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
                    Main.consultLocalResults();
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
            String filename = cep + ".json";
            FileManager.writeFile(filename, Main.gson.toJson(queryResult));
            System.out.println("O arquivo " + filename + " foi criado");
        } catch (IOException e) {
            System.err.println("Não foi possível armazenar o resultado em um arquivo. Erro:\n" +
                    e.getMessage());
        }

    }

    private static void consultLocalResults() {
        Pattern fileNamePattern = Pattern.compile("^[0-9]{8}.json$");

        ArrayList<File> files = new ArrayList( FileManager.getListOfFiles(".", fileNamePattern) );
        HashMap<String, File> mapNameWithFile = new HashMap<String, File>();

        if (files.isEmpty()) {
            System.out.println("Nenhum resultado armazenado foi encontrado.");
            return;
        }

        for (File file: files) {
            mapNameWithFile.put(file.getName(), file);
        }
        ArrayList<String> fileNames = new ArrayList<>(mapNameWithFile.keySet().stream().toList());
        Collections.sort(fileNames);

        System.out.println("Foram encontrados registros locais para os seguintes CEPs:");
        for (String fileName: fileNames) {
            System.out.println("\t" + fileName);
        }

        System.out.println("\nDigite o nome do arquivo que você deseja consultar: ");
        Scanner scanner = new Scanner(System.in);
        String selectedFileName = "";
        do {
            System.out.print("\nArquivo: ");
            selectedFileName = scanner.nextLine();

            if (!fileNames.contains(selectedFileName)) {
                System.err.println("O arquivo selecionado não existe. Tente novamente.");
            }
        } while (!fileNames.contains(selectedFileName));

        File selectedFile = mapNameWithFile.get(selectedFileName);
        try {
            Address cepInformation = Main.gson.fromJson(FileManager.readFile(selectedFile), Address.class);
            System.out.println("A seguir as informações obtidas: \n" + cepInformation);
        } catch (FileNotFoundException e) {
            System.err.println("Erro ao ler o arquivo de cep: " + e.getMessage());
        }
    }

}