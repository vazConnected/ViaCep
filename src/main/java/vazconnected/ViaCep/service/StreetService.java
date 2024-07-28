package vazconnected.ViaCep.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import lombok.NonNull;
import vazconnected.ViaCep.Cep;

public class StreetService {
	private static final String baseUrl = "https://viacep.com.br/ws/";
	private static final String defaultResponseMode = "/json/";
	
	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	private static Set<String> states = Set.of("AC", "AL", "AP", "AM", "BA", "CE", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE",
			"PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "DF");
	
	public List<Cep> searchStreet(@NonNull String stateInitials, @NonNull String city, @NonNull String street) throws IOException, InterruptedException {
	    if (!states.contains(stateInitials)) {
	        throw new InvalidParameterException("O estado informado não existe.");
	    }

	    HttpClient client = HttpClient.newHttpClient();
	    HttpRequest request = HttpRequest.newBuilder().uri(this.getStreetSearchUri(stateInitials, city, street)).build();

	    System.out.println("Enviando requisição ao ViaCep. Pesquisando por rua...");
	    String response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();

	    List<Cep> ceps = null;
	    JsonElement responseElement = gson.fromJson(response, JsonElement.class);
		if (responseElement.isJsonArray()) {
		    ceps = gson.fromJson(response, new TypeToken<List<Cep>>() {}.getType());
		} else {
		    JsonObject responseJson = gson.fromJson(response, JsonObject.class);
		    if (responseJson.has("erro")) {
		        String errorMessage = responseJson.get("erro").getAsString();
		        throw new RuntimeException("Erro da API ViaCEP: " + errorMessage);
		    } else {
		        throw new RuntimeException("A resposta da API ViaCep veio em um formato inesperado.");
		    }
		}
		
		for(Cep cep : ceps) {
			Cep.saveCepInCache(cep);
		}
		
		return ceps;
	}
	
	private URI getStreetSearchUri(@NonNull String stateInitials, @NonNull String city, @NonNull String street) {
		StringBuilder url = new StringBuilder();
		url.append(baseUrl);
		url.append(stateInitials);
		url.append("/");
		url.append(city);
		url.append("/");
		url.append(street);
		url.append(defaultResponseMode);
		
		try {
			return new URI(url.toString());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
}
