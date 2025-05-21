package br.com.taurustech.gestor.repository;

import br.com.taurustech.gestor.model.Origem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrigemRepository extends JpaRepository<Origem, Integer> {
    Origem findByDescricaoIgnoreCase (String descricao);
}
