package br.com.decisao.motordecisao.modules.rules.rule;

import br.com.decisao.motordecisao.modules.data.dto.ConsultedApiService;
import br.com.decisao.motordecisao.modules.data.dto.PayloadProduct;
import br.com.decisao.motordecisao.modules.data.dto.Rule;
import org.springframework.stereotype.Component;

import static br.com.decisao.motordecisao.config.rule.RuleId.REGRA_AVALIAR_CPF_VALIDO;
import static br.com.decisao.motordecisao.modules.data.enums.Api.CPF;
import static br.com.decisao.motordecisao.modules.data.enums.RuleStatus.APROVADA;
import static br.com.decisao.motordecisao.modules.data.enums.RuleStatus.REPROVADA;
import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class RegraCpfValido {

    public Rule avaliarRegra(PayloadProduct payloadProduct) {
        var cpf = payloadProduct.getPayload().getPessoa().getCpf();
        var apiConsultada = ConsultedApiService.getConsultedApiService(CPF, payloadProduct.getPayload().getApisConsultadas());
        var dadosCpf = payloadProduct.getPayload().getDadosApis().getCpfService();

        if (isEmpty(cpf)) {
            return Rule.createRuleWithStatus(REPROVADA, REGRA_AVALIAR_CPF_VALIDO, "O CPF está nulo para avaliação.");
        }

        if (isEmpty(apiConsultada)) {
            return Rule.createRuleWithPendingService(CPF, REGRA_AVALIAR_CPF_VALIDO, "O serviço de CPF ainda não foi consultado.");
        }

        if (!apiConsultada.isSucesso()) {
            return Rule.createRuleWithStatus(REPROVADA, REGRA_AVALIAR_CPF_VALIDO, "O serviço de CPF falhou na consulta.");
        }

        if (apiConsultada.isSucesso() && isEmpty(dadosCpf.getCpf())) {
           return Rule.createRuleWithStatus(REPROVADA, REGRA_AVALIAR_CPF_VALIDO, "O serviço de CPF não retornou dados.");
        }

        if (!dadosCpf.isValidCpf()) {
            return Rule.createRuleWithStatus(REPROVADA, REGRA_AVALIAR_CPF_VALIDO, "O CPF do cliente está inválido.");
        }

        if (dadosCpf.isValidCpf()) {
            return Rule.createRuleWithStatus(APROVADA, REGRA_AVALIAR_CPF_VALIDO, "O CPF do cliente está válido.");
        }

        return new Rule();
    }
}
