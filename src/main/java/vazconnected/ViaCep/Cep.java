package vazconnected.ViaCep;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
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
	@Getter @Setter private boolean fromCache;
	
	public Cep(@NonNull String cep, String logradouro, String complemento, String unidade, String bairro,
			String localidade, String uf, String ibge, String gia, String ddd, String siafi) {
		this.fromCache = false;
		this.cep = Cep.formatCep(cep);
		this.logradouro = logradouro;
		this.complemento = complemento;
		this.unidade = unidade;
		this.bairro = bairro;
		this.localidade = localidade;
		this.uf = uf;
		this.ibge = ibge;
		this.gia = gia;
		this.ddd = ddd;
		this.siafi = siafi;
	}
	
	@Override
	 public Object clone() {
		return new Cep(cep, logradouro, complemento, unidade, bairro,
				localidade, uf, ibge, gia, ddd, siafi, fromCache);
	 }
	
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
		case("save"): {
			Cep cep = (Cep) parameter;
			cep.setFromCache(true);
			return Cep.cepsCache.put(cep.getCep(), cep) == null;
		}
		case("search"): {
			String key = (String) parameter;
			return cepsCache.get(key);
		}
		case("delete"):{
			String key = (String) parameter;
			return cepsCache.remove(key) != null;
		}
		case("get"): {
			return new HashMap<String, Cep>(Cep.cepsCache);
		}
		default:
			throw new RuntimeException("Operacao invalida ao manipular cache de Ceps. Operacao solicitada: " + operation);
		}
	}
	
	public static boolean isValid(String cep) {
		Pattern onlyNumbersPattern = Pattern.compile("^[0-9]{8}$");
		Pattern cepPattern = Pattern.compile("^[0-9]{5}-[0-9]{3}$");
        return onlyNumbersPattern.matcher(cep).matches() || cepPattern.matcher(cep).matches();
	}
	
	public static String formatCep(String cep) {
		Pattern pattern = Pattern.compile("^[0-9]{5}-[0-9]{3}$");
		if (pattern.matcher(cep).matches()) {
			return cep;
		}
		
		if (cep.length() != 8) {
        	throw new InvalidParameterException("O Cep nao contem a quantidade de digitos necessaria.");
        }
		
		StringBuilder formatedCep = new StringBuilder();

		formatedCep.append(cep.substring(0, 4));
		formatedCep.append('-');
		formatedCep.append(cep.substring(4));
        
        return formatedCep.toString();
	}
}
