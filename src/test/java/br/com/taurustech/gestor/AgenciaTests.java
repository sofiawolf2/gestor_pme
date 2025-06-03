package br.com.taurustech.gestor;

import br.com.taurustech.gestor.model.dto.AgenciaDTO;
import br.com.taurustech.gestor.repository.AgenciaRepository;
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
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestPropertySource(properties = "spring.datasource.url=jdbc:postgresql://localhost:5435/gestordb")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class AgenciaTests extends BaseAPITest{
    private final AgenciaRepository repository;
    private final String url = "/api/v1/agencias";

    public AgenciaTests(AgenciaRepository repository) {
        this.repository = repository;
    }

    @AfterEach
    void apagarCriados(){
        repository.deleteAllExcept(List.of(1,2));
    }

    ResponseEntity<AgenciaDTO> getAgencia(String id) { return get(url + "/"  + id, AgenciaDTO.class); }

    void postAgencia(AgenciaDTO dto) { post( url, dto, Void .class);}

    void pacthAgencia(AgenciaDTO dto, String id) {patch(url + "/"  + id, dto, Void.class);}

    void deleteAgencia(String id) { delete(url + "/"  + id, Void.class); }

    ResponseEntity <List<AgenciaDTO>> getListaAgencia(String tipoConta, String banco) {
        var newUrl = url + "?";
        if (tipoConta != null) newUrl = newUrl + "tipoConta=" + tipoConta + "&";
        if (banco != null) newUrl = newUrl + "banco=" + banco;
        if (newUrl.endsWith("&") || newUrl.endsWith("?")) newUrl = newUrl.substring(0, newUrl.length() - 1);
        HttpHeaders headers = getHeaders();

        return rest.exchange(
                newUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                });
    }

    @Test
    void testeBuscarByIdAgencia(){
        var retorno = getAgencia("1");
        assertNotNull(retorno.getBody());
        assertEquals(HttpStatus.OK, retorno.getStatusCode());
        assertTrue(retorno.getBody().getNome().contains("Primeiro"));

        assertTrue(Objects.requireNonNull(getAgencia("2").getBody()).getNome().contains("Segundo"));
    }

    @Test
    void testeListarAgencia(){
        var retorno = getListaAgencia(null, null);
        assertNotNull(retorno.getBody());
        assertTrue(retorno.getBody().size()>1);
        assertEquals(retorno.getBody().size(), repository.findAll().size() );

        retorno = getListaAgencia("corR", null);
        assertNotNull(retorno.getBody());
        assertEquals("CORRENTE", retorno.getBody().getFirst().getTipoConta());

        retorno = getListaAgencia("pou", null);
        assertNotNull(retorno.getBody());
            assertEquals("POUPANÇA", retorno.getBody().getFirst().getTipoConta());

        retorno = getListaAgencia(null, "brasil");
        assertNotNull(retorno.getBody());
        assertEquals("Banco do Brasil", retorno.getBody().getFirst().getBanco());

        retorno = getListaAgencia(null, "it");
        assertNotNull(retorno.getBody());
        assertEquals("Itau", retorno.getBody().getFirst().getBanco());
    }

    @Test
    void testePostDeleteFuncionario(){
        String nome = "Post e Deelete";
        assertNull(repository.findByNomeIgnoreCase(nome)); // nao existe
        postAgencia(new AgenciaDTO( nome, "post delete", "bradesco", "1", "digital")); // cria e salva
        assertNotNull(repository.findByNomeIgnoreCase(nome)); // confirma que exite

        var id = repository.findByNomeIgnoreCase(nome).getId(); // busca o id
        deleteAgencia(id.toString());
        assertNull(repository.findByNomeIgnoreCase(nome)); // deleta e confirma que deletou
    }

    @Test
    void testePacthFuncionario(){
        var guardar = getAgencia("1");
        assertNotNull(guardar.getBody());
        String nome = guardar.getBody().getNome();
        String novoNome = "Pacth Teste";

        String banco = guardar.getBody().getBanco();

        assertNotEquals(novoNome, nome);
        assertNotEquals("Santander", banco);

        var dto = new AgenciaDTO (novoNome,null,"sAntAnDer",null,null);
        pacthAgencia(dto, "1");

        var depoisPacth = getAgencia("1");
        assertNotNull(depoisPacth.getBody());

        assertNotEquals(nome , depoisPacth.getBody().getNome());
        assertEquals(novoNome, depoisPacth.getBody().getNome());
        assertNotEquals(banco , depoisPacth.getBody().getBanco());
        assertEquals("Santander", depoisPacth.getBody().getBanco());

        dto.setNome(nome);
        dto.setBanco(banco);
        pacthAgencia(dto,"1");

    }
}
