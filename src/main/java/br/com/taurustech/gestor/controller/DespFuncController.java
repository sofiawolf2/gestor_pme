package br.com.taurustech.gestor.controller;

import br.com.taurustech.gestor.model.dto.DespFuncDTO;
import br.com.taurustech.gestor.service.DespFuncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/despesaFuncionarios")
@RequiredArgsConstructor
public class DespFuncController {
    private final DespFuncService service;

    @PostMapping
    @Secured({ "ROLE_ADMIN"})
    public ResponseEntity<Void> cadastrar (@RequestBody DespFuncDTO dto){
        service.cadastrar(dto);
        return ResponseEntity.status(201).build();
    }

    @GetMapping
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<List<DespFuncDTO>> listarDF(@RequestParam (required = false) String tipoDespesa){
        return ResponseEntity.ok(service.listarDespFunc(tipoDespesa));
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity <DespFuncDTO> getDFById(@PathVariable String id){
        return ResponseEntity.ok(service.buscarById(id));
    }
}
