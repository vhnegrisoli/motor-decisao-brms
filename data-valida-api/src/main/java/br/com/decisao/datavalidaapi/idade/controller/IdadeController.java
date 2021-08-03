package br.com.decisao.datavalidaapi.idade.controller;

import br.com.decisao.datavalidaapi.idade.dto.IdadeResponse;
import br.com.decisao.datavalidaapi.idade.service.IdadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/idade")
public class IdadeController {

    @Autowired
    private IdadeService idadeService;

    @GetMapping("{dataNascimento}")
    public IdadeResponse calcularDataNascimento(@PathVariable String dataNascimento,
                                                @RequestHeader String transactionId) {
        return idadeService.calcularIdade(dataNascimento);
    }
}
