package br.com.taurustech.gestor.controller;

import br.com.taurustech.gestor.model.dto.ContaDTO;
import br.com.taurustech.gestor.service.ContaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<List<ContaDTO>> listarContas(@RequestParam (required = false) String status,
                                                        @RequestParam (required = false) String origem,
                                                        @RequestParam (required = false) String categoria){
        return ResponseEntity.ok(service.listarContas(status,origem,categoria));
    }

}
