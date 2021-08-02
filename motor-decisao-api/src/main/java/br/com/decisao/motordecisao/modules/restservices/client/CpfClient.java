package br.com.decisao.motordecisao.modules.restservices.client;

import br.com.decisao.motordecisao.config.TransactionData;
import br.com.decisao.motordecisao.modules.data.dto.restclient.CpfServiceResponse;
import br.com.decisao.motordecisao.modules.data.dto.PayloadProduct;
import br.com.decisao.motordecisao.modules.data.enums.Api;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpMethod.GET;


@Slf4j
@Component
public class CpfClient {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${app-config.services.cpf.valid}")
    private String validCpfEndpoint;

    @Value("${app-config.services.cpf.clean}")
    private String cleanCpfEndpoint;

    public void callValidCpfClient(PayloadProduct payloadProduct) {
        var transactionData = TransactionData.getTransactionData();
        try {
            log.info("Chamando serviço de CPF com dados: {}", objectMapper.writeValueAsString(payloadProduct));
            var url = buildUrl(validCpfEndpoint, payloadProduct.getPayload().getPessoa().getCpf());
            var response = restTemplate
                .exchange(url, GET, new HttpEntity<>(buildHeaders()), CpfServiceResponse.class)
                .getBody();
            payloadProduct.getPayload().getDadosApis().setCpfService(response);
            log.info("Resposta do serviço de CPF: {}. TransactionId: {}, ServiceId: {}.",
                objectMapper.writeValueAsString(response), transactionData.getTransactionId(), transactionData.getServiceId());
            payloadProduct.addConsultedApi(Api.CPF_VALIDO, true, 200, null);
        } catch (Exception ex) {
            log.error("TransactionId: {}, ServiceId: {}. Erro ao consultar serviço de CPF: ",
                transactionData.getTransactionId(), transactionData.getServiceId(), ex);
            payloadProduct.addConsultedApi(Api.CPF_VALIDO, false, 400, "O CPF não foi encontrado.");
        }
    }

    private String buildUrl(String url,
                            String cpf) {
        return String.format(url, cpf);
    }

    private HttpHeaders buildHeaders() {
        var transactionData = TransactionData.getTransactionData();
        var headers = new HttpHeaders();
        headers.set("transactionId", transactionData.getTransactionId());
        return headers;
    }
}
