package br.com.decisao.motordecisao.modules.restservices.service;

import br.com.decisao.motordecisao.modules.data.dto.PayloadProduct;
import br.com.decisao.motordecisao.modules.data.enums.Api;
import br.com.decisao.motordecisao.modules.restservices.client.CepClient;
import br.com.decisao.motordecisao.modules.restservices.client.CpfClient;
import br.com.decisao.motordecisao.modules.restservices.client.IdadeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestCallService {

    @Autowired
    private CpfClient cpfClient;

    @Autowired
    private CepClient cepClient;

    @Autowired
    private IdadeClient idadeClient;

    public void callPendingApi(Api api,
                               PayloadProduct payloadProduct) {
        switch (api) {
            case CPF_VALIDO:
                cpfClient.callValidCpfClient(payloadProduct);
                break;
            case CPF_LIMPO:
                cpfClient.callCleanCpfClient(payloadProduct);
                break;
            case IDADE:
                idadeClient.callAgeClient(payloadProduct);
                break;
            case CEP:
                cepClient.callValidCepClient(payloadProduct);
                break;
        }
    }
}
