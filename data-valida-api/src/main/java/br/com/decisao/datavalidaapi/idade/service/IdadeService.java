package br.com.decisao.datavalidaapi.idade.service;

import br.com.decisao.datavalidaapi.config.JsonUtil;
import br.com.decisao.datavalidaapi.config.exception.ValidacaoException;
import br.com.decisao.datavalidaapi.idade.dto.IdadeResponse;
import br.com.decisao.datavalidaapi.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static java.lang.String.format;

@Service
public class IdadeService {

    @Autowired
    private LogService logService;

    @Autowired
    private JsonUtil jsonUtil;

    public IdadeResponse calcularIdade(String dataNascimento) {
        try {
            var data = LocalDate.parse(dataNascimento);
            logService.logData(format("Recebendo chamada ao endpoint de calcular a data de nascimento: %s", dataNascimento));
            if (ObjectUtils.isEmpty(dataNascimento)) {
                throw new ValidacaoException("A data de nascimento é nula.");
            }
            var dataAtual = LocalDate.now();
            var idade = (int) ChronoUnit.YEARS.between(data, dataAtual);
            var response = new IdadeResponse(idade);
            logService.logData(format("Resposta do cálculo de idade para a data de nascimento %s: %s.", dataNascimento, jsonUtil.toJson(response)));
            return response;
        } catch (Exception ex) {
            logService.logData("Erro ao tentar calcular data de nascimento.", ex);
            throw new ValidacaoException("Erro ao tentar calcular a idade.");
        }
    }
}