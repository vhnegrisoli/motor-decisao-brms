package br.com.decisao.motordecisao.log.service;

import br.com.decisao.motordecisao.config.TransactionData;
import br.com.decisao.motordecisao.log.dto.LogMessage;
import br.com.decisao.motordecisao.log.sender.LogSender;
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
        var transactionData = TransactionData.getTransactionData();
        message = message.concat(String.format(" [TransactionId: %s - ServiceId: %s.]",
            transactionData.getTransactionId(), transactionData.getServiceId()));
        System.Logger.Level logLevel = null;
        if (ObjectUtils.isEmpty(ex)) {
            log.info(message);
            logLevel = INFO;
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
}
