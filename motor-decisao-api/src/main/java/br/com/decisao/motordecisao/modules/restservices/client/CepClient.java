package br.com.decisao.motordecisao.modules.restservices.client;

import br.com.decisao.motordecisao.config.JsonUtil;
import br.com.decisao.motordecisao.config.TransactionData;
import br.com.decisao.motordecisao.log.service.LogService;
import br.com.decisao.motordecisao.modules.data.dto.PayloadProduct;
import br.com.decisao.motordecisao.modules.data.dto.restclient.ValidCpfResponse;
import br.com.decisao.motordecisao.modules.data.enums.Api;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static java.lang.String.format;
import static org.springframework.http.HttpMethod.GET;


@Component
public class CepClient {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LogService logService;

    @Autowired
    private JsonUtil jsonUtil;

    @Value("${app-config.services.cep.uri}")
    private String validCepEndpoint;

    public void callValidCepClient(PayloadProduct payloadProduct) {
        var transactionData = TransactionData.getTransactionData();
        try {
            var url = buildUrl(validCepEndpoint, payloadProduct.getPayload().getPessoa().getCpf());
            logService.logData(format("Chamando serviço de CPF válido (%s) com dados: %s. TransactionId: %s, ServiceId: %s",
                url, jsonUtil.toJson(payloadProduct), transactionData.getTransactionId(), transactionData.getServiceId()));
            var response = (ValidCpfResponse) callApi(url, GET, buildHeaders(), ValidCpfResponse.class);
            payloadProduct.getPayload().getDadosApis().setValidCpf(response);
            logService.logData(format("Resposta do serviço de CPF válido: %s. TransactionId: %s, ServiceId: %s.",
                jsonUtil.toJson(response), transactionData.getTransactionId(), transactionData.getServiceId()));
            payloadProduct.addConsultedApi(Api.CPF_VALIDO, true, 200, null);
        } catch (Exception ex) {
            logService.logData(format("TransactionId: %s, ServiceId: %s. Erro ao consultar serviço de CPF válido: ",
                transactionData.getTransactionId(), transactionData.getServiceId()), ex);
            payloadProduct.addConsultedApi(Api.CPF_VALIDO, false, 400, format("O CEP não foi encontrado: %s", ex.getMessage()));
        }
    }

    private String buildUrl(String url,
                            String cep) {
        return format(url, cep);
    }

    private HttpHeaders buildHeaders() {
        var transactionData = TransactionData.getTransactionData();
        var headers = new HttpHeaders();
        headers.set("transactionId", transactionData.getTransactionId());
        return headers;
    }

    private <T> Object callApi(String url,
                               HttpMethod httpMethod,
                               HttpHeaders headers,
                               Class objectClass) {
        return restTemplate
            .exchange(url, httpMethod, new HttpEntity<>(buildHeaders()), objectClass)
            .getBody();
    }
}
