package br.com.decisao.motordecisao.config;

import br.com.decisao.motordecisao.config.exception.ValidacaoException;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Component
public class TransactionInterceptor implements HandlerInterceptor {

    private static final String OPTIONS_METHOD = "OPTIONS";
    private static final String TRANSACTION_ID = "transactionId";
    private static final String TRANSACTION_ID_NEEDED = "api/v1/decision/";

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        if (isOptions(request)) {
            return true;
        }
        if (request.getRequestURI().contains(TRANSACTION_ID_NEEDED)
            && ObjectUtils.isEmpty(request.getHeader(TRANSACTION_ID))) {
            throw new ValidacaoException("O header transactionId é obrigatório.");
        }
        request.setAttribute("serviceId", UUID.randomUUID().toString());
        return true;
    }

    private boolean isOptions(HttpServletRequest request) {
        return request.getMethod().equals(OPTIONS_METHOD);
    }
}
