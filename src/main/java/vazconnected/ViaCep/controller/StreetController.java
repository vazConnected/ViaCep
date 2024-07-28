package vazconnected.ViaCep.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vazconnected.ViaCep.Cep;
import vazconnected.ViaCep.dto.ErrorOutputDto;
import vazconnected.ViaCep.dto.streetSearch.StreetSearchInput;
import vazconnected.ViaCep.dto.streetSearch.StreetSearchOutput;
import vazconnected.ViaCep.dto.streetSearch.StreetSearchSuccessOutput;
import vazconnected.ViaCep.service.StreetService;

@RestController
@RequestMapping("/rua")
public class StreetController {
	private StreetService streetService = new StreetService();

	@GetMapping
	public ResponseEntity<StreetSearchOutput> searchStreet(
			@RequestBody StreetSearchInput input) {

		String estado = input.estado().toUpperCase();
		String cidade = input.cidade().replace(" ", "%20");
		String rua = input.rua().replace(" ", "%20");

		List<Cep> ceps = null;
		try {
			ceps = streetService.searchStreet(estado, cidade, rua);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ErrorOutputDto(e.getClass().getCanonicalName(), e.getLocalizedMessage()));
		}

		return ResponseEntity.status(HttpStatus.OK).body(new StreetSearchSuccessOutput(ceps));
	}

}
