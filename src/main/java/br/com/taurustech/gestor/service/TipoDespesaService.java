package br.com.taurustech.gestor.service;

import br.com.taurustech.gestor.exception.ObjetoNaoEncontradoException;
import br.com.taurustech.gestor.model.TipoDespesa;
import br.com.taurustech.gestor.model.dto.MultiDTO;
import br.com.taurustech.gestor.repository.TipoDespesaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static br.com.taurustech.gestor.validator.ObjectValidation.gerarErroValidation;
import static br.com.taurustech.gestor.validator.ValidatorUtil.isInteger;

@Service @RequiredArgsConstructor
public class TipoDespesaService {
    private final TipoDespesaRepository repository;
    String erroNotFound = "tipo de despesa não encontrada";
    String existe = "ja existe! Esse campo deve ser único";

    private void jaExisteDescricao (String descricao, Integer id){
        var existeIgual = repository.findByDescricaoIgnoreCase(descricao);
        if (existeIgual!=null && !Objects.equals(existeIgual.getId(), id)) gerarErroValidation("tipoDespesa",existe);
    }

    private TipoDespesa buscarValidando (String id){
        return repository.findById(isInteger(id, "id")).orElseThrow(() -> new ObjetoNaoEncontradoException(erroNotFound));
    }

    public void cadastrar(MultiDTO dto) {
        jaExisteDescricao(dto.nome(), null);
        repository.save(new TipoDespesa(dto.nome().toUpperCase()));
    }

    public List<TipoDespesa> listarTodos() {
        return repository.findAll();
    }

    public TipoDespesa buscarById(String id) {
        return buscarValidando(id);
    }

    public void deletarById(String id) {
        repository.delete(buscarValidando(id));
    }

    public void atualizarPatch(MultiDTO dto, String id) {
        var tipoDespesa = buscarValidando(id);
        jaExisteDescricao(dto.nome(),Integer.valueOf(id));
        tipoDespesa.setDescricao(dto.nome().toUpperCase());
        repository.save(tipoDespesa);
    }
}
