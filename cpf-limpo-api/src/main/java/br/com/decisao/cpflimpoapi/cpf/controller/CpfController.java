package br.com.decisao.cpflimpoapi.cpf.controller;

import br.com.decisao.cpflimpoapi.cpf.dto.CpfLimpoResponse;
import br.com.decisao.cpflimpoapi.cpf.dto.CpfValidoResponse;
import br.com.decisao.cpflimpoapi.cpf.service.CpfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cpf")
public class CpfController {

    @Autowired
    private CpfService cpfService;

    @GetMapping("{cpf}/valido")
    public CpfValidoResponse definirCpfValido(@PathVariable String cpf,
                                              @RequestHeader String transactionId) {
        return cpfService.verificarCpfValido(cpf);
    }

    @GetMapping("{cpf}/limpo")
    public CpfLimpoResponse verificarCpfLimpo(@PathVariable String cpf,
                                              @RequestHeader String transactionId) {
        return cpfService.verificarCpfLimpo(cpf);
    }
}
