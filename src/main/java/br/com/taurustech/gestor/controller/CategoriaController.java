package br.com.taurustech.gestor.controller;

import br.com.taurustech.gestor.model.Categoria;
import br.com.taurustech.gestor.model.dto.MultiDTO;
import br.com.taurustech.gestor.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
public class CategoriaController {
    private final CategoriaService service;

    @PostMapping
    @Secured({ "ROLE_ADMIN"})
    public ResponseEntity<Void> cadastrar (@RequestBody MultiDTO nome){
        service.cadastrar(nome);
        return ResponseEntity.status(201).build();
    }

    @GetMapping
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<List<Categoria>> listarTodos(){
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity <Categoria> getById(@PathVariable String id){
        return ResponseEntity.ok(service.buscarById(id));
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity <Void> deleteById(@PathVariable String id){
        service.deletarById(id);
        return ResponseEntity.status(204).build();
    }


    @PatchMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Void> atualizarId(@RequestBody MultiDTO nome, @PathVariable String id){
        service.atualizarPatch(nome,id);
        return ResponseEntity.status(204).build();
    }

}
