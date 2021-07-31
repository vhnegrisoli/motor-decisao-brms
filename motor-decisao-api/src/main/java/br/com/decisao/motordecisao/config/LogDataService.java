package br.com.decisao.motordecisao.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogDataService {

    private String transactionId;
    private String serviceId;

    public void logData(String message,
                        String payload) {
        var transaction = TransactionData.getTransactionData();
        log.info(
            message,
            transaction.getTransactionId(),
            transaction.getServiceId(),
            payload
        );
    }
}
