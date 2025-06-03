package br.com.taurustech.gestor;

import br.com.taurustech.gestor.model.dto.PixDTO;
import br.com.taurustech.gestor.repository.PixRepository;
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
class PixTests extends BaseAPITest {
    private final PixRepository repository;
    private final String url = "/api/v1/pixs";

    public PixTests(PixRepository repository) {
        this.repository = repository;
    }

    @AfterEach
    void apagarCriados(){
        repository.deleteAllExcept(List.of(1,2));
    }

    ResponseEntity<PixDTO> getPix(String id) { return get(url + "/"  + id, PixDTO.class); }

    void postPix(PixDTO dto) { post("/api/v1/pixs", dto, Void .class);}

    void pacthPix(PixDTO dto, String id) {patch(url + "/"  + id, dto, Void.class);}

    void deletePix(String id) { delete(url + "/"  + id, Void.class); }

    ResponseEntity <List<PixDTO>> getListaPix(String tipoPix) {
        var newUrl = url;
        if (tipoPix != null) newUrl = newUrl + "?tipoPix=" + tipoPix;
        HttpHeaders headers = getHeaders();

        return rest.exchange(
                newUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                });
    }

    @Test
    void testeBuscarByIdPix(){
        var retorno = getPix("1");
        assertNotNull(retorno.getBody());
        assertEquals(HttpStatus.OK, retorno.getStatusCode());
        assertTrue(retorno.getBody().getDescricao().contains("Primeiro"));

        assertNotNull(getPix("2").getBody());
    }

    @Test
    void testeListarPix(){
        var retorno = getListaPix(null);
        assertNotNull(retorno.getBody());
        assertTrue(retorno.getBody().size()>1);
        assertEquals(retorno.getBody().size(), repository.findAll().size() );

        retorno = getListaPix("coBran");
        assertNotNull(retorno.getBody());
        assertEquals("COBRANÇA", retorno.getBody().getFirst().getTipoPix());

        retorno = getListaPix("tRanS");
        assertNotNull(retorno.getBody());
        assertEquals("TRANSFERÊNCIA", retorno.getBody().getFirst().getTipoPix());
    }

    @Test
    void testePostDeleteFuncionario(){
        String descricao = "Testando Post e Deelete";
        assertNull(repository.findByDescricaoIgnoreCase(descricao)); // nao existe
        postPix(new PixDTO(descricao, "2","troco")); // cria e salva
        assertNotNull(repository.findByDescricaoIgnoreCase(descricao)); // confirma que exite

        var id = repository.findByDescricaoIgnoreCase(descricao).getId(); // busca o id
        deletePix(id.toString());
        assertNull(repository.findByDescricaoIgnoreCase(descricao)); // deleta e confirma que deletou
    }

    @Test
    void testePacthFuncionario(){
        var guardar = getPix("1");
        assertNotNull(guardar.getBody());
        String descricao = guardar.getBody().getDescricao();
        String novaDescricao = "Novo teste de Pacth";

        assertNotEquals(novaDescricao, descricao);

        var dto = new PixDTO (novaDescricao);
        pacthPix(dto, "1");

        var depoisPacth = getPix("1");
        assertNotNull(depoisPacth.getBody());

        assertNotEquals(descricao , depoisPacth.getBody().getDescricao());
        assertEquals(novaDescricao, depoisPacth.getBody().getDescricao());

        dto.setDescricao(descricao);
        pacthPix(dto,"1");

    }
}
