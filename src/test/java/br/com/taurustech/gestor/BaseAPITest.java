package br.com.taurustech.gestor;

import br.com.taurustech.gestor.model.Role;
import br.com.taurustech.gestor.model.User;
import br.com.taurustech.gestor.model.dto.UserDTO;
import br.com.taurustech.gestor.security.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.springframework.http.HttpMethod.*;
public abstract class BaseAPITest {
    @Autowired
    protected TestRestTemplate rest;
    @Autowired
    @Qualifier("userDetailsService")
    protected UserDetailsService userDetailsService;

    private String jwtToken = "";

    HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwtToken);
        return headers;
    }

    @BeforeEach
    public void setupTest(){
        definirLogin("dev");
    }
    public void definirLogin(String login) {
        UserDetails user = userDetailsService.loadUserByUsername(login);
        Assertions.assertNotNull(user);
        jwtToken = Util.createToken(user);
        System.out.println(jwtToken);
        Assertions.assertNotNull(jwtToken);

        SecurityContextHolder.clearContext();
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, jwtToken, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    <T> ResponseEntity<T> post(String url, Object body, Class<T> responseType) {
        HttpHeaders headers = getHeaders();

        return rest.exchange(url, POST, new HttpEntity<>(body, headers), responseType);
    }

    <T> ResponseEntity<T> put(String url, Object body, Class<T> responseType) {
        HttpHeaders headers = getHeaders();

        return rest.exchange(url, PUT, new HttpEntity<>(body, headers), responseType);
    }
    <T> ResponseEntity<T> patch(String url, Object body, Class<T> responseType) {
        HttpHeaders headers = getHeaders();

        return rest.exchange(url, PATCH, new HttpEntity<>(body, headers), responseType);
    }

    <T> ResponseEntity<T> get(String url, Class<T> responseType) {

        HttpHeaders headers = getHeaders();

        return rest.exchange(url, GET, new HttpEntity<>(headers), responseType);
    }

    <T> ResponseEntity<T> delete(String url, Class<T> responseType) {

        HttpHeaders headers = getHeaders();

        return rest.exchange(url, DELETE, new HttpEntity<>(headers), responseType);
    }

    protected UserDTO gerarUser(String nome, String login, Role role){
        User user = new User();
        if (nome==null) user.setNome("Teste");
        else user.setNome(nome);
        user.setLogin(login);
        user.setEmail("Teste@gmail.com");
        user.setSenha("123");
        user.setRole(role);
        return UserDTO.createInput(user);
    }

}