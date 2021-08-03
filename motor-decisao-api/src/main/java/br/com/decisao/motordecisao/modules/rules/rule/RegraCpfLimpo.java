package br.com.decisao.motordecisao.modules.rules.rule;

import br.com.decisao.motordecisao.modules.data.dto.ConsultedApiService;
import br.com.decisao.motordecisao.modules.data.dto.PayloadProduct;
import br.com.decisao.motordecisao.modules.data.dto.Rule;
import org.springframework.stereotype.Component;

import static br.com.decisao.motordecisao.config.rule.RuleId.REGRA_AVALIAR_CPF_LIMPO;
import static br.com.decisao.motordecisao.modules.data.enums.Api.CPF_LIMPO;
import static br.com.decisao.motordecisao.modules.data.enums.RuleStatus.APROVADA;
import static br.com.decisao.motordecisao.modules.data.enums.RuleStatus.REPROVADA;
import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class RegraCpfLimpo {

    public Rule avaliarRegra(PayloadProduct payloadProduct) {
        var cpf = payloadProduct.getPayload().getPessoa().getCpf();
        var apiConsultada = ConsultedApiService.getConsultedApiService(CPF_LIMPO, payloadProduct.getPayload().getApisConsultadas());
        var dadosCpf = payloadProduct.getPayload().getDadosApis().getCleanCpf();

        if (isEmpty(cpf)) {
            return Rule.createRuleWithStatus(REPROVADA, REGRA_AVALIAR_CPF_LIMPO, "O CPF está nulo para avaliação.");
        }

        if (isEmpty(apiConsultada)) {
            return Rule.createRuleWithPendingService(CPF_LIMPO, REGRA_AVALIAR_CPF_LIMPO, "O serviço de CPF limpo ainda não foi consultado.");
        }

        if (!apiConsultada.isSucesso()) {
            return Rule.createRuleWithStatus(REPROVADA, REGRA_AVALIAR_CPF_LIMPO, "O serviço de CPF limpo falhou na consulta.");
        }

        if (apiConsultada.isSucesso() && isEmpty(dadosCpf.getCpf())) {
           return Rule.createRuleWithStatus(REPROVADA, REGRA_AVALIAR_CPF_LIMPO, "O serviço de CPF limpo não retornou dados.");
        }

        if (!dadosCpf.isCleanCpf()) {
            return Rule.createRuleWithStatus(REPROVADA, REGRA_AVALIAR_CPF_LIMPO, "O CPF do cliente está sujo.");
        }

        if (dadosCpf.isCleanCpf()) {
            return Rule.createRuleWithStatus(APROVADA, REGRA_AVALIAR_CPF_LIMPO, "O CPF do cliente está limpo.");
        }

        return new Rule();
    }
}
