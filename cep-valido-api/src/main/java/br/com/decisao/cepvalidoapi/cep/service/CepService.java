package br.com.decisao.cepvalidoapi.cep.service;

import br.com.decisao.cepvalidoapi.cep.client.CepClient;
import br.com.decisao.cepvalidoapi.cep.dto.CepValidoResponse;
import br.com.decisao.cepvalidoapi.config.JsonUtil;
import br.com.decisao.cepvalidoapi.config.exception.ValidacaoException;
import br.com.decisao.cepvalidoapi.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.lang.String.format;

@Service
public class CepService {

    @Autowired
    private CepClient cepClient;

    @Autowired
    private LogService logService;

    @Autowired
    private JsonUtil jsonUtil;

    public CepValidoResponse verificarCepValido(String cep) {
        try {
            logService.logData(format("Recebendo chamada ao endpoint de validação de CEP %s", cep));
            var response = cepClient
                .buscarViaCep(cep)
                .orElseThrow(() -> new ValidacaoException("O CEP não foi encontrado na API do VIA CEP."));
            var cepValido = new CepValidoResponse(cep, !response.isErro());
            logService.logData(format("Resposta da validação do CEP %s: %s.", cep, jsonUtil.toJson(cepValido)));
            return cepValido;
        } catch (Exception ex) {
            logService.logData(format("Erro ao tentar validar cep na API do VIA CEP com o CEP %s.", cep), ex);
            return new CepValidoResponse(cep, false);
        }
    }
}
