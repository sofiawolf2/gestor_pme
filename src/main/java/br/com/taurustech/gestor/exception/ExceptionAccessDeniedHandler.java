package br.com.taurustech.gestor.exception;

import br.com.taurustech.gestor.security.ServletUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Classe chamada quando acontece o erro 403 - FORBIDDEN
 */
@Component
public class ExceptionAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException exc) throws IOException {

        Authentication auth
                = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {

            String json = ServletUtil.getJson("error", "Acesso negado.");
            ServletUtil.write(response, HttpStatus.FORBIDDEN, json);
        }
    }
}
