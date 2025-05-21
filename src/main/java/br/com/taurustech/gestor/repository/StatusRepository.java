package br.com.taurustech.gestor.repository;

import br.com.taurustech.gestor.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Integer> {
    Status findByDescricaoIgnoreCase (String descricao);
}
