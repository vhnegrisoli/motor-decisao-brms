package br.com.decisao.motordecisao.modules.restservices.client;

import br.com.decisao.motordecisao.config.JsonUtil;
import br.com.decisao.motordecisao.log.service.LogService;
import br.com.decisao.motordecisao.modules.data.dto.PayloadProduct;
import br.com.decisao.motordecisao.modules.data.dto.restclient.CleanCpfResponse;
import br.com.decisao.motordecisao.modules.data.dto.restclient.ValidCpfResponse;
import br.com.decisao.motordecisao.modules.data.enums.Api;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static br.com.decisao.motordecisao.config.HttpHeaderUtil.buildHeaders;
import static java.lang.String.format;
import static org.springframework.http.HttpMethod.GET;

@Component
public class CpfClient {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LogService logService;

    @Autowired
    private JsonUtil jsonUtil;

    @Value("${app-config.services.cpf.valid}")
    private String validCpfEndpoint;

    @Value("${app-config.services.cpf.clean}")
    private String cleanCpfEndpoint;

    public void callValidCpfClient(PayloadProduct payloadProduct) {
        try {
            var url = buildUrl(validCpfEndpoint, payloadProduct.getPayload().getPessoa().getCpf());
            logService.logData(format("Chamando serviço de CPF válido (%s) com dados: %s.", url, jsonUtil.toJson(payloadProduct)));
            var response = (ValidCpfResponse) callApi(url, ValidCpfResponse.class);
            payloadProduct.getPayload().getDadosApis().setValidCpf(response);
            logService.logData(format("Resposta do serviço de CPF válido: %s.", jsonUtil.toJson(response)));
            payloadProduct.addConsultedApi(Api.CPF_VALIDO, true, 200, null);
        } catch (Exception ex) {
            logService.logData("Erro ao consultar serviço de CPF válido: ", ex);
            payloadProduct.addConsultedApi(Api.CPF_VALIDO, false, 400, format("O CPF não foi encontrado: %s", ex.getMessage()));
        }
    }

    public void callCleanCpfClient(PayloadProduct payloadProduct) {
        try {
            var url = buildUrl(cleanCpfEndpoint, payloadProduct.getPayload().getPessoa().getCpf());
            logService.logData(format("Chamando serviço de CPF limpo (%s) com dados: %s.", url, jsonUtil.toJson(payloadProduct)));
            var response = (CleanCpfResponse) callApi(url, CleanCpfResponse.class);
            payloadProduct.getPayload().getDadosApis().setCleanCpf(response);
            logService.logData(format("Resposta do serviço de CPF limpo: %s.", jsonUtil.toJson(response)));
            payloadProduct.addConsultedApi(Api.CPF_LIMPO, true, 200, null);
        } catch (Exception ex) {
            logService.logData("Erro ao consultar serviço de CPF limpo.", ex);
            payloadProduct.addConsultedApi(Api.CPF_LIMPO, false, 400, format("O CPF não foi encontrado: %s", ex.getMessage()));
        }
    }

    private String buildUrl(String url,
                            String cpf) {
        return format(url, cpf);
    }

    private Object callApi(String url,
                               Class objectClass) {
        return restTemplate
            .exchange(url, GET, new HttpEntity<>(buildHeaders()), objectClass)
            .getBody();
    }
}
