package br.com.decisao.motordecisao.log.service;

import br.com.decisao.motordecisao.log.dto.LogMessage;
import br.com.decisao.motordecisao.log.sender.LogSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.lang.System.Logger.Level.INFO;

@Slf4j
@Service
public class LogService {

    @Autowired
    private LogSender logSender;

    public void logData(String message, System.Logger.Level logLevel, Throwable ex) {
        if (INFO.equals(logLevel)) {
            log.info(message);
        } else {
            log.error(message, ex);
        }
        sendLog(message, logLevel);
    }

    private void sendLog(String message,
                        System.Logger.Level logLevel) {
        var logMessage = LogMessage.create(message,logLevel);
        logSender.sendLogMessageToQueue(logMessage);
    }
}
