package br.com.decisao.motordecisao.modules.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @JsonProperty("document")
    private String cpf;

    @JsonProperty("birthday")
    private LocalDate dataNascimento;

}
