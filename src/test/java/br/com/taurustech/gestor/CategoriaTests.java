package br.com.taurustech.gestor;

import br.com.taurustech.gestor.model.Categoria;
import br.com.taurustech.gestor.model.dto.MultiDTO;
import br.com.taurustech.gestor.repository.CategoriaRepository;
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
class CategoriaTests extends BaseAPITest{
    private final CategoriaRepository repository;
    private final String url = "/api/v1/categorias";
    
    CategoriaTests(CategoriaRepository repository) {
        this.repository = repository;
    }

    @AfterEach
    void apagarCriados(){
        repository.deleteAllExcept(List.of(1,2,3,4,5,6));
    }

    ResponseEntity<Categoria> getCategoria(String id) { return get(url + "/"  + id, Categoria.class); }

    void postCategoria(MultiDTO dto) { post(url, dto, Void .class);}

    void pacthCategoria(MultiDTO dto, String id) {patch(url + "/"  + id, dto, Void.class);}

    void deleteCategoria(String id) { delete(url + "/"  + id, Void.class); }

    ResponseEntity <List<Categoria>> getListaCategoria() {
        HttpHeaders headers = getHeaders();


        return rest.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                });
    }

    @Test
    void testeBuscarByIdCategoria(){
        var retorno = getCategoria("1");
        assertNotNull(retorno.getBody());
        assertEquals(HttpStatus.OK, retorno.getStatusCode());
        assertNotNull(getCategoria("2").getBody());
    }

    @Test
    void testeListarCategoria(){
        var retorno = getListaCategoria();
        assertNotNull(retorno.getBody());
        assertTrue(retorno.getBody().size()>1);
        assertEquals(retorno.getBody().size(), repository.findAll().size() );

    }

    @Test
    void testePostDeleteFuncionario(){
        String descricao = "Teste PostDelete";
        assertNull(repository.findByDescricaoIgnoreCase(descricao)); // nao existe
        postCategoria(new MultiDTO(descricao)); // cria e salva
        assertNotNull(repository.findByDescricaoIgnoreCase(descricao)); // confirma que exite

        var id = repository.findByDescricaoIgnoreCase(descricao).getId(); // busca o id
        deleteCategoria(id.toString());
        assertNull(repository.findByDescricaoIgnoreCase(descricao)); // deleta e confirma que deletou
    }

    @Test
    void testePacthFuncionario(){
        var guardar = getCategoria("1");
        assertNotNull(guardar.getBody());
        String descricao = guardar.getBody().getDescricao();
        String novaDescricao = "Novo Pacth";

        assertNotEquals(novaDescricao, descricao);

        pacthCategoria(new MultiDTO (novaDescricao), "1");

        var depoisPacth = getCategoria("1");
        assertNotNull(depoisPacth.getBody());

        assertNotEquals(descricao , depoisPacth.getBody().getDescricao());
        assertEquals(novaDescricao.toUpperCase(), depoisPacth.getBody().getDescricao());

        pacthCategoria(new MultiDTO (descricao),"1");

    }
}
