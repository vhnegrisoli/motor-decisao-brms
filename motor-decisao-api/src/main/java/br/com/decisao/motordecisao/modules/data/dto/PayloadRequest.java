package br.com.decisao.motordecisao.modules.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayloadRequest {

    @JsonProperty("products")
    private List<EngineProduct> produtos = new ArrayList<>();

    @JsonProperty("disapprovedProducts")
    private List<EngineProduct> produtosReprovados = new ArrayList<>();

    @JsonProperty("person")
    private Person pessoa = new Person();

    @JsonProperty("consultedApis")
    private List<ConsultedApiService> apisConsultadas = new ArrayList<>();

    @JsonProperty("apiData")
    private ApiData dadosApis = new ApiData();

    public void removeNullRules() {
        getProdutos()
            .forEach(product -> product
                .getRegras()
                .removeIf(rule -> ObjectUtils.isEmpty(rule) || ObjectUtils.isEmpty(rule.getId())));
    }

    public void defineDisapprovedProducts() {
        getProdutos()
            .stream()
            .filter(EngineProduct::isDisapprovedProduct)
            .forEach(disapprovedProduct -> produtosReprovados.add(disapprovedProduct));

        getProdutos()
            .removeIf(EngineProduct::isDisapprovedProduct);
    }
}
