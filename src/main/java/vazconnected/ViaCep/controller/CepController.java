package vazconnected.ViaCep.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import vazconnected.ViaCep.Cep;
import vazconnected.ViaCep.service.CepService;

@RestController
@RequestMapping("/cep")
public class CepController {

    @GetMapping("/{cep}")
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public Cep getCepData(@PathVariable String cep) {
        return CepService.getCepData(cep);
    }
    
    @DeleteMapping("/{cep}")
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public void deleteCepDataFromCache(@PathVariable("cep") String cep) {
        System.out.println(cep);
    }
}
