package br.com.decisao.motordecisao.modules.rules.service;

import br.com.decisao.motordecisao.config.rule.RuleId;
import br.com.decisao.motordecisao.modules.data.dto.PayloadProduct;
import br.com.decisao.motordecisao.modules.data.dto.Rule;
import br.com.decisao.motordecisao.modules.rules.rule.RegraCepValido;
import br.com.decisao.motordecisao.modules.rules.rule.RegraCpfLimpo;
import br.com.decisao.motordecisao.modules.rules.rule.RegraCpfValido;
import br.com.decisao.motordecisao.modules.rules.rule.RegraIdadePermitida;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RuleExecutorService {

    @Autowired
    private RegraCpfValido regraCpfValido;

    @Autowired
    private RegraCpfLimpo regraCpfLimpo;

    @Autowired
    private RegraCepValido regraCepValido;

    @Autowired
    private RegraIdadePermitida regraIdadePermitida;

    public Rule executeRule(RuleId ruleId,
                            PayloadProduct payloadProduct) {
        switch (ruleId) {
            case REGRA_AVALIAR_CPF_VALIDO:
                return regraCpfValido.avaliarRegra(payloadProduct);
            case REGRA_AVALIAR_CPF_LIMPO:
                return regraCpfLimpo.avaliarRegra(payloadProduct);
            case REGRA_AVALIAR_IDADE_PERMITIDA:
                return regraIdadePermitida.avaliarRegra(payloadProduct);
            case REGRA_AVALIAR_CEP_VALIDO:
                return regraCepValido.avaliarRegra(payloadProduct);
            default:
                return new Rule();
        }
    }
}
