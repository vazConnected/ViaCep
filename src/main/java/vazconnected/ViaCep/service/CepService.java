package vazconnected.ViaCep.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import vazconnected.ViaCep.Cep;
import vazconnected.ViaCep.dto.ErrorOutputDto;
import vazconnected.ViaCep.dto.cepSearch.CepSearchOutputDto;
import vazconnected.ViaCep.dto.cepSearch.CepSearchSuccessOutputDto;

@Service
public class CepService {
	private static final String baseUrl = "https://viacep.com.br/ws/";
	private static final String defaultResponseMode = "/json/";

	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	private URI getCepUri(String cepInput) {
		try {
			return new URI(baseUrl + cepInput + defaultResponseMode);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public CepSearchOutputDto getCepData(String cepInput) {
		if (!Cep.isValid(cepInput)) {
			throw new IllegalArgumentException("O cep não está formatado corretamente");
		} else {
			cepInput = Cep.formatCep(cepInput);
		}

		Cep cepFromCache = Cep.searchCepInCache(cepInput);
		if (cepFromCache != null) {
			return new CepSearchSuccessOutputDto(cepFromCache, true);
		}

		Cep cepFromViaCep = null;
		try {
			cepFromViaCep = this.getFromViaCepApi(cepInput);
		} catch (Exception e) {
			return new ErrorOutputDto(e.getClass().getCanonicalName(), e.getLocalizedMessage());
		}

		return new CepSearchSuccessOutputDto(cepFromViaCep, false);
	}

	public boolean deleteCepFromCache(String cep) {
		return Cep.deleteCepFromCache(cep);
	}
	
	private Cep getFromViaCepApi(String cepInput) throws IOException, InterruptedException {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(this.getCepUri(cepInput)).build();

		System.out.println("Enviando requisição ao ViaCep. Pesquisando por Cep...");
		String response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
		
		JsonObject responseJson = gson.fromJson(response, JsonObject.class);
		if (responseJson.has("erro")) {
			String errorMessage = responseJson.get("erro").getAsString();
			throw new RuntimeException("Erro da API ViaCEP: " + errorMessage);
		}

		Cep cep = gson.fromJson(response, Cep.class);
		Cep.saveCepInCache((Cep) cep.clone());

		return cep;
	}	
}
