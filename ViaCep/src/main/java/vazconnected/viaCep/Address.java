package vazconnected.viaCep;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public record Address(
        String cep,
        String logradouro,
        String complemento,
        String bairro,
        String localidade,
        String uf,
        String ibge,
        String gia,
        String ddd,
        String  siafi
) {
    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
