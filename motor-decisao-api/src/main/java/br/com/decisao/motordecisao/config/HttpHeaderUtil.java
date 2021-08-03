package br.com.decisao.motordecisao.config;

import org.springframework.http.HttpHeaders;

public class HttpHeaderUtil {

    public static HttpHeaders buildHeaders() {
        var transactionData = TransactionData.getTransactionData();
        var headers = new HttpHeaders();
        headers.set("transactionId", transactionData.getTransactionId());
        return headers;
    }
}
