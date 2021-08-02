package br.com.decisao.logapi.log.controller;

import br.com.decisao.logapi.log.document.Log;
import br.com.decisao.logapi.log.dto.LogFilter;
import br.com.decisao.logapi.log.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/logs")
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping
    public List<Log> findAll(LogFilter logFilter) {
        return logService.findAll(logFilter);
    }
}
