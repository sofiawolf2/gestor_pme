package br.com.taurustech.gestor.controller;

import br.com.taurustech.gestor.model.dto.ContaDTO;
import br.com.taurustech.gestor.service.ContaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/contas")
@RequiredArgsConstructor
public class ContaController {
    private final ContaService service;

    @PostMapping
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<Void> cadastrar (@RequestBody ContaDTO contaDTO){
        service.cadastrar(contaDTO);
        return ResponseEntity.status(201).build();
    }

}
