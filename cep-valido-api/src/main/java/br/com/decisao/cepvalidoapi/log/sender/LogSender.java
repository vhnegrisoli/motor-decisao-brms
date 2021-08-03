package br.com.decisao.cepvalidoapi.log.sender;

import br.com.decisao.cepvalidoapi.config.JsonUtil;
import br.com.decisao.cepvalidoapi.log.dto.LogMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private JsonUtil jsonUtil;

    @Value("${rabbit.exchange.log-data}")
    private String logDataTopicExchange;

    @Value("${rabbit.routingkey.log-data}")
    private String logDataRoutingkey;

    public void sendLogMessageToQueue(LogMessage logMessage) {
        try {
            log.info("Enviando mensagem ao RabbitMQ: {}.", jsonUtil.toJson(logMessage));
            rabbitTemplate.convertAndSend(logDataTopicExchange, logDataRoutingkey, logMessage);
        } catch (Exception ex) {
            log.error("Erro ao enviar mensagem ao RabbitMQ.", ex);
        }
    }
}
