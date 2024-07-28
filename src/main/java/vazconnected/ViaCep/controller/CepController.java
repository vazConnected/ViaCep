package vazconnected.ViaCep.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vazconnected.ViaCep.Cep;
import vazconnected.ViaCep.dto.cepDeletion.CepDeletionOutput;
import vazconnected.ViaCep.dto.cepSearch.CepSearchOutputDto;
import vazconnected.ViaCep.dto.cepSearch.CepSearchSuccessOutputDto;
import vazconnected.ViaCep.service.CepService;

@RestController
@RequestMapping("/cep")
public class CepController {
	private static final CepService cepService = new CepService();

    @GetMapping("/{cep}")
    public ResponseEntity<CepSearchOutputDto> getCepData(@PathVariable String cep) {
    	System.out.println("Busca por: " + cep);
    	if (!Cep.isValid(cep)) {
			cep = Cep.formatCep(cep);
		}
    	
    	CepSearchOutputDto output = cepService.getCepData(cep);
    	if (output instanceof CepSearchSuccessOutputDto) {
    		return ResponseEntity.status(HttpStatus.OK).body(output);
    	} else {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(output);
    	}
    }
    
    @DeleteMapping("/{cep}")
    public ResponseEntity<CepDeletionOutput> deleteCepDataFromCache(@PathVariable("cep") String cep) {
    	CepDeletionOutput output = new CepDeletionOutput(cepService.deleteCepFromCache(cep));
    	
    	if (output.deleted()) {
    		return ResponseEntity.status(HttpStatus.OK).body(output);
    	} else {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(output);
    	}
    }
}
