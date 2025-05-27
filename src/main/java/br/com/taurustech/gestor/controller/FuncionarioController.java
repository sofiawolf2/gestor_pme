package br.com.taurustech.gestor.controller;

import br.com.taurustech.gestor.model.Funcionario;
import br.com.taurustech.gestor.service.FuncionarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/funcionarios")
@RequiredArgsConstructor
public class FuncionarioController {
    private final FuncionarioService service;

    @PostMapping
    @Secured({ "ROLE_ADMIN"})
    public ResponseEntity<Void> cadastrar (@RequestBody Funcionario funcionario){
        service.cadastrar(funcionario);
        return ResponseEntity.status(201).build();
    }
}
