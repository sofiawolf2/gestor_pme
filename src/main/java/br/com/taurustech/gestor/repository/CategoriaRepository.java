package br.com.taurustech.gestor.repository;

import br.com.taurustech.gestor.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    Categoria findByDescricaoIgnoreCase (String descricao);
}
