package vazconnected.ViaCep;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Cep {
	private static Map<String, Cep> cepsCache = new HashMap<String, Cep>();
	@Getter @NonNull private String cep;
	@Getter private String logradouro;
	@Getter private String complemento;
	@Getter private String unidade;
	@Getter private String bairro;
	@Getter private String localidade;
	@Getter private String uf;
	@Getter private String ibge;
	@Getter private String gia;
	@Getter private String ddd;
	@Getter private String siafi;
	
	@Override
	public boolean equals(@NonNull Object obj) {
		if (!(obj instanceof Cep)) return false;

		Cep cep = ((Cep) obj);
		return this.cep.equals(cep.cep);
	}
	
	@Override
	public int hashCode() {
		return this.cep.hashCode();
	}

	public static boolean saveCepInCache(@NonNull Cep cep) {
		return (Boolean) Cep.cepsCacheOperations("save", cep);
	}
	
	public static Cep searchCepInCache(@NonNull String cep) {
		return (Cep) Cep.cepsCacheOperations("search", cep);
	}
	
	public static boolean deleteCepFromCache(@NonNull String cep) {
		return (Boolean) Cep.cepsCacheOperations("delete", cep);
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Cep> getCepsCache() {
		return (Map<String, Cep>) Cep.cepsCacheOperations("get", null);
	}
	
	private static synchronized Object cepsCacheOperations(@NonNull String operation, Object parameter) {
		switch (operation) {
		case("save"):
			return Cep.cepsCache.put(((Cep) parameter).cep, (Cep) parameter) == null;
		case("search"):
			return cepsCache.get((String) parameter);
		case("delete"):
			return cepsCache.remove(((Cep) parameter).cep) != null;
		case("get"):
			return new HashMap<String, Cep>(Cep.cepsCache);
		default:
			throw new RuntimeException("Operacao invalida ao manipular cache de Ceps. Operacao solicitada: " + operation);
		}
	}
	
	public static boolean isValid(String cep) {
		Pattern pattern = Pattern.compile("^[0-9]{8}$");
        return pattern.matcher(cep).matches();
	}
	
	public static String formatCep(String cep) {
        Pattern pattern = Pattern.compile("\\d");
        StringBuilder builder = new StringBuilder();

        for (char c : cep.toCharArray()) {
            if (pattern.matcher(Character.toString(c)).matches()) {
                builder.append(c);
            }
        }
        
        if (builder.length() != 8) {
        	throw new InvalidParameterException("O Cep nao contem a quantidade de digitos necessaria.");
        }

        return builder.toString();
	}
	
}
