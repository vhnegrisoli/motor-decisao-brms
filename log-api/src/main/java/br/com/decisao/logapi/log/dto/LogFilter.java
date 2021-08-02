package br.com.decisao.logapi.log.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogFilter {

    private String transactionId;
    private String serviceId;
    private String message;
    private String logLevel;
    private String serviceName;
}
