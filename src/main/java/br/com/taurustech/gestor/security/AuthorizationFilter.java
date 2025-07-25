package br.com.taurustech.gestor.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.List;

public class AuthorizationFilter extends BasicAuthenticationFilter {
    private static final Logger logger_ = LoggerFactory.getLogger(AuthorizationFilter.class);

    private final UserDetailsService userDetailsService;

    public AuthorizationFilter(AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        String token = request.getHeader("Authorization");// leu o header

        if (StringUtils.isEmpty(token) || !token.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            if(! Util.isTokenValid(token)) {
                throw new AccessDeniedException("Acesso negado.");
            }

            String login = Util.getLogin(token);

            UserDetails userDetails = userDetailsService.loadUserByUsername(login);

            List<GrantedAuthority> authorities = Util.getRoles(token);

            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);


            SecurityContextHolder.getContext().setAuthentication(auth);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            logger.info("Usuário autenticado: " + authentication.getName());
            logger.info("Roles: " + authentication.getAuthorities());
            filterChain.doFilter(request, response);

        } catch (RuntimeException ex) {
            logger_.error("Authentication error: {}", ex.getMessage(), ex);
        }
    }
}
