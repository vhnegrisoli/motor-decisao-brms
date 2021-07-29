package br.com.decisao.motordecisao.config.rule;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RuleAvailability {

    HABILITADA(true),
    DESABILITADA(false)
    ;

    private final Boolean ruleAvailability;
}
