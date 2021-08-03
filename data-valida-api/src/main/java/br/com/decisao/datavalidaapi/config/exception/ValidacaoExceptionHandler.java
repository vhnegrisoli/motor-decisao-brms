package br.com.decisao.datavalidaapi.config.exception;

import br.com.decisao.datavalidaapi.config.TransactionData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ValidacaoExceptionHandler {

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<?> handleResouseNotFoundException(ValidacaoException rfnException) {
        ExceptionDetails resourceNotFoundDetails = new ExceptionDetails();
        var transactionData = TransactionData.getTransactionData();
        resourceNotFoundDetails.setStatus(HttpStatus.BAD_REQUEST.value());
        resourceNotFoundDetails.setMessage(rfnException.getMessage());
        resourceNotFoundDetails.setTransactionId(transactionData.getTransactionId());
        resourceNotFoundDetails.setServiceId(transactionData.getServiceId());
        return new ResponseEntity<>(resourceNotFoundDetails, HttpStatus.BAD_REQUEST);
    }
}
