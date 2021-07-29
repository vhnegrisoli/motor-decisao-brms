package br.com.decisao.motordecisao.modules.restservices.service;

import br.com.decisao.motordecisao.modules.data.dto.PayloadProduct;
import br.com.decisao.motordecisao.modules.data.enums.Api;
import br.com.decisao.motordecisao.modules.restservices.client.CepClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestCallService {

    @Autowired
    private CepClient cepClient;

    public void callPendingApi(Api api,
                               PayloadProduct payloadProduct) {
        if (Api.CPF.equals(api)) {
            cepClient.callCepClient(payloadProduct);
        }
    }
}
