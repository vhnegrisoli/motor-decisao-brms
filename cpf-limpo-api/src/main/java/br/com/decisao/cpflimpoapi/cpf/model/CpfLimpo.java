package br.com.decisao.cpflimpoapi.cpf.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CPF_LIMPO")
public class CpfLimpo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(name = "CPF", nullable = false, unique = true)
    private String cpf;

    @Column(name = "LIMPO", nullable = false)
    private boolean limpo;
}
