package br.com.decisao.motordecisao.modules.rules.service;

import br.com.decisao.motordecisao.config.rule.RuleId;
import br.com.decisao.motordecisao.modules.data.dto.PayloadProduct;
import br.com.decisao.motordecisao.modules.data.dto.Rule;
import br.com.decisao.motordecisao.modules.rules.rule.RegraCpfValido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RuleExecutorService {

    @Autowired
    private RegraCpfValido regraCpfValido;

    public Rule executeRule(RuleId ruleId,
                            PayloadProduct payloadProduct) {
        switch (ruleId) {
            case REGRA_AVALIAR_CPF_VALIDO:
                return regraCpfValido.avaliarRegra(payloadProduct);
            case REGRA_AVALIAR_CPF_LIMPO:
            case REGRA_AVALIAR_IDADE_PERMITIDA:
            default:
                return new Rule();
        }
    }
}
