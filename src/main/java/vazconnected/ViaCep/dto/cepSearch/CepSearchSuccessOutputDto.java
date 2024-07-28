package vazconnected.ViaCep.dto.cepSearch;

import vazconnected.ViaCep.Cep;

public record CepSearchSuccessOutputDto(Cep cep, boolean fromCache) implements CepSearchOutputDto {

}
