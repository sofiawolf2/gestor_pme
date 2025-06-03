package br.com.taurustech.gestor.service;

import br.com.taurustech.gestor.exception.ObjetoNaoEncontradoException;
import br.com.taurustech.gestor.model.DespesaFuncionario;
import br.com.taurustech.gestor.model.TipoDespesa;
import br.com.taurustech.gestor.model.dto.DespFuncDTO;
import br.com.taurustech.gestor.repository.DespFuncRepository;
import br.com.taurustech.gestor.repository.TipoDespesaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.taurustech.gestor.validator.ValidatorUtil.isInteger;

@Service @RequiredArgsConstructor
public class DespFuncService {
    private final DespFuncRepository repository;
    private final FuncionarioService funcionarioService;
    private final TipoDespesaRepository tipoDespesaRepository;
    String erroNotFound = "despesa de funcionário não encontrada";

    private DespesaFuncionario gerarEntidade(DespFuncDTO dto){
        DespesaFuncionario entidade;
        entidade = dto.gerarPixSemEntidades();
        if (dto.getFuncionario()!=null) entidade.setFuncionario(funcionarioService.buscarValidando(dto.getFuncionario(), true));
        entidade.setTipoDespesa(tipoDespesaRepository.findByDescricaoIgnoreCase(dto.getTipoDespesa()));
        return entidade;
    }
    private DespesaFuncionario buscarValidando (String id){
        return repository.findById(isInteger(id, "id")).orElseThrow(() -> new ObjetoNaoEncontradoException(erroNotFound));
    }

    public void cadastrar(DespFuncDTO dto) {
        dto.setId(null);
        repository.save(gerarEntidade(dto));
    }

    public List<DespFuncDTO> listarDespFunc(String tipoDespesa) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<DespesaFuncionario> example = Example.of(new DespesaFuncionario(new TipoDespesa(tipoDespesa)),matcher);

        var lista = repository.findAll(example);

        if (lista.isEmpty()) throw new ObjetoNaoEncontradoException(erroNotFound);
        return lista.stream().map(DespFuncDTO::createOutput).toList();
    }

    public DespFuncDTO buscarById(String id) {
        return DespFuncDTO.createOutput(buscarValidando(id));
    }

    public void deletarById(String id) {
        repository.deleteById(buscarValidando(id).getId());
    }

    public void atualizarPatch(DespFuncDTO dto, String id) {
        var dFAntes = buscarValidando(id);
        var dFGerado = gerarEntidade(dto);
        if (dto.getObservacao()!=null) dFAntes.setObservacao(dFGerado.getObservacao());
        if (dto.getTipoDespesa()!=null) dFAntes.setTipoDespesa(dFGerado.getTipoDespesa());
        if (dto.getValor()!=null) dFAntes.setValor(dFGerado.getValor());
        if (dto.getFuncionario()!=null) dFAntes.setFuncionario(dFGerado.getFuncionario());
        repository.save(dFAntes);
    }
}
