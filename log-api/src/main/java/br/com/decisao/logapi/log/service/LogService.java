package br.com.decisao.logapi.log.service;

import br.com.decisao.logapi.log.dto.LogFilter;
import br.com.decisao.logapi.log.repository.LogRepository;
import br.com.decisao.logapi.log.document.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

import static java.lang.String.format;
import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Service
public class LogService {

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Log> findAll(LogFilter logFilter) {
        var criteria = new Query();
        if (!isEmpty(logFilter.getTransactionId())) {
            criteria = criteria.addCriteria(Criteria.where("transactionId").is(logFilter.getTransactionId()));
        }
        if (!isEmpty(logFilter.getServiceId())) {
            criteria = criteria.addCriteria(Criteria.where("serviceId").is(logFilter.getServiceId()));
        }
        if (!isEmpty(logFilter.getLogLevel())) {
            criteria = criteria.addCriteria(Criteria.where("logLevel").is(logFilter.getLogLevel()));
        }
        if (!isEmpty(logFilter.getServiceName())) {
            criteria = criteria.addCriteria(Criteria.where("serviceName").is(logFilter.getServiceName()));
        }
        if (!isEmpty(logFilter.getMessage())) {
            criteria = criteria.addCriteria(Criteria.where("message").regex(format(".*%s.*", logFilter.getMessage()), "i"));
        }
        return mongoTemplate.find(criteria, Log.class);
    }

    public void saveLog(Log logResponse) {
        try {
            logRepository.save(logResponse);
        } catch (Exception ex) {
            log.error("Erro ao tentar salvar log recebido: ", ex);
        }
    }
}
