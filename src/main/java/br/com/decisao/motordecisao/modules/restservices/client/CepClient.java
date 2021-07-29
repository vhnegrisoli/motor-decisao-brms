package br.com.decisao.motordecisao.modules.restservices.client;

import br.com.decisao.motordecisao.modules.data.dto.ConsultedApiService;
import br.com.decisao.motordecisao.modules.data.dto.restclient.CpfServiceResponse;
import br.com.decisao.motordecisao.modules.data.dto.PayloadProduct;
import br.com.decisao.motordecisao.modules.data.enums.Api;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CepClient {

    @Autowired
    private ObjectMapper objectMapper;

    public void callCepClient(PayloadProduct payloadProduct) {
        try {
            log.info("Chamando serviço de CPF com dados: {}", objectMapper.writeValueAsString(payloadProduct));
            var response = CpfServiceResponse
                .builder()
                .cpf("10332458954")
                .validCpf(true)
                .build();
            payloadProduct.getPayload().getDadosApis().setCpfService(response);
            log.info("Resposta do serviço de CPF: {}", objectMapper.writeValueAsString(response));
            payloadProduct.addConsultedApi(Api.CPF, true, 200, null);
        } catch (Exception ex) {
            log.error("Erro ao consultar serviço de CPF: ", ex);
            payloadProduct.addConsultedApi(Api.CPF, false, 400, "O CPF não foi encontrado.");
        }
    }
}
