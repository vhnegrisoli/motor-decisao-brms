package modules.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static modules.mocks.DtoMocks.*;
import static org.assertj.core.api.Assertions.assertThat;

public class EngineProductTest {

    @Test
    @DisplayName("Should return true if product has any disapproved rule")
    public void shouldReturnTrueIfProductHasAnyDisapprovedRule() {
        var rules = List.of(
            getApprovedRule(),
            getApprovedRule(),
            getDisapprovedRule(),
            getPendingApiServiceRule()
        );

        var product = getEngineProduct();
        product.setRegras(rules);

        assertThat(product.isDisapprovedProduct()).isTrue();
    }

    @Test
    @DisplayName("Should return false if product does not have any disapproved rule")
    public void shouldReturnFalseIfProductDoesNotHaveAnyDisapprovedRule() {
        var rules = List.of(
            getApprovedRule(),
            getApprovedRule()
        );

        var product = getEngineProduct();
        product.setRegras(rules);

        assertThat(product.isDisapprovedProduct()).isFalse();
    }

    @Test
    @DisplayName("Should return false if product does not have any evaluated rules")
    public void shouldReturnFalseIfProductDoesNotHaveEvaluatedRules() {
        var product = getEngineProduct();
        System.out.println(product);
        assertThat(product.isDisapprovedProduct()).isFalse();
    }
}
