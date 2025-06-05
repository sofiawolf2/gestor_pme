package br.com.taurustech.gestor;

import br.com.taurustech.gestor.model.Origem;
import br.com.taurustech.gestor.model.dto.MultiDTO;
import br.com.taurustech.gestor.repository.OrigemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(properties = "spring.datasource.url=jdbc:postgresql://localhost:5435/gestordb")
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL) @RunWith(SpringRunner.class)
class OrigemTests extends BaseAPITest{
    private final OrigemRepository repository;
    private final String url = "/api/v1/origens";
    
    OrigemTests(OrigemRepository repository) {
        this.repository = repository;
    }

    @AfterEach
    void apagarCriados(){
        repository.deleteAllExcept(List.of(1,2,3,4,5));
    }

    ResponseEntity<Origem> getOrigem(String id) { return get(url + "/"  + id, Origem.class); }

    void postOrigem(MultiDTO dto) { post(url, dto, Void .class);}

    void pacthOrigem(MultiDTO dto, String id) {patch(url + "/"  + id, dto, Void.class);}

    void deleteOrigem(String id) { delete(url + "/"  + id, Void.class); }

    ResponseEntity <List<Origem>> getListaOrigem() {
        HttpHeaders headers = getHeaders();


        return rest.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                });
    }

    @Test
    void testeBuscarByIdOrigem(){
        var retorno = getOrigem("1");
        assertNotNull(retorno.getBody());
        assertEquals(HttpStatus.OK, retorno.getStatusCode());
        assertNotNull(getOrigem("2").getBody());
    }

    @Test
    void testeListarOrigem(){
        var retorno = getListaOrigem();
        assertNotNull(retorno.getBody());
        assertTrue(retorno.getBody().size()>1);
        assertEquals(retorno.getBody().size(), repository.findAll().size() );

    }

    @Test
    void testePostDeleteFuncionario(){
        String descricao = "Teste PostDelete";
        assertNull(repository.findByDescricaoIgnoreCase(descricao)); // nao existe
        postOrigem(new MultiDTO(descricao)); // cria e salva
        assertNotNull(repository.findByDescricaoIgnoreCase(descricao)); // confirma que exite

        var id = repository.findByDescricaoIgnoreCase(descricao).getId(); // busca o id
        deleteOrigem(id.toString());
        assertNull(repository.findByDescricaoIgnoreCase(descricao)); // deleta e confirma que deletou
    }

    @Test
    void testePacthFuncionario(){
        var guardar = getOrigem("1");
        assertNotNull(guardar.getBody());
        String descricao = guardar.getBody().getDescricao();
        String novaDescricao = "Novo Pacth";

        assertNotEquals(novaDescricao, descricao);

        pacthOrigem(new MultiDTO (novaDescricao), "1");

        var depoisPacth = getOrigem("1");
        assertNotNull(depoisPacth.getBody());

        assertNotEquals(descricao , depoisPacth.getBody().getDescricao());
        assertEquals(novaDescricao.toUpperCase(), depoisPacth.getBody().getDescricao());

        pacthOrigem(new MultiDTO (descricao),"1");

    }
}
