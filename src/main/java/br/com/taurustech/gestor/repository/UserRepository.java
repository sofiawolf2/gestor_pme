package br.com.taurustech.gestor.repository;

import br.com.taurustech.gestor.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByLogin (String login);

    @Query("SELECT u FROM User u JOIN u.role r WHERE LOWER(r.nome) LIKE LOWER(CONCAT('%', :nomeRole, '%'))")
    List<User> findByRoleNomeContainingIgnoreCase(@Param("nomeRole") String nomeRole);

    @Modifying
    @Transactional
    @Query("DELETE FROM User u WHERE u.login NOT IN (:logins)")
    void deleteAllExcept(@Param("logins") List<String> logins);

    @Query("SELECT u FROM User u JOIN u.role r WHERE r.nome = 'ROLE_ADMIN' ")
    User findAdmin ();
}
