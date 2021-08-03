package br.com.decisao.cepvalidoapi.cep.client;

import br.com.decisao.cepvalidoapi.cep.dto.CepResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(
    name = "cepClient",
    contextId = "cepClient",
    url = "${app-config.services.viacep.uri}"
)
public interface CepClient {

    @GetMapping("{cep}/json")
    Optional<CepResponse> buscarViaCep(@PathVariable String cep);
}
