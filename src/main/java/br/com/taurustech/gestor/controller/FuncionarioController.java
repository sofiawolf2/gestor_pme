package br.com.taurustech.gestor.controller;

import br.com.taurustech.gestor.model.dto.FuncionarioDTO;
import br.com.taurustech.gestor.service.FuncionarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/funcionarios")
@RequiredArgsConstructor
public class FuncionarioController {
    private final FuncionarioService service;

    @PostMapping
    @Secured({ "ROLE_ADMIN"})
    public ResponseEntity<Void> cadastrar (@RequestBody FuncionarioDTO funcionario){
        service.cadastrar(funcionario);
        return ResponseEntity.status(201).build();
    }

    @GetMapping
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<List<FuncionarioDTO>> listarFuncionarios(@RequestParam (required = false) String funcao){
        return ResponseEntity.ok(service.listarFuncionarios(funcao));
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity <FuncionarioDTO> getFuncionarioById(@PathVariable String id){
        return ResponseEntity.ok(service.buscarById(id));
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity <Void> deleteFuncinarioById(@PathVariable String id){
        service.deletarById(id);
        return ResponseEntity.status(204).build();
    }

    @PatchMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Void> atualizarId(@RequestBody FuncionarioDTO dto, @PathVariable String id){
        service.atualizarPatch(dto,id);
        return ResponseEntity.status(204).build();
    }
}
