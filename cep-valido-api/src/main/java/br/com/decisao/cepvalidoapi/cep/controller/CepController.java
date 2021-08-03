package br.com.decisao.cepvalidoapi.cep.controller;

import br.com.decisao.cepvalidoapi.cep.dto.CepValidoResponse;
import br.com.decisao.cepvalidoapi.cep.service.CepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cep")
public class CepController {

    @Autowired
    private CepService cepService;

    @GetMapping("{cep}")
    public CepValidoResponse verificarCepValido(@PathVariable String cep,
                                                @RequestHeader String transactionId) {
        return cepService.verificarCepValido(cep);
    }
}
