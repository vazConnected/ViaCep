package vazconnected.ViaCep.dto.streetSearch;

import java.util.List;

import vazconnected.ViaCep.Cep;

public record StreetSearchSuccessOutput(List<Cep> ceps) implements StreetSearchOutput {

}
