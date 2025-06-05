package br.com.taurustech.gestor.repository;

import br.com.taurustech.gestor.model.Categoria;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    Categoria findByDescricaoIgnoreCase (String descricao);
    @Modifying
    @Transactional
    @Query("DELETE FROM Categoria u WHERE u.id NOT IN (:id)")
    void deleteAllExcept(@Param("id") List<Integer> id);
}
