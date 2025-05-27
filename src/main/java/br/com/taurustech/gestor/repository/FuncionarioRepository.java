package br.com.taurustech.gestor.repository;

import br.com.taurustech.gestor.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {

}
