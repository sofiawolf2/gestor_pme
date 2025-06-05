package br.com.taurustech.gestor.service;

import br.com.taurustech.gestor.exception.ObjetoNaoEncontradoException;
import br.com.taurustech.gestor.model.Categoria;
import br.com.taurustech.gestor.model.dto.MultiDTO;
import br.com.taurustech.gestor.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static br.com.taurustech.gestor.validator.ObjectValidation.gerarErroValidation;
import static br.com.taurustech.gestor.validator.ValidatorUtil.isInteger;

@Service @RequiredArgsConstructor
public class CategoriaService {
    private final CategoriaRepository repository;
    String erroNotFound = "categoria não encontrada";
    String existe = "ja existe! Esse campo deve ser único";

    private void jaExisteDescricao (String descricao, Integer id){
        var existeIgual = repository.findByDescricaoIgnoreCase(descricao);
        if (existeIgual!=null && !Objects.equals(existeIgual.getId(), id)) gerarErroValidation("categoria",existe);
    }

    private Categoria buscarValidando (String id){
        return repository.findById(isInteger(id, "id")).orElseThrow(() -> new ObjetoNaoEncontradoException(erroNotFound));
    }

    public void cadastrar(MultiDTO dto) {
        jaExisteDescricao(dto.nome(), null);
        repository.save(new Categoria(dto.nome().toUpperCase()));
    }

    public List<Categoria> listarTodos() {
        return repository.findAll();
    }

    public Categoria buscarById(String id) {
        return buscarValidando(id);
    }

    public void deletarById(String id) {
        repository.delete(buscarValidando(id));
    }

    public void atualizarPatch(MultiDTO dto, String id) {
        var categoria = buscarValidando(id);
        jaExisteDescricao(dto.nome(),Integer.valueOf(id));
        categoria.setDescricao(dto.nome().toUpperCase());
        repository.save(categoria);
    }
}
