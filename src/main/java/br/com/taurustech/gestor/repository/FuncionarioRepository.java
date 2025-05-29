package br.com.taurustech.gestor.repository;

import br.com.taurustech.gestor.model.Funcionario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Funcionario u WHERE u.id NOT IN (:id)")
    void deleteAllExcept(@Param("id") List<Integer> id);

    boolean existsByCpf(String cpf);
    Funcionario findByCpf(String cpf);
    boolean existsByTelefone(String telefone);
}
