package br.com.decisao.logapi.log.listener;

import br.com.decisao.logapi.log.service.LogService;
import br.com.decisao.logapi.log.document.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogListener {

    @Autowired
    private LogService logService;

    @RabbitListener(queues = "${rabbit.queue.log-data}")
    public void saveLog(Log logResponse) {
        logService.saveLog(logResponse);
    }
}
