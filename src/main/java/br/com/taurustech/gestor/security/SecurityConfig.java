package br.com.taurustech.gestor.security;

import br.com.taurustech.gestor.exception.ExceptionAccessDeniedHandler;
import br.com.taurustech.gestor.exception.UnauthorizedHandler;
import br.com.taurustech.gestor.model.User;
import br.com.taurustech.gestor.repository.UserRepository;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration @EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig implements SecurityFilterChain {

    private UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            User user = userRepository.findByLogin(username);
            if (user == null) {
                throw new UsernameNotFoundException("Usuário não encontrado: " + username);
            }
            return user;
        };
    }

    @Bean
    public AuthenticationManager authManager(){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService(userRepository));
        authProvider.setPasswordEncoder(encoder);
        return new ProviderManager(List.of(authProvider));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
        UnauthorizedHandler unauthorizedHandler = new UnauthorizedHandler();
        ExceptionAccessDeniedHandler exceptionAccessDeniedHandler = new ExceptionAccessDeniedHandler();
        http
                // Configuração de autorizações
                .authenticationManager(authManager)
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.GET, "/api/v1/login").permitAll() // Permitir login sem autenticação
                        .anyRequest().authenticated() // Todas as outras requisições exigem autenticação
                )
                // Configuração de CSRF
                .csrf(AbstractHttpConfigurer::disable) // CSRF desabilitado para APIs RESTful
                // Adicionando filtros personalizados
                .addFilter(new AuthenticationFilter(authManager)) // filtro de autenticação-> faz o login do usuário
                .addFilter(new AuthorizationFilter(authManager, userDetailsService(userRepository))) // filtro de autorização-> pega o token e ver se é valido
                // Configuração de exceções
                .exceptionHandling(exceptions -> exceptions
                        .accessDeniedHandler(exceptionAccessDeniedHandler) // Tratador de erros de acesso negado
                        .authenticationEntryPoint(unauthorizedHandler) // Tratador de erros de autenticação
                )
                // Gerenciamento de sessão
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // desligando a criação de cookies na sessão

        return http.build();
    }


    @Override
    public boolean matches(HttpServletRequest request) {
        return false;
    }

    @Override
    public List<Filter> getFilters() {
        return List.of();
    }

}
