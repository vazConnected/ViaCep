package vazconnected.ViaCep.service;

import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import vazconnected.ViaCep.Cep;

@Service
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CepService {

	public static Cep getCepData(String cepId) {
		if (!Cep.isValid(cepId)) {
			cepId = Cep.formatCep(cepId);
		}
		
		Cep cep = Cep.searchCepInCache(cepId);
		if (cep == null) {
			// TODO: consultar API ViaCep
		}
		
		return cep;
	}
}
