package br.com.taurustech.gestor.repository;

import br.com.taurustech.gestor.model.Banco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BancoRepository extends JpaRepository<Banco, Integer> {
    Banco findByDescricaoIgnoreCase (String descricao);
}
