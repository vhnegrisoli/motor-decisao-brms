package br.com.decisao.logapi.log.predicate;

import br.com.decisao.logapi.log.dto.LogFilter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import static java.lang.String.format;
import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class LogPredicate {

    public Query definePredicate(LogFilter logFilter) {
        var criteria = new Criteria();

        filterTransactionId(criteria, logFilter);
        filterServiceId(criteria, logFilter);
        filterServiceName(criteria, logFilter);
        filterLogLevel(criteria, logFilter);
        filterMessage(criteria, logFilter);

        return new Query(criteria);
    }

    private void filterTransactionId(Criteria criteria, LogFilter logFilter) {
        if (!isEmpty(logFilter.getTransactionId())) {
            criteria.and("transactionId").is(logFilter.getTransactionId());
        }
    }

    private void filterServiceId(Criteria criteria, LogFilter logFilter) {
        if (!isEmpty(logFilter.getServiceId())) {
            criteria.and("serviceId").is(logFilter.getServiceId());
        }
    }

    private void filterServiceName(Criteria criteria, LogFilter logFilter) {
        if (!isEmpty(logFilter.getServiceName())) {
            criteria.and("serviceName").is(logFilter.getServiceName());
        }
    }

    private void filterLogLevel(Criteria criteria, LogFilter logFilter) {
        if (!isEmpty(logFilter.getLogLevel())) {
            criteria.and("logLevel").is(logFilter.getLogLevel());
        }
    }

    private void filterMessage(Criteria criteria, LogFilter logFilter) {
        if (!isEmpty(logFilter.getMessage())) {
            criteria.and("message").regex(format(".*%s.*", logFilter.getMessage()), "i");
        }
    }
}
