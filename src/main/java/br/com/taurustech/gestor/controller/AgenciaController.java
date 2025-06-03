package br.com.taurustech.gestor.controller;

import br.com.taurustech.gestor.model.dto.AgenciaDTO;
import br.com.taurustech.gestor.service.AgenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/agencias")
@RequiredArgsConstructor
public class AgenciaController {
    private final AgenciaService service;

    @PostMapping
    @Secured({ "ROLE_ADMIN"})
    public ResponseEntity<Void> cadastrar (@RequestBody AgenciaDTO dto){
        service.cadastrar(dto);
        return ResponseEntity.status(201).build();
    }

    @GetMapping
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<List<AgenciaDTO>> listaragencias(@RequestParam (required = false) String tipoConta,
                                                           @RequestParam (required = false) String banco){
        return ResponseEntity.ok(service.listarAgencias(tipoConta, banco));
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity <AgenciaDTO> getagenciaById(@PathVariable String id){
        return ResponseEntity.ok(service.buscarById(id));
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity <Void> deleteagenciaById(@PathVariable String id){
        service.deletarById(id);
        return ResponseEntity.status(204).build();
    }


    @PatchMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Void> atualizarId(@RequestBody AgenciaDTO dto, @PathVariable String id){
        service.atualizarPatch(dto,id);
        return ResponseEntity.status(204).build();
    }
}
