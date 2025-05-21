package br.com.taurustech.gestor.repository;

import br.com.taurustech.gestor.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaRepository extends JpaRepository<Conta, Integer> {
}
