package br.com.decisao.logapi.log.repository;

import br.com.decisao.logapi.log.document.Log;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogRepository extends MongoRepository<Log, String> {
}
