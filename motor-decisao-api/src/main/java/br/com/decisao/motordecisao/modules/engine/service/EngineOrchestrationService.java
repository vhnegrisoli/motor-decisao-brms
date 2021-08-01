package br.com.decisao.motordecisao.modules.engine.service;

import br.com.decisao.motordecisao.config.LogDataService;
import br.com.decisao.motordecisao.config.TransactionData;
import br.com.decisao.motordecisao.config.exception.ValidacaoException;
import br.com.decisao.motordecisao.config.rule.AvailableRules;
import br.com.decisao.motordecisao.config.rule.RuleId;
import br.com.decisao.motordecisao.modules.data.dto.PayloadProduct;
import br.com.decisao.motordecisao.modules.data.dto.PayloadRequest;
import br.com.decisao.motordecisao.modules.data.dto.Rule;
import br.com.decisao.motordecisao.modules.engine.document.EngineEvaluation;
import br.com.decisao.motordecisao.modules.engine.repository.EngineEvaluationRepository;
import br.com.decisao.motordecisao.modules.restservices.service.RestCallService;
import br.com.decisao.motordecisao.modules.rules.service.RuleExecutorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static br.com.decisao.motordecisao.config.rule.RuleId.*;

@Slf4j
@Service
public class EngineOrchestrationService {

    private static final String ENTRADA = "Entrada";
    private static final String SAIDA = "Saída";

    @Autowired
    private RuleExecutorService ruleExecutorService;

    @Autowired
    private RestCallService restCallService;

    @Autowired
    private EngineEvaluationRepository engineEvaluationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LogDataService logDataService;

    public EngineEvaluation runEngine(PayloadRequest payload) {
        try {
            logData(payload, ENTRADA);
            var startTime = LocalDateTime.now();
            runProcess(payload);
            var engineEvaluation = EngineEvaluation.convertFrom(payload, startTime);
            var response = engineEvaluationRepository.save(engineEvaluation);
            logData(response, SAIDA);
            return response;
        } catch (Exception ex) {
            var transaction = TransactionData.getTransactionData();
            log.error("Erro ao tentar processar avaliação do motor. TransactionId: {}, ServiceId: {}.",
                transaction.getTransactionId(), transaction.getServiceId(), ex);
            throw new ValidacaoException("Erro ao tentar processar os dados no motor de decisão: "
                    .concat(ex.getMessage()));
        }
    }

    private void runProcess(PayloadRequest payload) {
        payload
            .getProdutos()
            .forEach(produto -> evaluateRules(PayloadProduct.create(payload, produto)));
        payload.removeNullRules();
        payload.defineDisapprovedProducts();
    }

    private void evaluateRules(PayloadProduct payloadProduto) {
        var keepRunning = true;
        var rules = payloadProduto.getProduto().getRegras();
        if (shouldEvaluate(keepRunning, REGRA_AVALIAR_CPF_VALIDO, rules, payloadProduto)) {
            keepRunning = executeAndDefineNext(REGRA_AVALIAR_CPF_VALIDO, payloadProduto);
        }
        if (shouldEvaluate(keepRunning, REGRA_AVALIAR_CPF_LIMPO, rules, payloadProduto)) {
            keepRunning = executeAndDefineNext(REGRA_AVALIAR_CPF_LIMPO, payloadProduto);
        }
        if (shouldEvaluate(keepRunning, REGRA_AVALIAR_IDADE_PERMITIDA, rules, payloadProduto)) {
            keepRunning = executeAndDefineNext(REGRA_AVALIAR_IDADE_PERMITIDA, payloadProduto);
        }
    }

    private boolean executeAndDefineNext(RuleId ruleId,
                                         PayloadProduct payloadProduto) {
        var rule = ruleExecutorService.executeRule(ruleId, payloadProduto);
        checkPendingRule(rule, payloadProduto, ruleId);
        addRule(rule, payloadProduto);
        return rule.isApproved();
    }

    private void checkPendingRule(Rule rule, PayloadProduct payloadProduto, RuleId ruleId) {
        if (rule.isApiPending()) {
            restCallService.callPendingApi(rule.getApiPendente(), payloadProduto);
            rule = ruleExecutorService.executeRule(ruleId, payloadProduto);
            addRule(rule, payloadProduto);
        }
    }

    private void addRule(Rule rule, PayloadProduct payloadProduto) {
        payloadProduto.getProduto().getRegras().add(rule);
    }

    private boolean shouldEvaluate(boolean keepRunning,
                                   RuleId ruleId,
                                   List<Rule> rules,
                                   PayloadProduct payloadProduto) {
        return keepRunning
            && AvailableRules.isRuleAvailable(ruleId, payloadProduto.getProduto().getId())
            && !Rule.isAlreadyEvaluatedRule(ruleId, rules);
    }

    public EngineEvaluation findById(String id) {
        return engineEvaluationRepository
            .findById(id)
            .orElseThrow(() -> new ValidacaoException("Não foi encontrada uma avaliação para este ID."));
    }

    public EngineEvaluation findByEvaluationId(String evaluationId) {
        return engineEvaluationRepository
            .findByEngineId(evaluationId)
            .orElseThrow(() -> new ValidacaoException("Não foi encontrada uma avaliação para este ID."));
    }

    private void logData(Object payload,
                         String step) {
        try {
            var payloadStr = objectMapper.writeValueAsString(payload);
            logDataService.logData(
                String.format("%s do endpoint de rodar o moto com transactionId {}, seriviceId: {} e dados: {}", step),
                payloadStr
            );
        } catch (Exception ex) {
            log.error("Erro ao tentar processar JSON de entrada: ", ex);
        }
    }
}
