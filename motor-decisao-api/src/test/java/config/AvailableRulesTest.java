package config;

import br.com.decisao.motordecisao.config.rule.AvailableRules;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static br.com.decisao.motordecisao.config.rule.RuleId.REGRA_AVALIAR_CPF_LIMPO;
import static br.com.decisao.motordecisao.config.rule.RuleId.REGRA_AVALIAR_CPF_VALIDO;
import static br.com.decisao.motordecisao.modules.data.enums.ProductId.ALUGUEL;
import static br.com.decisao.motordecisao.modules.data.enums.ProductId.HIBRIDO;
import static org.assertj.core.api.Assertions.assertThat;

public class AvailableRulesTest {

    @Test
    @DisplayName("Should return true when informing an available rule for product")
    public void shouldReturnTrueWhenInformingAvailableRuleForProduct() {
        assertThat(AvailableRules.isRuleAvailable(REGRA_AVALIAR_CPF_VALIDO, ALUGUEL)).isTrue();
    }

    @Test
    @DisplayName("Should return false when informing an unavailable rule for product")
    public void shouldReturnFalseWhenInformingUnavailableRuleForProduct() {
        assertThat(AvailableRules.isRuleAvailable(REGRA_AVALIAR_CPF_LIMPO, ALUGUEL)).isTrue();
    }

    @Test
    @DisplayName("Should return false when informing a rule not mapped for product")
    public void shouldReturnFalseWhenInformingRuleThatIsNotForProduct() {
        assertThat(AvailableRules.isRuleAvailable(REGRA_AVALIAR_CPF_LIMPO, HIBRIDO)).isFalse();
    }
}
