package br.com.taurustech.gestor;

import br.com.taurustech.gestor.model.dto.DespFuncDTO;
import br.com.taurustech.gestor.repository.DespFuncRepository;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestPropertySource(properties = "spring.datasource.url=jdbc:postgresql://localhost:5435/gestordb")
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL) @RunWith(SpringRunner.class)
class DespFuncTests extends BaseAPITest{
    private final DespFuncRepository repository;
    private final String url = "/api/v1/despesaFuncionarios";

    DespFuncTests(DespFuncRepository repository) {
        this.repository = repository;
    }
    @AfterEach
    void apagarCriados(){
        repository.deleteAllExcept(List.of(1,2));
    }

    ResponseEntity<DespFuncDTO> getDF(String id) { return get(url + "/" + id, DespFuncDTO.class); }

    void postDF(DespFuncDTO dto) { post(url, dto, Void .class);}

    void pacthDF(DespFuncDTO dto, String id) {patch(url + "/" + id, dto, Void.class);}

    void deleteDF(String id) { delete(url + "/" + id, Void.class); }

    ResponseEntity <List<DespFuncDTO>> getListaDF(String tipoDespesa) {
        var newUrl = url;
        if (tipoDespesa != null) newUrl = newUrl + "?tipoDespesa=" + tipoDespesa;
        HttpHeaders headers = getHeaders();

        return rest.exchange(
                newUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                });
    }

    @Test
    void testeBuscarByIdDF(){
        var retorno = getDF("1");
        assertNotNull(retorno.getBody());
        assertEquals(HttpStatus.OK, retorno.getStatusCode());
        assertTrue(retorno.getBody().getObservacao().contains("Primeira"));

        assertNotNull(getDF("2").getBody());
    }

    @Test
    void testeListarDF(){
        var retorno = getListaDF(null);
        assertNotNull(retorno.getBody());
        assertTrue(retorno.getBody().size()>1);
        assertEquals(retorno.getBody().size(), repository.findAll().size() );

        retorno = getListaDF("fiX");
        assertNotNull(retorno.getBody());
        assertEquals("FIXA", retorno.getBody().getFirst().getTipoDespesa());

        retorno = getListaDF("opEr");
        assertNotNull(retorno.getBody());
        assertEquals("OPERACIONAL", retorno.getBody().getFirst().getTipoDespesa());
    }

    @Test
    void testePostDeleteDF(){
        String observacao = "Testando Post e Deelete";
        assertNull(repository.findByObservacaoIgnoreCase(observacao)); // nao existe
        postDF(new DespFuncDTO("1", 3849.00, observacao, "financeira" )); // cria e salva
        assertNotNull(repository.findByObservacaoIgnoreCase(observacao)); // confirma que exite

        var id = repository.findByObservacaoIgnoreCase(observacao).getId(); // busca o id
        deleteDF(id.toString());
        assertNull(repository.findByObservacaoIgnoreCase(observacao)); // deleta e confirma que deletou
    }

    @Test
    void testePacthDF(){
        var guardar = getDF("1");
        assertNotNull(guardar.getBody());
        String observacao = guardar.getBody().getObservacao();
        String despesa = guardar.getBody().getTipoDespesa();
        String novaObservacao = "Novo teste de Pacth";

        assertNotEquals(novaObservacao, observacao);

        var dto = new DespFuncDTO(null, null, novaObservacao, "vAriaveL");
        pacthDF(dto, "1");

        var depoisPacth = getDF("1");
        assertNotNull(depoisPacth.getBody());

        assertNotEquals(observacao , depoisPacth.getBody().getObservacao());
        assertEquals(novaObservacao, depoisPacth.getBody().getObservacao());

        assertNotEquals(despesa , depoisPacth.getBody().getTipoDespesa());
        assertEquals("VARIAVEL", depoisPacth.getBody().getTipoDespesa());

        dto.setObservacao(observacao);
        dto.setTipoDespesa(despesa);
        pacthDF(dto,"1");

    }
}
