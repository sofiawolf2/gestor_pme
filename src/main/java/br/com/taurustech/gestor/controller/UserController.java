package br.com.taurustech.gestor.controller;

import br.com.taurustech.gestor.model.dto.UserDTO;
import br.com.taurustech.gestor.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Void> cadastrarUsuario(@RequestBody UserDTO user){
        service.cadastro(user);
        return ResponseEntity.status(201).build();
    }

    @GetMapping
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<List<UserDTO>> listarUsuarios(@RequestParam (required = false) String nome,
                                                             @RequestParam (required = false) String login,
                                                             @RequestParam (required = false) String email,
                                                             @RequestParam (required = false) String role){
        return ResponseEntity.ok(service.pesquisarTodos(nome,login,email,role));
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<UserDTO> buscarId(@PathVariable String id){
        return ResponseEntity.ok(service.buscarOutID(id));
    }

    @PatchMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Void> atualizarId(@RequestBody UserDTO user, @PathVariable String id){
        service.atualizarPatch(user,id);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Void> deletariD(@PathVariable String id){
        service.deletarStringId(id);
        return ResponseEntity.noContent().build();
    }
}
