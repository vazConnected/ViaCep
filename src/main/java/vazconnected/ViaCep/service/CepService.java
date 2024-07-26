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

@Service
public class CepService {
	public static final String baseUrl = "https://viacep.com.br/ws/";
	public static final String defaultResponseMode = "/json/";
	
	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	private URI getCepUri(String cepInput) {		
		try {
			return new URI(baseUrl + cepInput + defaultResponseMode);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	public Cep getCepData(String cepInput) {
		if (!Cep.isValid(cepInput)) {
			throw new IllegalArgumentException("O cep não está formatado corretamente");
		} else {
			cepInput = Cep.formatCep(cepInput);
		}
		
		Cep cepFromCache = Cep.searchCepInCache(cepInput);
		if (cepFromCache != null) {
			return cepFromCache;
		}
		
        Cep cepFromViaCep = this.getFromViaCepApi(cepInput);
        return cepFromViaCep;
	}
	
	private Cep getFromViaCepApi(String cepInput) {
		HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri( this.getCepUri(cepInput) ).build();

		String response = null;
        try {
            System.out.println("Enviando requisição ao ViaCep...");
            response = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException exception) {
            throw new RuntimeException("Não foi possível obter as informações dos servidores ViaCep. Tente novamente mais tarde.");
        } catch (InterruptedException exception) {
        	throw new RuntimeException("Erro de interrupção da Thread de requisição: " + exception.getMessage());
        }
        
        if (gson.fromJson(response, JsonObject.class).get("erro") != null) {
            throw new RuntimeException("O cep informado não consta na base de dados da ViaCep.");
        }
        
        Cep cep = gson.fromJson(response, Cep.class);
        cep.setFromCache(false);
        
        Cep.saveCepInCache((Cep) cep.clone());
        
        return cep;
	}

	public boolean deleteCepFromCache(String cep) {
		return Cep.deleteCepFromCache(cep);
	}
}
