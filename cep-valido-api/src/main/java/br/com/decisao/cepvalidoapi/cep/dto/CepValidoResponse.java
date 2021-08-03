package br.com.decisao.cepvalidoapi.cep.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CepValidoResponse {

    private String  cep;
    @JsonProperty("validCep")
    private boolean valido;
}
