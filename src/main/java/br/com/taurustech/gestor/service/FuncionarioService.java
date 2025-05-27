package br.com.taurustech.gestor.service;

import br.com.taurustech.gestor.model.Funcionario;
import br.com.taurustech.gestor.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class FuncionarioService {
    private final FuncionarioRepository repository;

    public void cadastrar(Funcionario funcionario) {
        repository.save(funcionario);
    }
}
