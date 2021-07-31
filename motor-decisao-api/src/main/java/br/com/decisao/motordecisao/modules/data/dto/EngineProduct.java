package br.com.decisao.motordecisao.modules.data.dto;

import br.com.decisao.motordecisao.modules.data.enums.ProductId;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class EngineProduct {

    private ProductId id;

    @JsonProperty("rules")
    private List<Rule> regras = new ArrayList<>();

    @JsonIgnore
    public boolean isDisapprovedProduct() {
        return !ObjectUtils.isEmpty(regras) &&
            regras
            .stream()
            .anyMatch(Rule::isDisapproved);
    }
}
