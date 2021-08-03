package br.com.decisao.motordecisao.modules.restservices.client;

import br.com.decisao.motordecisao.config.JsonUtil;
import br.com.decisao.motordecisao.log.service.LogService;
import br.com.decisao.motordecisao.modules.data.dto.PayloadProduct;
import br.com.decisao.motordecisao.modules.data.dto.restclient.ValidCepResponse;
import br.com.decisao.motordecisao.modules.data.enums.Api;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static br.com.decisao.motordecisao.config.HttpHeaderUtil.buildHeaders;
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
        try {
            var url = buildUrl(validCepEndpoint, payloadProduct.getPayload().getPessoa().getCep());
            logService.logData(format("Chamando serviço de CEP válido (%s) com dados: %s.", url, jsonUtil.toJson(payloadProduct)));
            var response = restTemplate.exchange(url, GET, new HttpEntity<>(buildHeaders()), ValidCepResponse.class).getBody();
            payloadProduct.getPayload().getDadosApis().setCep(response);
            logService.logData(format("Resposta do serviço de CEP válido: %s.", jsonUtil.toJson(response)));
            payloadProduct.addConsultedApi(Api.CEP, true, 200, null);
        } catch (Exception ex) {
            logService.logData("Erro ao consultar serviço de CEP válido.", ex);
            payloadProduct.addConsultedApi(Api.CEP, false, 400, format("O CEP não foi encontrado: %s", ex.getMessage()));
        }
    }

    private String buildUrl(String url,
                            String cep) {
        return format(url, cep);
    }
}
