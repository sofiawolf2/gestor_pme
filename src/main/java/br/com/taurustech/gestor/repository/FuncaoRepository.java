package br.com.taurustech.gestor.repository;

import br.com.taurustech.gestor.model.Funcao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncaoRepository extends JpaRepository<Funcao, Integer> {
    Funcao findByDescricaoIgnoreCase (String descricao);
}
