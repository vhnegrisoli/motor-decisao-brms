package br.com.decisao.cepvalidoapi.cep.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CepResponse {

    private String  cep;
    private boolean erro;
}
