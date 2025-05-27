package br.com.taurustech.gestor;

import br.com.taurustech.gestor.model.dto.UserDTO;
import br.com.taurustech.gestor.service.RoleService;
import br.com.taurustech.gestor.service.UserService;
import jakarta.validation.ConstraintViolationException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestPropertySource(properties = "spring.datasource.url=jdbc:postgresql://localhost:5435/gestordb")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class UserTests extends BaseAPITest{

    private final UserService service;
    private final RoleService roleService;

    public UserTests(UserService service, RoleService roleService) {
        this.service = service;
        this.roleService = roleService;
    }

    private ResponseEntity<UserDTO> getUser(String id) {
        return get("/api/v1/usuarios/" + id, UserDTO.class);
    }

    private void pacthUser(UserDTO user, String id) { patch("/api/v1/usuarios/" + id, user, Void.class);}

    private void deleteUser(String id) { delete("/api/v1/usuarios/" + id, Void.class);}

    private ResponseEntity<List<UserDTO>> getListaUsers(String nome, String login) {
        String url = "/api/v1/usuarios?";
        if (nome != null) url = url + "nome=" + nome + "&";
        if (login != null) url = url + "login=" + login;
        if (url.endsWith("&") || url.endsWith("?")) url = url.substring(0, url.length() - 1);
        HttpHeaders headers = getHeaders();

        return rest.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                });
    }


    @AfterEach
    void apagarCriados(){
        service.deletarTodosMenosLogin(List.of("dev","teste"));
    }

    @Test
    void testeServiceRegistroDuplo(){
        var uuid1 = service.getID(service.cadastro(gerarUser(null,"sofiaUser", roleService.buscarNome("ROLE_USER"))));
        assertNotNull(service.buscarOutID(uuid1.toString()));

        var sofiaUser = service.buscarLogin("sofiaUser");
        assertEquals(uuid1,  sofiaUser.getId());

        var amanda = gerarUser(null,"sofiaUser", roleService.buscarNome("ROLE_USER"));

        try {
            service.cadastro(amanda);
        } catch (ConstraintViolationException e){
            amanda.setLogin("amandaUser");
            service.cadastro(amanda);
        }
        var amandaUser = service.buscarLogin("amandaUser");
        assertNotNull(amandaUser);
    }

    @Test
    void testeEditarService(){
        service.cadastro(gerarUser(null,"EditarTeste", roleService.buscarNome("ROLE_USER")));
        var user = service.buscarLogin("EditarTeste");
        user.setLogin("Editado");
        service.atualizarPatch(UserDTO.createInput(user), user.getId().toString());
        assertEquals("Editado", service.buscarOutID(user.getId().toString()).getLogin());
    }

    @Test
    void testeGetById (){
        ResponseEntity<UserDTO> lista = getUser("e9b1f85d-4a58-4c2e-bb8b-3a41f8a9d1c7");
        assertNotNull(lista.getBody());
        assertEquals("dev", lista.getBody().getLogin());

    }
    @Test
    void testePost(){
        var retorno = post("/api/v1/usuarios", gerarUser(null,"testePost", roleService.buscarNome("ROLE_USER")), UserDTO.class);
        assertEquals(HttpStatus.CREATED, retorno.getStatusCode());
        assertNotNull(service.buscarLogin("testePost"));

    }

    @Test
    void pesquisaPorAtributo(){
        service.cadastro(gerarUser("Ana", "anaBia",roleService.buscarNome("ROLE_USER")));
        service.cadastro(gerarUser("Sofia", "sofiaw", roleService.buscarNome("ROLE_USER")));
        service.cadastro(gerarUser("Carlos", "car", roleService.buscarNome("ROLE_USER")));
        service.cadastro(gerarUser("Bia", "bb", roleService.buscarNome("ROLE_USER")));

        ResponseEntity<List<UserDTO>> lista = getListaUsers("los", null);
        assertNotNull(lista.getBody());
        assertEquals("car", lista.getBody().getFirst().getLogin());

        lista = getListaUsers(null, "bb");
        assertNotNull(lista.getBody());
        assertEquals("bb", lista.getBody().getFirst().getLogin()); // aparece b primeiro
        assertEquals(1, lista.getBody().size()); // o login esta inteiro igual

        lista = getListaUsers("N", "B");
        assertNotNull(lista.getBody());
        assertEquals("anaBia", lista.getBody().getFirst().getLogin()); // bate com as duas definições

        try {
            getListaUsers("soani", null);
        } catch ( Exception e) {
            System.out.println("Não foi encontrado o usuário informado");
            // eu gero um objetoNotFoundErro e depois trato retornanedo um ResponseEntity not found. O problema é que ele estava esperando um UserViewDTO então
            //não consegue fazer a conversão e isso gera um erro. Pois ele ainda esta esperando a resposta pra retornar aqui pra mim.
        }
    }

    @Test
    void testePut(){
        var id = service.getID(service.cadastro(gerarUser("Sofia", "uau", roleService.buscarNome("ROLE_USER"))));
        var user = service.buscarLogin("uau");
        assertNotNull(user);
        assertEquals("Sofia", user.getNome());
        user.setNome("Carla");
        pacthUser(UserDTO.createInput(user), id.toString());
        var userAtualizada = service.buscarOutID(id.toString());
        assertEquals("Carla", userAtualizada.getNome());
        assertEquals("uau", userAtualizada.getLogin());
    }

    @Test
    void testeDelete(){
        var teste = gerarUser("Teste", "deletar", roleService.buscarNome("ROLE_USER"));
        var id = service.getID(service.cadastro(teste));
        var user = service.buscarOutID(id.toString());
        assertNotNull(user);
        assertEquals("Teste", user.getNome());
        deleteUser(id.toString());
        var novoRetorno = getUser(id.toString());
        assertEquals(HttpStatus.NOT_FOUND, novoRetorno.getStatusCode());

    }


}
