package br.com.taurustech.gestor;

import br.com.taurustech.gestor.model.Role;
import br.com.taurustech.gestor.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(properties = "spring.datasource.url=jdbc:postgresql://localhost:5435/gestordb")
@SpringBootTest(classes = Application.class)
class RoleTests {
    @Autowired
    private RoleRepository repository;

    @Test
    void testeFindBy(){
        Role role = new Role();
        role.setNome("TESTE_EXISTS");
        role = repository.save(role);
        assertNotNull(repository.findByNomeIgnoreCase(role.getNome()));
        repository.delete(role);
        assertNull(repository.findByNomeIgnoreCase(role.getNome()));
    }

    @Test
    void testeEditarEncontrar(){
        Role role = new Role();
        role.setNome("TESTE_SALVAR");
        role = repository.save(role);
        Optional<Role> roleSalvo = repository.findById(role.getId());
        assert (roleSalvo.isPresent());
        role = roleSalvo.get();
        assertEquals("TESTE_SALVAR", role.getNome());


        role.setNome("MODIFICAR");
        repository.save(role);
        Optional<Role> roleModificado = repository.findById(role.getId());
        assert (roleModificado.isPresent());
        role = roleModificado.get();

        assertNotEquals("TESTE_SALVAR", role.getNome());

        repository.delete(role);
        assertNull(repository.findByNomeIgnoreCase( role.getNome()));
    }

}
