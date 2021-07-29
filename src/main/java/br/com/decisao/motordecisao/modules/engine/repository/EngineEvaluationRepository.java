package br.com.decisao.motordecisao.modules.engine.repository;

import br.com.decisao.motordecisao.modules.engine.document.EngineEvaluation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EngineEvaluationRepository extends MongoRepository<EngineEvaluation, String> {
}
