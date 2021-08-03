package br.com.decisao.motordecisao.modules.data.dto.restclient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CleanCpfResponse {

    private String cpf;
    private boolean cleanCpf;
}
