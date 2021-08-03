package br.com.decisao.motordecisao.modules.data.dto;

import br.com.decisao.motordecisao.modules.data.dto.restclient.BirthdayResponse;
import br.com.decisao.motordecisao.modules.data.dto.restclient.CleanCpfResponse;
import br.com.decisao.motordecisao.modules.data.dto.restclient.ValidCepResponse;
import br.com.decisao.motordecisao.modules.data.dto.restclient.ValidCpfResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiData {

    private ValidCpfResponse validCpf = new ValidCpfResponse();
    private CleanCpfResponse cleanCpf = new CleanCpfResponse();
    private BirthdayResponse birthday = new BirthdayResponse();
    private ValidCepResponse cep = new ValidCepResponse();
}