package br.com.taurustech.gestor;

import br.com.taurustech.gestor.model.dto.ContaDTO;
import br.com.taurustech.gestor.repository.ContaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(properties = "spring.datasource.url=jdbc:postgresql://localhost:5435/gestordb")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class ContaTests extends BaseAPITest{
    private final ContaRepository contaRepository;
    String imagem64Exemplo = "iVBORw0KGgoAAAANSUhEUgAAAAcAAAAGCAYAAAAPDoR2AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAAvSURBVBhXYzQwMPjPgA6s8hkW+D9jYIJyUYCVqSHDyzMnGbDrhAKsOmGAXEkGBgCmsgjFVcuAawAAAABJRU5ErkJggg==";

    ContaTests( ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    @AfterEach
    void apagarCriados(){
        contaRepository.deleteAllExcept(List.of(1,2,3));
    }

    ResponseEntity<ContaDTO> getConta(String id) { return get("/api/v1/contas/" + id, ContaDTO.class); }

    ResponseEntity<byte[]> getContaImagem(String id) { return get("/api/v1/contas/" + id + "/png", byte[].class); }

    void deleteContaImagem(String id) { delete("/api/v1/contas/" + id + "/png", Void.class); }

    void postConta(ContaDTO conta) { post("/api/v1/contas", conta, Void .class);}

    void pacthConta(ContaDTO conta, String id) {patch("/api/v1/contas/" + id, conta, Void.class);}

    void deleteConta(String id) { delete("/api/v1/contas/" + id, Void.class); }

    ResponseEntity<List<ContaDTO>> getListaContas(String status, String origem) {
        String url = "/api/v1/contas?";
        if (status != null) url = url + "status=" + status + "&";
        if (origem != null) url = url + "origem=" + origem;
        if (url.endsWith("&") || url.endsWith("?")) url = url.substring(0, url.length() - 1);
        HttpHeaders headers = getHeaders();

        return rest.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                });
    }


    private ContaDTO gerarContaBasicaComDescricao (String descricao){
        var dto = new ContaDTO();

        dto.setVencimento(LocalDate.parse("2026-01-01"));
        dto.setDescricao(descricao);
        dto.setValor(Double.valueOf("100.00"));
        dto.setStatus("aberta");
        dto.setOrigem("fornecedor");
        dto.setCategoria("despesa fixa");

        return dto;
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
    void testePostEDeleteConta()  {
        String descricao = "criando conta direto do teste";
        var input = gerarContaBasicaComDescricao(descricao);
        assertTrue(contaRepository.findByDescricao(descricao).isEmpty()); // conta não salva

        postConta(input);

        var contaSalva = contaRepository.findByDescricao(descricao).getFirst();
        assertNotNull(contaSalva); // conta salva

        deleteConta(contaSalva.getId().toString()); // deletar conta por id

        assertTrue(contaRepository.findByDescricao(descricao).isEmpty()); // conta não existe mais
    }

    @Test
    void testePacth(){
        var output = getConta("3");
        assertNotNull(output.getBody());
        String categoriaAntes = output.getBody().getCategoria();
        String observacaoAntes = output.getBody().getObservacao();
        var contaPacth = new ContaDTO();
        contaPacth.setCategoria("DespESa fIxA");
        contaPacth.setObservacao("teste atualizando usando Pacth");

        pacthConta(contaPacth, "3");

        output = getConta("3");
        assertNotNull(output.getBody());

        assertNotEquals(categoriaAntes, output.getBody().getCategoria());
        assertNotEquals(observacaoAntes, output.getBody().getObservacao());

        assertEquals("DESPESA FIXA", output.getBody().getCategoria());
        assertEquals("teste atualizando usando Pacth", output.getBody().getObservacao());

        // desfazendo mudanças:
        contaPacth.setCategoria(categoriaAntes);
        contaPacth.setObservacao(observacaoAntes);
        pacthConta(contaPacth, "3");
    }

    @Test void testeGetDeleteImagem(){
        var contaRetorno = getConta("1");
        assertNotNull(contaRetorno.getBody()); // veio uma conta
        assertNull((contaRetorno.getBody().getImagem())); // a conta não tem imagem
        assertEquals(HttpStatus.NOT_FOUND, getContaImagem("1").getStatusCode()); // imagem não encontrada

        var dto = new ContaDTO();
        dto.setImagem(imagem64Exemplo);
        pacthConta(dto,"1");// salva imagem

        var imagemRetorno = getContaImagem("1");
        assertNotNull(imagemRetorno.getBody()); // veio uma imagem
        assertNotEquals(HttpStatus.NOT_FOUND, imagemRetorno.getStatusCode()); // imagem encontrada
        assertEquals(byte[].class, imagemRetorno.getBody().getClass()); // imagem do tipo byte[]

        deleteContaImagem("1"); //deleta imagem

        //imagem não existe mais
        contaRetorno = getConta("1");
        assertNotNull(contaRetorno.getBody()); // veio uma conta
        assertNull((contaRetorno.getBody().getImagem())); // a conta não tem imagem
        assertEquals(HttpStatus.NOT_FOUND, getContaImagem("1").getStatusCode()); // imagem não encontrada
    }

}
