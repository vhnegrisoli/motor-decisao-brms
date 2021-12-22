package br.com.decisao.motordecisao.modules.restservices.service;

import br.com.decisao.motordecisao.modules.data.dto.PayloadProduct;
import br.com.decisao.motordecisao.modules.data.enums.Api;
import br.com.decisao.motordecisao.modules.restservices.client.WrapperApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestCallService {

    @Autowired
    private WrapperApiClient wrapperApiClient;

    public void callPendingApi(Api api,
                               PayloadProduct payloadProduct) {
        wrapperApiClient.callWrapperApi(api, payloadProduct);
    }
}
