package br.com.decisao.cepvalidoapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CepValidoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CepValidoApiApplication.class, args);
	}
}
