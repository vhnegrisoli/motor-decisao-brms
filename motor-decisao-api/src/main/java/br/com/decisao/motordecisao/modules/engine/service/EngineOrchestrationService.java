package br.com.decisao.motordecisao.modules.engine.service;

import br.com.decisao.motordecisao.config.JsonUtil;
import br.com.decisao.motordecisao.config.exception.ValidacaoException;
import br.com.decisao.motordecisao.config.rule.AvailableRules;
import br.com.decisao.motordecisao.config.rule.RuleId;
import br.com.decisao.motordecisao.log.service.LogService;
import br.com.decisao.motordecisao.modules.data.dto.EngineProduct;
import br.com.decisao.motordecisao.modules.data.dto.PayloadProduct;
import br.com.decisao.motordecisao.modules.data.dto.PayloadRequest;
import br.com.decisao.motordecisao.modules.data.dto.Rule;
import br.com.decisao.motordecisao.modules.engine.document.EngineEvaluation;
import br.com.decisao.motordecisao.modules.engine.repository.EngineEvaluationRepository;
import br.com.decisao.motordecisao.modules.restservices.service.RestCallService;
import br.com.decisao.motordecisao.modules.rules.service.RuleExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static br.com.decisao.motordecisao.config.rule.RuleId.*;
import static java.lang.String.format;

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
    private JsonUtil jsonUtil;

    @Autowired
    private LogService logService;

    public EngineEvaluation runEngine(PayloadRequest payload) {
        try {
            logService.logData(format("Chamando endpoint de avaliação ao motor com dados: %s.", jsonUtil.toJson(payload)));
            var startTime = LocalDateTime.now();
            runProcess(payload);
            var engineEvaluation = EngineEvaluation.convertFrom(payload, startTime);
            var response = engineEvaluationRepository.save(engineEvaluation);
            logService.logData(format("Resposta da avaliação ao motor com dados: %s.", jsonUtil.toJson(response)));
            return response;
        } catch (Exception ex) {
            logService.logData("Erro ao tentar processar avaliação do motor.", ex);
            throw new ValidacaoException(format("Erro ao tentar processar os dados no motor de decisão: %s", ex.getMessage()));
        }
    }

    private void runProcess(PayloadRequest payload) {
        payload
            .getProdutos()
            .forEach(produto -> evaluateRules(payload, produto));
        payload.removeNullRules();
        payload.defineDisapprovedProducts();
    }

    private void evaluateRules(PayloadRequest payload, EngineProduct produto) {
        var payloadProduto = PayloadProduct.create(payload, produto);
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
        if (shouldEvaluate(keepRunning, REGRA_AVALIAR_CEP_VALIDO, rules, payloadProduto)) {
            executeAndDefineNext(REGRA_AVALIAR_CEP_VALIDO, payloadProduto);
        }
        payload.setApisConsultadas(payloadProduto.getPayload().getApisConsultadas());
        payload.setDadosApis(payloadProduto.getPayload().getDadosApis());
    }

    private boolean executeAndDefineNext(RuleId ruleId,
                                         PayloadProduct payloadProduto) {
        var rule = ruleExecutorService.executeRule(ruleId, payloadProduto);
        rule = checkPendingRule(rule, payloadProduto, ruleId);
        addRule(rule, payloadProduto);
        return rule.isApproved();
    }

    private Rule checkPendingRule(Rule rule, PayloadProduct payloadProduto, RuleId ruleId) {
        if (rule.isApiPending()) {
            restCallService.callPendingApi(rule.getApiPendente(), payloadProduto);
            return ruleExecutorService.executeRule(ruleId, payloadProduto);
        }
        return rule;
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
        try {
            logService.logData(format("Iniciando consulta de avaliação por ID %s.", id));
            var response = findByMongoId(id);
            logService.logData(format("Resposta da consulta de avaliação por ID %s: %s.", id, jsonUtil.toJson(response)));
            return response;
        } catch (Exception ex) {
            logService.logData(format("Não foi encontrada uma avaliação pelo ID %s.", id), ex);
            throw ex;
        }
    }

    public EngineEvaluation findByEvaluationId(String evaluationId) {
        try {
            logService.logData(format("Iniciando consulta de avaliação por ID do motor: %s.", evaluationId));
            var response = findByEngineId(evaluationId);
            logService.logData(format("Resposta da consulta de avaliação por ID do motor %s: %s.", evaluationId, jsonUtil.toJson(response)));
            return response;
        } catch (Exception ex) {
            logService.logData(format("Não foi encontrada um ID de avaliação pelo ID %s.", evaluationId), ex);
            throw ex;
        }
    }

    private EngineEvaluation findByMongoId(String id) {
        return engineEvaluationRepository
            .findById(id)
            .orElseThrow(() -> new ValidacaoException("Não foi encontrada uma avaliação para este ID."));
    }

    private EngineEvaluation findByEngineId(String id) {
        return engineEvaluationRepository
            .findByEngineId(id)
            .orElseThrow(() -> new ValidacaoException("Não foi encontrada uma avaliação para este ID."));
    }
}
