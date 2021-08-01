package br.com.decisao.cpflimpoapi.cpf.service;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.decisao.cpflimpoapi.config.TransactionData;
import br.com.decisao.cpflimpoapi.config.exception.ValidacaoException;
import br.com.decisao.cpflimpoapi.cpf.dto.CpfLimpoResponse;
import br.com.decisao.cpflimpoapi.cpf.dto.CpfValidoResponse;
import br.com.decisao.cpflimpoapi.cpf.repository.CpfLimpoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CpfService {

    @Autowired
    private CpfLimpoRepository cpfLimpoRepository;

    public CpfValidoResponse verificarCpfValido(String cpf) {
        try {
            new CPFValidator().assertValid(cpf);
            return new CpfValidoResponse(cpf, true);
        } catch (Exception ex) {
            var transaction = TransactionData.getTransactionData();
            log.error("O CPF {} está inválido. TransactionId: {}, ServiceId: {}.",
                transaction.getTransactionId(), transaction.getServiceId(), ex);
            return new CpfValidoResponse(cpf, false);
        }
    }

    public CpfLimpoResponse verificarCpfLimpo(String cpf) {
        try {
            return CpfLimpoResponse.converterDe(cpfLimpoRepository
                .findByCpf(cpf)
                .orElseThrow(() -> new ValidacaoException("O CPF não foi encontrado.")));
        } catch (Exception ex) {
            var transaction = TransactionData.getTransactionData();
            log.error("Não foi possível verificar o CPF {}. TransactionId: {}, ServiceId: {}.",
                cpf, transaction.getTransactionId(), transaction.getServiceId(), ex);
            throw ex;
        }
    }
}
