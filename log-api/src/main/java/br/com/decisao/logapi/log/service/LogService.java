package br.com.decisao.logapi.log.service;

import br.com.decisao.logapi.log.dto.LogFilter;
import br.com.decisao.logapi.log.predicate.LogPredicate;
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

    @Autowired
    private LogPredicate logPredicate;

    public List<Log> findAll(LogFilter logFilter) {
        return mongoTemplate.find(logPredicate.definePredicate(logFilter), Log.class);
    }

    public void saveLog(Log logResponse) {
        try {
            logRepository.save(logResponse);
        } catch (Exception ex) {
            log.error("Erro ao tentar salvar log recebido: ", ex);
        }
    }
}
