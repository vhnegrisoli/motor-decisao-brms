package br.com.decisao.cpflimpoapi.log.dto;

import br.com.decisao.cpflimpoapi.config.TransactionData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogMessage {

    private String transactionId;

    private String serviceId;

    private String message;

    private String logLevel;

    private String serviceName;

    public static LogMessage create(String message,
                                    System.Logger.Level logLevel) {
        var transactionData = TransactionData.getTransactionData();
        return LogMessage
            .builder()
            .transactionId(transactionData.getTransactionId())
            .serviceId(transactionData.getServiceId())
            .serviceName("cpf-limpo-api")
            .message(message)
            .logLevel(logLevel.name())
            .build();
    }
}
