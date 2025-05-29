package br.com.taurustech.gestor.repository;

import br.com.taurustech.gestor.model.TipoPix;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoPixRepository extends JpaRepository<TipoPix, Integer> {
    TipoPix findByDescricaoIgnoreCase (String descricao);
}
