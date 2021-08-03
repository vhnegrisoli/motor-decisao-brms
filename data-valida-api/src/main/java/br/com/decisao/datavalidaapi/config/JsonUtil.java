package br.com.decisao.datavalidaapi.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JsonUtil {

    @Autowired
    private ObjectMapper objectMapper;
    
    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception ex) {
            log.error("Erro ao tentar converter objeto para JSON.", ex);
            return Strings.EMPTY;
        }
    }
}
