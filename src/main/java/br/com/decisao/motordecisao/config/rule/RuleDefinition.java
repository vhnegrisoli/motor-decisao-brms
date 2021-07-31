package br.com.decisao.motordecisao.config.rule;

import br.com.decisao.motordecisao.modules.data.enums.ProductId;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

import static br.com.decisao.motordecisao.config.rule.RuleAvailability.DESABILITADA;
import static br.com.decisao.motordecisao.config.rule.RuleAvailability.HABILITADA;
import static br.com.decisao.motordecisao.config.rule.RuleExecution.define;
import static br.com.decisao.motordecisao.config.rule.RuleId.*;
import static br.com.decisao.motordecisao.modules.data.enums.ProductId.ALUGUEL;
import static br.com.decisao.motordecisao.modules.data.enums.ProductId.COMPRA;

@Getter
@AllArgsConstructor
public class RuleDefinition {

    public static List<RuleExecution> getRules() {
        return List.of(
            define(ALUGUEL, REGRA_AVALIAR_CPF_VALIDO, HABILITADA),
            define(COMPRA, REGRA_AVALIAR_CPF_VALIDO, HABILITADA),
            define(ALUGUEL, REGRA_AVALIAR_CPF_LIMPO, DESABILITADA),
            define(ALUGUEL, REGRA_AVALIAR_IDADE_PERMITIDA, HABILITADA)
        );
    }

    public static boolean isRuleAvailable(RuleId ruleId, ProductId productId) {
        return getRules()
            .stream()
            .anyMatch(ruleExecution -> ruleId.name().equals(ruleExecution.getRuleId())
                && ruleExecution.isAvailable()
                && productId.name().equals(ruleExecution.getProductId()));
    }
}
