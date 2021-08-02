package modules.mocks;

import br.com.decisao.motordecisao.config.rule.RuleId;
import br.com.decisao.motordecisao.modules.data.dto.ConsultedApiService;
import br.com.decisao.motordecisao.modules.data.dto.EngineProduct;
import br.com.decisao.motordecisao.modules.data.dto.Rule;
import br.com.decisao.motordecisao.modules.data.enums.Api;
import br.com.decisao.motordecisao.modules.data.enums.ProductId;
import br.com.decisao.motordecisao.modules.data.enums.RuleStatus;

public class DtoMocks {

    public static ConsultedApiService getOneConsultedApiService() {
        return ConsultedApiService
            .builder()
            .id(Api.CPF_VALIDO)
            .razao(null)
            .status(200)
            .sucesso(true)
            .build();
    }

    public static Rule getApprovedRule() {
        return Rule
            .builder()
            .apiPendente(null)
            .id(RuleId.REGRA_AVALIAR_CPF_VALIDO)
            .descricao("teste")
            .status(RuleStatus.APROVADA)
            .build();
    }

    public static Rule getDisapprovedRule() {
        return Rule
            .builder()
            .apiPendente(null)
            .id(RuleId.REGRA_AVALIAR_CPF_VALIDO)
            .descricao("teste")
            .status(RuleStatus.REPROVADA)
            .build();
    }

    public static Rule getPendingApiServiceRule() {
        return Rule
            .builder()
            .apiPendente(Api.CPF_VALIDO)
            .id(RuleId.REGRA_AVALIAR_CPF_VALIDO)
            .descricao("teste")
            .status(RuleStatus.API_PENDENTE)
            .build();
    }

    public static EngineProduct getEngineProduct() {
        return EngineProduct
            .builder()
            .id(ProductId.ALUGUEL)
            .build();
    }
}