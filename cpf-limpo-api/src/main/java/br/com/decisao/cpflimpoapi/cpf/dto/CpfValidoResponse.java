package br.com.decisao.cpflimpoapi.cpf.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CpfValidoResponse {

    private String cpf;

    @JsonProperty("validCpf")
    private boolean cpfValido;
}
