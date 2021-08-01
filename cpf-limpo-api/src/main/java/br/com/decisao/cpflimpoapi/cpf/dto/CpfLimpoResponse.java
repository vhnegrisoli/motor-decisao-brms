package br.com.decisao.cpflimpoapi.cpf.dto;

import br.com.decisao.cpflimpoapi.cpf.model.CpfLimpo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CpfLimpoResponse {

    private String cpf;

    @JsonProperty("cleanCpf")
    private boolean cpfLimpo;

    public static CpfLimpoResponse converterDe(CpfLimpo cpfLimpo) {
        return CpfLimpoResponse
            .builder()
            .cpf(cpfLimpo.getCpf())
            .cpfLimpo(cpfLimpo.isLimpo())
            .build();
    }
}
