package vazconnected.ViaCep.dto;

import vazconnected.ViaCep.dto.cepSearch.CepSearchOutputDto;
import vazconnected.ViaCep.dto.streetSearch.StreetSearchOutput;

public record ErrorOutputDto(String errorType, String errorMessage) implements CepSearchOutputDto, StreetSearchOutput {

}
