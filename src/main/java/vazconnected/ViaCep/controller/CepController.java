package vazconnected.ViaCep.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import vazconnected.ViaCep.Cep;
import vazconnected.ViaCep.service.CepService;

@RestController
@RequestMapping("/cep")
public class CepController {
	private static final CepService cepService = new CepService();

    @GetMapping("/{cep}")
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public Cep getCepData(@PathVariable String cep) {
    	System.out.println("Busca por: " + cep);
    	if (!Cep.isValid(cep)) {
			cep = Cep.formatCep(cep);
		}
    	
        return cepService.getCepData(cep);
    }
    
    @DeleteMapping("/{cep}")
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public boolean deleteCepDataFromCache(@PathVariable("cep") String cep) {
        return cepService.deleteCepFromCache(cep);
    }
}
