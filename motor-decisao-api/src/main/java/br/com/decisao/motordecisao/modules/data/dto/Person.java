package br.com.decisao.motordecisao.modules.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @JsonProperty("document")
    private String cpf;

    @JsonProperty("birthday")
    private String dataNascimento;

    @JsonProperty("postcode")
    private String cep;

}
