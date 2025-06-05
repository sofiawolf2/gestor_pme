package br.com.taurustech.gestor;

import br.com.taurustech.gestor.model.Status;
import br.com.taurustech.gestor.model.dto.MultiDTO;
import br.com.taurustech.gestor.repository.StatusRepository;
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
class StatusTests extends BaseAPITest{
    private final StatusRepository repository;
    private final String url = "/api/v1/status";
    
    StatusTests(StatusRepository repository) {
        this.repository = repository;
    }

    @AfterEach
    void apagarCriados(){
        repository.deleteAllExcept(List.of(1,2,3,4,5,6,7));
    }

    ResponseEntity<Status> getStatus(String id) { return get(url + "/"  + id, Status.class); }

    void postStatus(MultiDTO dto) { post(url, dto, Void .class);}

    void pacthStatus(MultiDTO dto, String id) {patch(url + "/"  + id, dto, Void.class);}

    void deleteStatus(String id) { delete(url + "/"  + id, Void.class); }

    ResponseEntity <List<Status>> getListaStatus() {
        HttpHeaders headers = getHeaders();


        return rest.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                });
    }

    @Test
    void testeBuscarByIdStatus(){
        var retorno = getStatus("1");
        assertNotNull(retorno.getBody());
        assertEquals(HttpStatus.OK, retorno.getStatusCode());
        assertNotNull(getStatus("2").getBody());
    }

    @Test
    void testeListarStatus(){
        var retorno = getListaStatus();
        assertNotNull(retorno.getBody());
        assertTrue(retorno.getBody().size()>1);
        assertEquals(retorno.getBody().size(), repository.findAll().size() );

    }

    @Test
    void testePostDeleteFuncionario(){
        String descricao = "Teste PostDelete";
        assertNull(repository.findByDescricaoIgnoreCase(descricao)); // nao existe
        postStatus(new MultiDTO(descricao)); // cria e salva
        assertNotNull(repository.findByDescricaoIgnoreCase(descricao)); // confirma que exite

        var id = repository.findByDescricaoIgnoreCase(descricao).getId(); // busca o id
        deleteStatus(id.toString());
        assertNull(repository.findByDescricaoIgnoreCase(descricao)); // deleta e confirma que deletou
    }

    @Test
    void testePacthFuncionario(){
        var guardar = getStatus("1");
        assertNotNull(guardar.getBody());
        String descricao = guardar.getBody().getDescricao();
        String novaDescricao = "Novo Pacth";

        assertNotEquals(novaDescricao, descricao);

        pacthStatus(new MultiDTO (novaDescricao), "1");

        var depoisPacth = getStatus("1");
        assertNotNull(depoisPacth.getBody());

        assertNotEquals(descricao , depoisPacth.getBody().getDescricao());
        assertEquals(novaDescricao.toUpperCase(), depoisPacth.getBody().getDescricao());

        pacthStatus(new MultiDTO (descricao),"1");

    }
}
