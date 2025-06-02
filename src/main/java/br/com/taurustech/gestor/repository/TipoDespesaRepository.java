package br.com.taurustech.gestor.repository;

import br.com.taurustech.gestor.model.TipoDespesa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoDespesaRepository extends JpaRepository<TipoDespesa, Integer> {
    TipoDespesa findByDescricaoIgnoreCase (String descricao);
}
