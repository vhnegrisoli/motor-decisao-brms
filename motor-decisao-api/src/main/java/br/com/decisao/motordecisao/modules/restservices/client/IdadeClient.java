package br.com.decisao.motordecisao.modules.restservices.client;

import br.com.decisao.motordecisao.config.JsonUtil;
import br.com.decisao.motordecisao.config.TransactionData;
import br.com.decisao.motordecisao.log.service.LogService;
import br.com.decisao.motordecisao.modules.data.dto.PayloadProduct;
import br.com.decisao.motordecisao.modules.data.dto.restclient.BirthdayResponse;
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
import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.INFO;
import static org.springframework.http.HttpMethod.GET;


@Component
public class IdadeClient {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LogService logService;

    @Autowired
    private JsonUtil jsonUtil;

    @Value("${app-config.services.idade.uri}")
    private String validAgeEndpoint;

    public void callAgeClient(PayloadProduct payloadProduct) {
        try {
            var url = buildUrl(validAgeEndpoint, payloadProduct.getPayload().getPessoa().getCpf());
            logService.logData(format("Chamando serviço de idade (%s) com dados: %s.", url, jsonUtil.toJson(payloadProduct)));
            var response = (BirthdayResponse) callApi(url, GET, buildHeaders(), ValidCpfResponse.class);
            payloadProduct.getPayload().getDadosApis().setBirthday(response);
            logService.logData(format("Resposta do serviço de idade: %s.", jsonUtil.toJson(response)));
            payloadProduct.addConsultedApi(Api.IDADE, true, 200, null);
        } catch (Exception ex) {
            logService.logData("Erro ao consultar serviço de CPF válido.", ex);
            payloadProduct.addConsultedApi(Api.IDADE, false, 400, format("A idade não foi encontrada: %s", ex.getMessage()));
        }
    }

    private String buildUrl(String url,
                            String birthday) {
        return format(url, birthday);
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
