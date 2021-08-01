package br.com.decisao.motordecisao.config.rule;

import br.com.decisao.motordecisao.modules.data.enums.ProductId;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RuleDefinition {

    private String ruleId;
    private String productId;
    private boolean available;

    public static RuleDefinition define(ProductId productId, RuleId ruleId, RuleAvailability availability) {
        return new RuleDefinition(ruleId.name(), productId.name(), availability.getRuleAvailability());
    }
}
