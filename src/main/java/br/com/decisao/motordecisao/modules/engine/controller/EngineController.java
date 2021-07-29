package br.com.decisao.motordecisao.modules.engine.controller;

import br.com.decisao.motordecisao.modules.data.dto.PayloadRequest;
import br.com.decisao.motordecisao.modules.engine.service.EngineOrchestrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/decision/engine")
public class EngineController {

    @Autowired
    private EngineOrchestrationService engineService;

    @PostMapping("run")
    public PayloadRequest runEngine(@RequestBody PayloadRequest body) {
        return engineService.runEngine(body);
    }
}
