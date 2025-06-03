package br.com.taurustech.gestor;

import br.com.taurustech.gestor.model.dto.FuncionarioDTO;
import br.com.taurustech.gestor.repository.FuncionarioRepository;
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
class FuncionarioTests extends BaseAPITest{
    private final FuncionarioRepository repository;
    private final String url = "/api/v1/funcionarios";

    public FuncionarioTests(FuncionarioRepository repository) {
        this.repository = repository;
    }

    @AfterEach
    void apagarCriados(){
        repository.deleteAllExcept(List.of(1,2));
    }

    ResponseEntity<FuncionarioDTO> getFuncionario(String id) { return get(url + "/" + id, FuncionarioDTO.class); }

    void postFuncionario(FuncionarioDTO dto) { post(url, dto, Void .class);}

    void pacthFuncionario(FuncionarioDTO dto, String id) {patch(url + "/" + id, dto, Void.class);}

    void deleteFuncionario(String id) { delete(url + "/" + id, Void.class); }

    FuncionarioDTO gerarFuncionario( String cpf, String telefone, String funcao){
        FuncionarioDTO dto = new FuncionarioDTO();
        dto.setNome("Joana");
        dto.setCpf(cpf);
        dto.setTelefone(telefone);
        dto.setFuncao(funcao);
        dto.setAtivo(false);
        dto.setSalario(1440.00);
        return dto;
    }

    ResponseEntity <List<FuncionarioDTO>> getListaFuncionario(String funcao) {
        var newUrl = url;
        if (funcao != null) newUrl = newUrl + "?funcao=" + funcao;
        HttpHeaders headers = getHeaders();

        return rest.exchange(
                newUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                });
    }

    @Test
    void testeBuscarByIdFuncionario(){
        var retorno = getFuncionario("1");
        assertNotNull(retorno.getBody());
        assertEquals(HttpStatus.OK, retorno.getStatusCode());
        assertTrue(retorno.getBody().getNome().contains("Ana"));

        assertNotNull(getFuncionario("2").getBody());
    }

    @Test
    void testeListarFuncionarios(){
        var retorno = getListaFuncionario(null);
        assertNotNull(retorno.getBody());
        assertTrue(retorno.getBody().size()>1);
        assertEquals(retorno.getBody().size(), repository.findAll().size() );

        retorno = getListaFuncionario("aNalIs");
        assertNotNull(retorno.getBody());
        assertEquals("ANALISTA", retorno.getBody().getFirst().getFuncao());

        retorno = getListaFuncionario("deSEnvO");
        assertNotNull(retorno.getBody());
        assertEquals("DESENVOLVEDOR", retorno.getBody().getFirst().getFuncao());
    }

    @Test
    void testePostDeleteFuncionario(){
        assertFalse(repository.existsByCpf("123.321.123-18")); // nao existe
        assertFalse(repository.existsByCpf("000.100.001-00"));

        postFuncionario(gerarFuncionario("123.321.123-18", "1231231231234","estagiAriO")); // cria e salva
        postFuncionario(gerarFuncionario("000.100.001-00", "1231231230000","anaListA"));

        assertTrue(repository.existsByCpf("000.100.001-00")); // confirma que exite
        assertTrue(repository.existsByCpf("123.321.123-18"));

        var id1 = repository.findByCpf("123.321.123-18").getId(); // busca o id
        var id2 = repository.findByCpf("000.100.001-00").getId();

        deleteFuncionario(id1.toString());
        assertFalse(repository.existsByCpf("123.321.123-18")); // deleta e confirma que deletou

        deleteFuncionario(id2.toString());
        assertFalse(repository.existsByCpf("000.100.001-00"));

    }

    @Test
    void testePacthFuncionario(){
        var guardar = getFuncionario("1");
        assertNotNull(guardar.getBody());
        var salarioAntes = guardar.getBody().getSalario();
        var telefoneAntes = guardar.getBody().getTelefone();


        assertNotEquals(2900.92, salarioAntes);
        assertNotEquals("5581900000000", telefoneAntes);

        var dto = new FuncionarioDTO ();
        dto.setTelefone("5581900000000");
        dto.setSalario(2900.92);

        pacthFuncionario(dto, "1");

        var depoisPacth = getFuncionario("1");
        assertNotNull(depoisPacth.getBody());

        assertNotEquals(telefoneAntes , depoisPacth.getBody().getTelefone());
        assertEquals("5581900000000", depoisPacth.getBody().getTelefone());
        assertNotEquals(salarioAntes , depoisPacth.getBody().getSalario());
        assertEquals(2900.92, depoisPacth.getBody().getSalario());

        dto.setSalario(salarioAntes);
        dto.setTelefone(telefoneAntes);
        pacthFuncionario(dto,"1");

    }
}
