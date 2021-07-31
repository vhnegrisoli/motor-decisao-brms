package br.com.decisao.motordecisao.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionData {

    private String transactionId;
    private String serviceId;

    public static TransactionData getTransactionData() {
        var currentRequest = RequestUtil.getCurrentRequest();
        return TransactionData
            .builder()
            .transactionId(currentRequest.getHeader("transactionId"))
            .serviceId(currentRequest.getAttribute("serviceId").toString())
            .build();
    }
}
