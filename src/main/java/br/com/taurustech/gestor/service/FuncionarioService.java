package br.com.taurustech.gestor.service;

import br.com.taurustech.gestor.exception.ObjetoNaoEncontradoException;
import br.com.taurustech.gestor.model.Funcao;
import br.com.taurustech.gestor.model.Funcionario;
import br.com.taurustech.gestor.model.dto.FuncionarioDTO;
import br.com.taurustech.gestor.repository.FuncaoRepository;
import br.com.taurustech.gestor.repository.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.taurustech.gestor.validator.ValidatorUtil.isInteger;

@Service @RequiredArgsConstructor
public class FuncionarioService {
    private final FuncionarioRepository funcionarioRepository;
    private final FuncaoRepository funcaoRepository;
    String erroNotFound = "funcionário não encontrado";

    private Funcionario gerarEntidade(FuncionarioDTO dto){
        Funcionario entidade;
        entidade = dto.gerarFuncionarioSemEntidades();
        entidade.setFuncao(funcaoRepository.findByDescricaoIgnoreCase(dto.getFuncao()));
        return entidade;
    }

    private Funcionario buscarValidando (String id){
        return funcionarioRepository.findById(isInteger(id, "id")).orElseThrow(() -> new ObjetoNaoEncontradoException(erroNotFound));
    }

    public void cadastrar(FuncionarioDTO funcionario) {
        funcionarioRepository.save(gerarEntidade(funcionario));
    }

    public List<FuncionarioDTO> listarFuncionarios(String funcao) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Funcionario> funcionarioExample = Example.of(new Funcionario(new Funcao(funcao)),matcher);

        var lista = funcionarioRepository.findAll(funcionarioExample);

        if (lista.isEmpty()) throw new ObjetoNaoEncontradoException(erroNotFound);
        return lista.stream().map(FuncionarioDTO::createOutput).toList();
    }

    public FuncionarioDTO buscarById(String id) {
        var funcionario = buscarValidando(id);
        return FuncionarioDTO.createOutput(funcionario);
    }
}
