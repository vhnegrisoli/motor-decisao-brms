package br.com.decisao.motordecisao.modules.engine.service;

import br.com.decisao.motordecisao.config.exception.ValidacaoException;
import br.com.decisao.motordecisao.config.rule.RuleDefinition;
import br.com.decisao.motordecisao.config.rule.RuleId;
import br.com.decisao.motordecisao.modules.data.dto.PayloadProduct;
import br.com.decisao.motordecisao.modules.data.dto.PayloadRequest;
import br.com.decisao.motordecisao.modules.data.dto.Rule;
import br.com.decisao.motordecisao.modules.restservices.service.RestCallService;
import br.com.decisao.motordecisao.modules.rules.service.RuleExecutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EngineOrchestrationService {

    @Autowired
    private RuleExecutorService ruleExecutorService;

    @Autowired
    private RestCallService restCallService;

    public PayloadRequest runEngine(PayloadRequest payload) {
        try {
            payload
                .getProdutos()
                .forEach(produto -> evaluateRules(PayloadProduct.create(payload, produto)));
            payload.removeNullRules();
            return payload;
        } catch (Exception ex) {
            throw new ValidacaoException(
                "Erro ao tentar processar os dados no motor de decisão: "
                    .concat(ex.getMessage())
            );
        }
    }

    private void evaluateRules(PayloadProduct payloadProduto) {
        var keepRunning = true;
        var rules = payloadProduto.getProduto().getRegras();
        if (shouldEvaluate(keepRunning, RuleId.REGRA_AVALIAR_CPF_VALIDO, rules, payloadProduto)) {
            keepRunning = executeAndDefineNext(RuleId.REGRA_AVALIAR_CPF_VALIDO, payloadProduto);
        }
        if (shouldEvaluate(keepRunning, RuleId.REGRA_AVALIAR_CPF_LIMPO, rules, payloadProduto)) {
            keepRunning = executeAndDefineNext(RuleId.REGRA_AVALIAR_CPF_LIMPO, payloadProduto);
        }
        if (shouldEvaluate(keepRunning, RuleId.REGRA_AVALIAR_IDADE_PERMITIDA, rules, payloadProduto)) {
            keepRunning = executeAndDefineNext(RuleId.REGRA_AVALIAR_IDADE_PERMITIDA, payloadProduto);
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
}
