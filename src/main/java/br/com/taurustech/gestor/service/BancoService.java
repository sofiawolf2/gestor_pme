package br.com.taurustech.gestor.service;

import br.com.taurustech.gestor.exception.ObjetoNaoEncontradoException;
import br.com.taurustech.gestor.model.Banco;
import br.com.taurustech.gestor.model.dto.MultiDTO;
import br.com.taurustech.gestor.repository.BancoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static br.com.taurustech.gestor.validator.ObjectValidation.gerarErroValidation;
import static br.com.taurustech.gestor.validator.ValidatorUtil.isInteger;

@Service @RequiredArgsConstructor
public class BancoService {
    private final BancoRepository repository;
    String erroNotFound = "banco não encontrada";
    String existe = "ja existe! Esse campo deve ser único";

    private void jaExisteDescricao (String descricao, Integer id){
        var existeIgual = repository.findByDescricaoIgnoreCase(descricao);
        if (existeIgual!=null && !Objects.equals(existeIgual.getId(), id)) gerarErroValidation("banco",existe);
    }

    private Banco buscarValidando (String id){
        return repository.findById(isInteger(id, "id")).orElseThrow(() -> new ObjetoNaoEncontradoException(erroNotFound));
    }

    public void cadastrar(MultiDTO dto) {
        jaExisteDescricao(dto.nome(), null);
        repository.save(new Banco(dto.nome()));
    }

    public List<Banco> listarTodos() {
        return repository.findAll();
    }

    public Banco buscarById(String id) {
        return buscarValidando(id);
    }

    public void deletarById(String id) {
        repository.delete(buscarValidando(id));
    }

    public void atualizarPatch(MultiDTO dto, String id) {
        var banco = buscarValidando(id);
        jaExisteDescricao(dto.nome(),Integer.valueOf(id));
        banco.setDescricao(dto.nome());
        repository.save(banco);
    }
}
