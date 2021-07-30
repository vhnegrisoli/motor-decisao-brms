package br.com.decisao.motordecisao.modules.engine.service;

import br.com.decisao.motordecisao.config.exception.ValidacaoException;
import br.com.decisao.motordecisao.config.rule.RuleDefinition;
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

    @Autowired
    private RuleExecutorService ruleExecutorService;

    @Autowired
    private RestCallService restCallService;

    @Autowired
    private EngineEvaluationRepository engineEvaluationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public EngineEvaluation runEngine(PayloadRequest payload) {
        try {
            logRequest(payload);
            var startTime = LocalDateTime.now();
            runProcess(payload);
            var engineEvaluation = EngineEvaluation.convertFrom(payload, startTime);
            var response = engineEvaluationRepository.save(engineEvaluation);
            logResponse(response);
            return response;
        } catch (Exception ex) {
            throw new ValidacaoException(
                "Erro ao tentar processar os dados no motor de decisão: "
                    .concat(ex.getMessage())
            );
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
            && RuleDefinition.isRuleAvailable(ruleId, payloadProduto.getProduto().getId())
            && !Rule.isAlreadyEvaluatedRule(ruleId, rules);
    }

    private void logRequest(PayloadRequest payload) {
        try {
            log.info("Início da chamada ao endpoint de rodar o moto com payload: {}",
                objectMapper.writeValueAsString(payload));
        } catch (Exception ex) {
            log.error("Erro ao tentar processar JSON de entrada: ", ex);
        }
    }

    private void logResponse(EngineEvaluation engineEvaluation) {
        try {
            log.info("Payload de resposta da chamada ao endpoint de rodar o moto: {}",
                objectMapper.writeValueAsString(engineEvaluation));
        } catch (Exception ex) {
            log.error("Erro ao tentar processar JSON de saída: ", ex);
        }
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
}
