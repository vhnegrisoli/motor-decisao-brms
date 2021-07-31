package br.com.decisao.motordecisao.modules.data.dto;

import br.com.decisao.motordecisao.config.rule.RuleId;
import br.com.decisao.motordecisao.modules.data.enums.Api;
import br.com.decisao.motordecisao.modules.data.enums.RuleStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Rule {

    private RuleId id;

    @JsonProperty("description")
    private String descricao;

    private RuleStatus status;

    @JsonIgnore
    private Api apiPendente;

    public static boolean isAlreadyEvaluatedRule(RuleId ruleId, List<Rule> rules) {
        return !ObjectUtils.isEmpty(ruleId)
            && !ObjectUtils.isEmpty(rules)
            && rules
            .stream()
            .anyMatch(rule -> ruleId.equals(rule.getId()));
    }

    @JsonIgnore
    public boolean isApproved() {
        return RuleStatus.APROVADA.equals(status);
    }

    @JsonIgnore
    public boolean isDisapproved() {
        return RuleStatus.REPROVADA.equals(status);
    }

    @JsonIgnore
    public boolean isApiPending() {
        return RuleStatus.API_PENDENTE.equals(status) && !ObjectUtils.isEmpty(apiPendente);
    }

    public static Rule createRuleWithStatus(RuleStatus status,
                                            RuleId id,
                                            String description) {
        return Rule
            .builder()
            .id(id)
            .status(status)
            .descricao(description)
            .apiPendente(null)
            .build();
    }

    public static Rule createRuleWithPendingService(Api pendingService,
                                                    RuleId id,
                                                    String description) {
        return Rule
            .builder()
            .id(id)
            .status(RuleStatus.API_PENDENTE)
            .apiPendente(pendingService)
            .descricao(description)
            .build();
    }
}
