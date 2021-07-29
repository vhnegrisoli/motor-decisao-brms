package br.com.decisao.motordecisao.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainControllerConfig {

    @GetMapping
    public String redirecionarParaSwagger() {
        return "redirect:swagger-ui.html";
    }
}