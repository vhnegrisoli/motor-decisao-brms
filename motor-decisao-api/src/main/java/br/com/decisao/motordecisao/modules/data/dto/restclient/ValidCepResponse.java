package br.com.decisao.motordecisao.modules.data.dto.restclient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidCepResponse {

    private String cep;
    private boolean validCep;
}
