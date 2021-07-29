package br.com.decisao.motordecisao.modules.data.dto;

import br.com.decisao.motordecisao.modules.data.enums.Api;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class PayloadProduct {

    PayloadRequest payload = new PayloadRequest();

    EngineProduct produto = new EngineProduct();

    public static PayloadProduct create(PayloadRequest payload,
                                        EngineProduct product) {
        return PayloadProduct
            .builder()
            .payload(payload)
            .produto(product)
            .build();
    }

    public void addConsultedApi(Api api, boolean success, int status, String reason) {
        getPayload()
            .getApisConsultadas()
            .add(ConsultedApiService.create(api, success, status, reason));
    }
}
