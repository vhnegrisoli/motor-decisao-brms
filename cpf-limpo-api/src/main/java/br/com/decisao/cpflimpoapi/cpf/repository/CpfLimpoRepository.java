package br.com.decisao.cpflimpoapi.cpf.repository;

import br.com.decisao.cpflimpoapi.cpf.model.CpfLimpo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CpfLimpoRepository extends JpaRepository<CpfLimpo, Integer> {

    Optional<CpfLimpo> findByCpf(String cpf);
}
