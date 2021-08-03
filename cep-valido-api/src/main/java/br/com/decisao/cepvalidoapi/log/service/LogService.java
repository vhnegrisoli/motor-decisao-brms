package br.com.decisao.cepvalidoapi.log.service;

import br.com.decisao.cepvalidoapi.config.TransactionData;
import br.com.decisao.cepvalidoapi.log.dto.LogMessage;
import br.com.decisao.cepvalidoapi.log.sender.LogSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import static java.lang.System.Logger.Level.ERROR;
import static java.lang.System.Logger.Level.INFO;

@Slf4j
@Service
public class LogService {

    @Autowired
    private LogSender logSender;

    public void logData(String message) {
        logData(message, null);
    }

    public void logData(String message, Throwable ex) {
        var logLevel = INFO;
        if (ObjectUtils.isEmpty(ex)) {
            log.info(message.concat(complementMessage()));
        } else {
            log.error(message, ex);
            logLevel = ERROR;
        }
        sendLog(message, logLevel);
    }

    private void sendLog(String message,
                        System.Logger.Level logLevel) {
        var logMessage = LogMessage.create(message,logLevel);
        logSender.sendLogMessageToQueue(logMessage);
    }

    private String complementMessage() {
        var transactionData = TransactionData.getTransactionData();
        return String.format(
            " [TransactionId: %s - ServiceId: %s.]",
            transactionData.getTransactionId(),
            transactionData.getServiceId());
    }
}
