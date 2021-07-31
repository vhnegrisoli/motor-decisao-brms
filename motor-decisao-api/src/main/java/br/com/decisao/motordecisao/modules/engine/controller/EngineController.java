package br.com.decisao.motordecisao.modules.engine.controller;

import br.com.decisao.motordecisao.modules.data.dto.PayloadRequest;
import br.com.decisao.motordecisao.modules.engine.document.EngineEvaluation;
import br.com.decisao.motordecisao.modules.engine.service.EngineOrchestrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/decision/engine")
public class EngineController {

    @Autowired
    private EngineOrchestrationService engineService;

    @PostMapping("run")
    public EngineEvaluation runEngine(@RequestBody PayloadRequest body,
                                      @RequestHeader String transactionId) {
        return engineService.runEngine(body);
    }

    @GetMapping("{id}")
    public EngineEvaluation findById(@PathVariable String id,
                                     @RequestHeader String transactionId) {
        return engineService.findById(id);
    }

    @GetMapping("engine-evaluation/{evaluationId}")
    public EngineEvaluation findByEvaluationId(@PathVariable String evaluationId,
                                               @RequestHeader String transactionId) {
        return engineService.findByEvaluationId(evaluationId);
    }
}
