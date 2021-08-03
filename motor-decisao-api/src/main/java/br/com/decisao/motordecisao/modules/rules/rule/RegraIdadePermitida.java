package br.com.decisao.motordecisao.modules.rules.rule;

import br.com.decisao.motordecisao.modules.data.dto.ConsultedApiService;
import br.com.decisao.motordecisao.modules.data.dto.PayloadProduct;
import br.com.decisao.motordecisao.modules.data.dto.Rule;
import org.springframework.stereotype.Component;

import static br.com.decisao.motordecisao.config.rule.RuleId.REGRA_AVALIAR_IDADE_PERMITIDA;
import static br.com.decisao.motordecisao.modules.data.enums.Api.IDADE;
import static br.com.decisao.motordecisao.modules.data.enums.RuleStatus.APROVADA;
import static br.com.decisao.motordecisao.modules.data.enums.RuleStatus.REPROVADA;
import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class RegraIdadePermitida {

    public Rule avaliarRegra(PayloadProduct payloadProduct) {
        var dataNascimento = payloadProduct.getPayload().getPessoa().getDataNascimento();
        var apiConsultada = ConsultedApiService.getConsultedApiService(IDADE, payloadProduct.getPayload().getApisConsultadas());
        var dadosIdade = payloadProduct.getPayload().getDadosApis().getBirthday();
        var idadePermitida = 18;

        if (isEmpty(dataNascimento)) {
            return Rule.createRuleWithStatus(REPROVADA, REGRA_AVALIAR_IDADE_PERMITIDA, "A data de nascimento está nula para avaliação.");
        }

        if (isEmpty(apiConsultada)) {
            return Rule.createRuleWithPendingService(IDADE, REGRA_AVALIAR_IDADE_PERMITIDA, "O serviço de Idade ainda não foi consultado.");
        }

        if (!apiConsultada.isSucesso()) {
            return Rule.createRuleWithStatus(REPROVADA, REGRA_AVALIAR_IDADE_PERMITIDA, "O serviço de idade falhou na consulta.");
        }

        if (apiConsultada.isSucesso() && isEmpty(dadosIdade.getAge())) {
           return Rule.createRuleWithStatus(REPROVADA, REGRA_AVALIAR_IDADE_PERMITIDA, "O serviço de idade não retornou dados.");
        }

        if (dadosIdade.getAge() < idadePermitida) {
            return Rule.createRuleWithStatus(REPROVADA, REGRA_AVALIAR_IDADE_PERMITIDA, "O cliente é menor que a idade permitida.");
        }

        if (dadosIdade.getAge() >= idadePermitida) {
            return Rule.createRuleWithStatus(APROVADA, REGRA_AVALIAR_IDADE_PERMITIDA, "O cliente é maior ou igual a idade permitida.");
        }

        return new Rule();
    }
}
