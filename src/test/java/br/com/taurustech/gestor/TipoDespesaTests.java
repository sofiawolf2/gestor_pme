package br.com.taurustech.gestor;

import br.com.taurustech.gestor.model.TipoDespesa;
import br.com.taurustech.gestor.model.dto.MultiDTO;
import br.com.taurustech.gestor.repository.TipoDespesaRepository;
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
class TipoDespesaTests extends BaseAPITest{
    private final TipoDespesaRepository repository;
    private final String url = "/api/v1/tipo_despesa";
    
    TipoDespesaTests(TipoDespesaRepository repository) {
        this.repository = repository;
    }

    @AfterEach
    void apagarCriados(){
        repository.deleteAllExcept(List.of(1,2,3,4,5));
    }

    ResponseEntity<TipoDespesa> getTipoDespesa(String id) { return get(url + "/"  + id, TipoDespesa.class); }

    void postTipoDespesa(MultiDTO dto) { post(url, dto, Void .class);}

    void pacthTipoDespesa(MultiDTO dto, String id) {patch(url + "/"  + id, dto, Void.class);}

    void deleteTipoDespesa(String id) { delete(url + "/"  + id, Void.class); }

    ResponseEntity <List<TipoDespesa>> getListaTipoDespesa() {
        HttpHeaders headers = getHeaders();


        return rest.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                });
    }

    @Test
    void testeBuscarByIdTipoDespesa(){
        var retorno = getTipoDespesa("1");
        assertNotNull(retorno.getBody());
        assertEquals(HttpStatus.OK, retorno.getStatusCode());
        assertNotNull(getTipoDespesa("2").getBody());
    }

    @Test
    void testeListarTipoDespesa(){
        var retorno = getListaTipoDespesa();
        assertNotNull(retorno.getBody());
        assertTrue(retorno.getBody().size()>1);
        assertEquals(retorno.getBody().size(), repository.findAll().size() );

    }

    @Test
    void testePostDeleteFuncionario(){
        String descricao = "Teste PostDelete";
        assertNull(repository.findByDescricaoIgnoreCase(descricao)); // nao existe
        postTipoDespesa(new MultiDTO(descricao)); // cria e salva
        assertNotNull(repository.findByDescricaoIgnoreCase(descricao)); // confirma que exite

        var id = repository.findByDescricaoIgnoreCase(descricao).getId(); // busca o id
        deleteTipoDespesa(id.toString());
        assertNull(repository.findByDescricaoIgnoreCase(descricao)); // deleta e confirma que deletou
    }

    @Test
    void testePacthFuncionario(){
        var guardar = getTipoDespesa("1");
        assertNotNull(guardar.getBody());
        String descricao = guardar.getBody().getDescricao();
        String novaDescricao = "Novo Pacth";

        assertNotEquals(novaDescricao, descricao);

        pacthTipoDespesa(new MultiDTO (novaDescricao), "1");

        var depoisPacth = getTipoDespesa("1");
        assertNotNull(depoisPacth.getBody());

        assertNotEquals(descricao , depoisPacth.getBody().getDescricao());
        assertEquals(novaDescricao.toUpperCase(), depoisPacth.getBody().getDescricao());

        pacthTipoDespesa(new MultiDTO (descricao),"1");

    }
}
