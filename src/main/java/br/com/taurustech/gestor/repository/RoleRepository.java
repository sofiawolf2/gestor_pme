package br.com.taurustech.gestor.repository;

import br.com.taurustech.gestor.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByNomeIgnoreCase(String nome);
}
