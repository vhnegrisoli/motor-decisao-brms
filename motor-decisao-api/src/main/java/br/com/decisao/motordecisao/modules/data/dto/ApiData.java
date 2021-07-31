package br.com.decisao.motordecisao.modules.data.dto;

import br.com.decisao.motordecisao.modules.data.dto.restclient.CpfServiceResponse;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiData {

    private CpfServiceResponse cpfService = new CpfServiceResponse();
}