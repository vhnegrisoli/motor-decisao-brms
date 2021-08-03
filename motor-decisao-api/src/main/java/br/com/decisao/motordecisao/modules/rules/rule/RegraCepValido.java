package br.com.decisao.motordecisao.modules.rules.rule;

import br.com.decisao.motordecisao.modules.data.dto.ConsultedApiService;
import br.com.decisao.motordecisao.modules.data.dto.PayloadProduct;
import br.com.decisao.motordecisao.modules.data.dto.Rule;
import org.springframework.stereotype.Component;

import static br.com.decisao.motordecisao.config.rule.RuleId.REGRA_AVALIAR_CEP_VALIDO;
import static br.com.decisao.motordecisao.modules.data.enums.Api.CEP;
import static br.com.decisao.motordecisao.modules.data.enums.Api.CPF_VALIDO;
import static br.com.decisao.motordecisao.modules.data.enums.RuleStatus.APROVADA;
import static br.com.decisao.motordecisao.modules.data.enums.RuleStatus.REPROVADA;
import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class RegraCepValido {

    public Rule avaliarRegra(PayloadProduct payloadProduct) {
        var cep = payloadProduct.getPayload().getPessoa().getCep();
        var apiConsultada = ConsultedApiService.getConsultedApiService(CEP, payloadProduct.getPayload().getApisConsultadas());
        var dadosCep = payloadProduct.getPayload().getDadosApis().getCep();

        if (isEmpty(cep)) {
            return Rule.createRuleWithStatus(REPROVADA, REGRA_AVALIAR_CEP_VALIDO, "O cep está nulo para avaliação.");
        }

        if (isEmpty(apiConsultada)) {
            return Rule.createRuleWithPendingService(CPF_VALIDO, REGRA_AVALIAR_CEP_VALIDO, "O serviço de cep válido ainda não foi consultado.");
        }

        if (!apiConsultada.isSucesso()) {
            return Rule.createRuleWithStatus(REPROVADA, REGRA_AVALIAR_CEP_VALIDO, "O serviço de cep válido falhou na consulta.");
        }

        if (apiConsultada.isSucesso() && isEmpty(dadosCep.getCep())) {
           return Rule.createRuleWithStatus(REPROVADA, REGRA_AVALIAR_CEP_VALIDO, "O serviço de cep válido não retornou dados.");
        }

        if (!dadosCep.isValidCep()) {
            return Rule.createRuleWithStatus(REPROVADA, REGRA_AVALIAR_CEP_VALIDO, "O cep do cliente está inválido.");
        }

        if (dadosCep.isValidCep()) {
            return Rule.createRuleWithStatus(APROVADA, REGRA_AVALIAR_CEP_VALIDO, "O cep do cliente está válido.");
        }

        return new Rule();
    }
}
