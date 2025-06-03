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
    @GetMapping("/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity <ContaDTO> getContaById(@PathVariable String id){
        return ResponseEntity.ok(service.buscarById(id));
    }
    @GetMapping("/{id}/png")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity <byte[]> getImagemContaById(@PathVariable String id){
        return service.imprimirImagemConta(id);
    }

    @DeleteMapping("/{id}/png")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity <Void> deleteImagemContaById(@PathVariable String id){
        service.deletarImagemById(id);
        return ResponseEntity.status(204).build();
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity <Void> deleteContaById(@PathVariable String id){
        service.deleteById(id);
        return ResponseEntity.status(204).build();
    }

    @PatchMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Void> atualizarId(@RequestBody ContaDTO conta, @PathVariable String id){
        service.atualizarPatch(conta,id);
        return ResponseEntity.status(204).build(); // no content
    }


}
