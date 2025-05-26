package br.com.taurustech.gestor;

import br.com.taurustech.gestor.model.Conta;
import br.com.taurustech.gestor.model.dto.ContaDTO;
import br.com.taurustech.gestor.repository.ContaRepository;
import br.com.taurustech.gestor.service.ContaService;
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
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ContaTests extends BaseAPITest{
    private final ContaService contaService;
    private final ContaRepository contaRepository;

    ContaTests(ContaService contaService, ContaRepository contaRepository) {
        this.contaService = contaService;
        this.contaRepository = contaRepository;
    }

    @AfterEach
    void apagarCriados(){
        contaService.deletarTodosMenos(List.of(1,2,3));
    }

    private ResponseEntity<ContaDTO> getConta(String id) { return get("/api/v1/contas/" + id, ContaDTO.class); }

    private ResponseEntity<List<ContaDTO>> getListaContas(String status, String origem) {
        String url = "/api/v1/contas?";
        if (status != null) url = url + "status=" + status + "&";
        if (origem != null) url = url + "origem=" + origem + "&";
        if (url.endsWith("&") || url.endsWith("?")) url = url.substring(0, url.length() - 1);
        HttpHeaders headers = getHeaders();

        return rest.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                });
    }

    private void postConta(ContaDTO conta) {
        HttpHeaders headers = getHeaders();

        rest.exchange(
                "/api/v1/contas",
                HttpMethod.POST,
                new HttpEntity<>(conta, headers),
                Void.class
        );
    }

    @Test
    void testeBuscar(){
        var retorno = getConta("1");
        assertNotNull(retorno);
        assertNotNull(retorno.getBody());
        assertEquals("Conta teste", retorno.getBody().getDescricao());
        assertEquals(HttpStatus.OK, retorno.getStatusCode());
        var segundoRetorno = getConta("2");
        assertNotNull(segundoRetorno.getBody());
        assertNotEquals(retorno.getBody().getDescricao(), segundoRetorno.getBody().getDescricao());
    }

    @Test
    void testeListarContas(){
        definirLogin("teste");
        var retornoLista = getListaContas(null,null);
        assertNotNull(retornoLista.getBody());
        assertEquals(2, retornoLista.getBody().size());
        assertEquals(retornoLista.getBody().get(0).getUser(), retornoLista.getBody().get(1).getUser());

        retornoLista = getListaContas("abeRtA", null);
        assertNotNull(retornoLista.getBody());
        assertEquals("Conta teste",retornoLista.getBody().getFirst().getDescricao());
        assertEquals("ABERTA",retornoLista.getBody().getFirst().getStatus());

        retornoLista = getListaContas(null, "clieNte");
        assertNotNull(retornoLista.getBody());
        assertEquals("Segunda conta teste",retornoLista.getBody().getFirst().getDescricao());
        assertEquals("CLIENTE",retornoLista.getBody().getFirst().getOrigem());

        var todasContasExistentes =contaRepository.findAll();
        assertFalse(todasContasExistentes.isEmpty());
        assertTrue(todasContasExistentes.size()>2);

    }

    @Test
    void testePostConta()  {
        var dto = new ContaDTO();
        dto.setVencimento("2025-09-12");
        dto.setDescricao("criando conta direto do teste");
        dto.setValor("20.00");
        dto.setStatus("1");
        dto.setOrigem("1");
        dto.setCategoria("1");
        postConta(dto);
        var todos = contaRepository.findAll();
        assertFalse(todos.isEmpty());
        for (Conta a: todos){
            if (a.getDescricao().equals("criando conta direto do teste")) break;
        }
    }
}
