package br.com.decisao.motordecisao.config.rule;

import br.com.decisao.motordecisao.modules.data.enums.ProductId;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

import static br.com.decisao.motordecisao.config.rule.RuleAvailability.DESABILITADA;
import static br.com.decisao.motordecisao.config.rule.RuleAvailability.HABILITADA;
import static br.com.decisao.motordecisao.config.rule.RuleDefinition.define;
import static br.com.decisao.motordecisao.config.rule.RuleId.*;
import static br.com.decisao.motordecisao.modules.data.enums.ProductId.ALUGUEL;
import static br.com.decisao.motordecisao.modules.data.enums.ProductId.COMPRA;

@Getter
@AllArgsConstructor
public class AvailableRules {

    public static List<RuleDefinition> getRules() {
        return List.of(
            define(ALUGUEL, REGRA_AVALIAR_CPF_VALIDO, HABILITADA),
            define(ALUGUEL, REGRA_AVALIAR_CPF_LIMPO, HABILITADA),
            define(ALUGUEL, REGRA_AVALIAR_IDADE_PERMITIDA, HABILITADA),
            define(ALUGUEL, REGRA_AVALIAR_CEP_VALIDO, HABILITADA)
        );
    }

    public static boolean isRuleAvailable(RuleId ruleId, ProductId productId) {
        return getRules()
            .stream()
            .anyMatch(ruleDefinition -> ruleId.name().equals(ruleDefinition.getRuleId())
                && ruleDefinition.isAvailable()
                && productId.name().equals(ruleDefinition.getProductId()));
    }
}
