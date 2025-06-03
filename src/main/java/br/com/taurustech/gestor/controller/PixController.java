package br.com.taurustech.gestor.controller;

import br.com.taurustech.gestor.model.Pix;
import br.com.taurustech.gestor.model.dto.PixDTO;
import br.com.taurustech.gestor.service.PixService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pixs")
@RequiredArgsConstructor
public class PixController {
    private final PixService service;

    @PostMapping
    @Secured({ "ROLE_ADMIN"})
    public ResponseEntity<Void> cadastrar (@RequestBody PixDTO pix){
        service.cadastrar(pix);
        return ResponseEntity.status(201).build();
    }

    @GetMapping
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<List<PixDTO>> listarPixs(@RequestParam (required = false) String tipoPix){
        return ResponseEntity.ok(service.listarpixs(tipoPix));
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity <PixDTO> getPixById(@PathVariable String id){
        return ResponseEntity.ok(service.buscarById(id));
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity <Void> deletePixById(@PathVariable String id){
        service.deletarById(id);
        return ResponseEntity.status(204).build();
    }


    @PatchMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Void> atualizarId(@RequestBody Pix dto, @PathVariable String id){
        service.atualizarPatch(dto,id);
        return ResponseEntity.status(204).build();
    }

}
