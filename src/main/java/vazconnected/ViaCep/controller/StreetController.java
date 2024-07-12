package vazconnected.ViaCep.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import vazconnected.ViaCep.Cep;

@RestController
@RequestMapping("/rua")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StreetController {
	@GetMapping
	@ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
	public static Cep searchStreet(@RequestBody @NonNull String rua, @RequestBody @NonNull String estado) {
		// TODO: pesquisar por rua
		return null;
	}
}
