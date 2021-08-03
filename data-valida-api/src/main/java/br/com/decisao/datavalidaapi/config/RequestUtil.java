package br.com.decisao.datavalidaapi.config;

import br.com.decisao.datavalidaapi.config.exception.ValidacaoException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {

    public static HttpServletRequest getCurrentRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes())
                .getRequest();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ValidacaoException("Erro ao tentar recuperar o request atual.");
        }
    }
}
