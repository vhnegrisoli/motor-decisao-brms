package br.com.decisao.cpflimpoapi.cpf.service;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.decisao.cpflimpoapi.config.JsonUtil;
import br.com.decisao.cpflimpoapi.config.exception.ValidacaoException;
import br.com.decisao.cpflimpoapi.cpf.dto.CpfLimpoResponse;
import br.com.decisao.cpflimpoapi.cpf.dto.CpfValidoResponse;
import br.com.decisao.cpflimpoapi.cpf.repository.CpfLimpoRepository;
import br.com.decisao.cpflimpoapi.log.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import static java.lang.String.format;

@Slf4j
@Service
public class CpfService {

    @Autowired
    private JsonUtil jsonUtil;

    @Autowired
    private CpfLimpoRepository cpfLimpoRepository;

    @Autowired
    private LogService logService;

    public CpfValidoResponse verificarCpfValido(String cpf) {
        try {
            logService.logData(format("Realizando chamada ao endpoint de verificar CPF válido para o CPF %s.", cpf));
            cpf = formatarCpf(cpf);
            new CPFValidator().assertValid(cpf);
            var response = new CpfValidoResponse(cpf, true);
            logService.logData(format("Resposta para o CPF %s: %s.", cpf, jsonUtil.toJson(response)));
            return response;
        } catch (Exception ex) {
            logService.logData(format("O CPF %s está inválido.", cpf), ex);
            return new CpfValidoResponse(cpf, false);
        }
    }

    public CpfLimpoResponse verificarCpfLimpo(String cpf) {
        try {
            logService.logData(format("Realizando chamada ao endpoint de verificar CPF limpo para o CPF %s.", cpf));
            cpf = formatarCpf(cpf);
            var response = buscarPorCpf(cpf);
            logService.logData(format("Resposta da consulta ao CPF %s: %s.", cpf, jsonUtil.toJson(response)));
            return response;
        } catch (Exception ex) {
            logService.logData(format("Não foi possível verificar o CPF %s.", cpf), ex);
            return new CpfLimpoResponse(cpf, false);
        }
    }

    private CpfLimpoResponse buscarPorCpf(String cpf) {
        return CpfLimpoResponse.converterDe(cpfLimpoRepository
            .findByCpf(cpf)
            .orElseThrow(() -> new ValidacaoException("Não foram encontrados dados para este CPF.")));
    }

    private String formatarCpf(String cpf) {
        if (!ObjectUtils.isEmpty(cpf)) {
            cpf = cpf.replaceAll("\\.", "");
            cpf = cpf.replace("-", "");
        }
        return cpf;
    }
}
