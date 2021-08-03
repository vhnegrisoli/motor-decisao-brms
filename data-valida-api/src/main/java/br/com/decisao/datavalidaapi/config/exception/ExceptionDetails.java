package br.com.decisao.datavalidaapi.config.exception;

import lombok.Data;

@Data
public class ExceptionDetails {

    private int status;
    private String message;
    private String transactionId;
    private String serviceId;
}
