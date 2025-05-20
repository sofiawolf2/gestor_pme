package br.com.taurustech.gestor.service;

import br.com.taurustech.gestor.model.Role;
import br.com.taurustech.gestor.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role buscarNome(String nome){
        return roleRepository.findByNome(nome);
    }
    public boolean existeByNome(String nome){
        return roleRepository.existsByNome(nome);
    }
    public Role buscarNomeProx(String role) {return roleRepository.findTopByNomeIgnoreCase(role);}
}
