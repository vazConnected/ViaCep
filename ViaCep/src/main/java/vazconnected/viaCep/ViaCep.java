package vazconnected.viaCep;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Pattern;

public class ViaCep {
    private static final String baseUri = "https://viacep.com.br/ws/";
    private static final String defaultResponseMode = "/json/";

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private ViaCep() {}

    public static String formatCep(String cep) {
        // Remove all characters that are not digits
        Pattern pattern = Pattern.compile("\\d");
        StringBuilder builder = new StringBuilder();

        for (char c : cep.toCharArray()) {
            if (pattern.matcher(Character.toString(c)).matches()) {
                builder.append(c);
            }
        }

        return builder.toString();
    }

    public static Boolean cepIsValid(String cep) {
        Pattern pattern = Pattern.compile("^[0-9]{8}$");
        return pattern.matcher(cep).matches();
    }

    public static Address callApi(String cep) {
        if (!ViaCep.cepIsValid(cep)) throw new IllegalArgumentException("O cep não está formatado corretamente");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri( ViaCep.createUri(cep) ).build();

        String response = null;
        try {
            System.out.println("Enviando requisição ao ViaCep...");
            response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException exception) {
            throw new RuntimeException("Não foi possível obter as informações dos servidores ViaCep. Tente novamente mais tarde.");
        } catch (InterruptedException exception) {
            System.err.println("Erro de interrupção da Thread de requisição: " + exception.getMessage());
            System.exit(1);
        }
        System.out.println("Requisição realizada com sucesso.");

        if (!successfulResponse(response)) {
            throw new IllegalArgumentException("O cep informado não consta na base de dados da ViaCep.");
        }

        return gson.fromJson(response, Address.class);
    }

    private static URI createUri(String cep) {
        return URI.create(baseUri + cep + defaultResponseMode);
    }

    private static boolean successfulResponse(String response) {
        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
        try {
            Boolean error = jsonObject.get("erro").getAsBoolean();
            return error;
        } catch (UnsupportedOperationException | NullPointerException exception) {
            return true;
        }
    }
}
