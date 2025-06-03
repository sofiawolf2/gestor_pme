package br.com.taurustech.gestor.repository;

import br.com.taurustech.gestor.model.Banco;
import br.com.taurustech.gestor.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BancoRepository extends JpaRepository<Banco, Integer> {
    Categoria findByDescricaoIgnoreCase (String descricao);
}
