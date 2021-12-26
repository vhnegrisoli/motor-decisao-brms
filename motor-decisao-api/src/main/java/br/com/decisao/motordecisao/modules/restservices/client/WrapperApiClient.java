package br.com.decisao.motordecisao.modules.restservices.client;

import br.com.decisao.motordecisao.config.JsonUtil;
import br.com.decisao.motordecisao.log.service.LogService;
import br.com.decisao.motordecisao.modules.data.dto.ApiWrapperData;
import br.com.decisao.motordecisao.modules.data.dto.PayloadProduct;
import br.com.decisao.motordecisao.modules.data.enums.Api;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static br.com.decisao.motordecisao.config.HttpHeaderUtil.buildHeaders;
import static java.lang.String.format;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class WrapperApiClient {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LogService logService;

    @Autowired
    private JsonUtil jsonUtil;

    @Value("${app-config.services.wrapper.uri}")
    private String url;

    public void callWrapperApi(Api serviceId, PayloadProduct payloadProduct) {
        try {
            var request = new ApiWrapperData(serviceId, payloadProduct.getPayload());
            logService.logData(format("Chamando serviço Wrapper para API %s com dados: %s.", serviceId, jsonUtil.toJson(request)));
            var response = restTemplate.exchange(url, POST, new HttpEntity<>(request, buildHeaders()), ApiWrapperData.class).getBody();
            logService.logData(format("Resposta do serviço Wrapper para a API %s: %s.", serviceId, jsonUtil.toJson(response)));
            if (!isEmpty(response) && !isEmpty(response.getPayload())) {
                payloadProduct.setPayload(response.getPayload());
            } else {
                payloadProduct.addConsultedApi(serviceId, true, 200, null);
            }
        } catch (Exception ex) {
            logService.logData(format("Erro ao consultar serviço Wrapper para API %s.", serviceId), ex);
            payloadProduct.addConsultedApi(serviceId, false, 400, format(
                "Erro ao consultar Wrapper para API %s: %s", serviceId, ex.getMessage())
            );
        }
    }
}
