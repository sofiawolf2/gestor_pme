package br.com.taurustech.gestor.repository;

import br.com.taurustech.gestor.model.TipoConta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoContaRepository extends JpaRepository<TipoConta, Integer> {
    TipoConta findByDescricaoIgnoreCase (String descricao);
}
