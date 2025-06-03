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

import static br.com.taurustech.gestor.validator.ObjectValidation.gerarErroValidation;
import static br.com.taurustech.gestor.validator.ValidatorUtil.isInteger;

@Service @RequiredArgsConstructor
public class FuncionarioService {
    private final FuncionarioRepository funcionarioRepository;
    private final FuncaoRepository funcaoRepository;
    String erroNotFound = "funcionário não encontrado";
    String jaExiste = "ja existe! Esse campo deve ser único";

    private Funcionario gerarEntidade(FuncionarioDTO dto){
        Funcionario entidade;
        entidade = dto.gerarFuncionarioSemEntidades();
        entidade.setFuncao(funcaoRepository.findByDescricaoIgnoreCase(dto.getFuncao()));
        return entidade;
    }

    Funcionario buscarValidando(String id, boolean classeAtributo){
        String atributo = "id";
        if (classeAtributo) atributo = "funcionario";
        return funcionarioRepository.findById(isInteger(id, atributo)).orElseThrow(() -> new ObjetoNaoEncontradoException(erroNotFound));
    }

    private void validarCpfTelefone(Funcionario func){
        if (func.getCpf()!=null && funcionarioRepository.existsByCpf(func.getCpf())) gerarErroValidation("cpf", jaExiste);
        if (func.getCpf()!=null && funcionarioRepository.existsByTelefone(func.getTelefone())) gerarErroValidation("telefone", jaExiste);
    }

    public void cadastrar(FuncionarioDTO funcionario) {
        funcionario.setId( null);
        var func = gerarEntidade(funcionario);
        validarCpfTelefone(func);
        funcionarioRepository.save(func);
    }

    public List<FuncionarioDTO> listarFuncionarios(String funcao) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Funcionario> funcionarioExample = Example.of(new Funcionario(new Funcao(funcao)),matcher);

        var lista = funcionarioRepository.findAll(funcionarioExample);

        if (lista.isEmpty()) throw new ObjetoNaoEncontradoException(erroNotFound);
        return lista.stream().map(FuncionarioDTO::createOutput).toList();
    }

    public FuncionarioDTO buscarById(String id) {
        return FuncionarioDTO.createOutput(buscarValidando(id, false));
    }

    public void deletarById(String id) {
        funcionarioRepository.delete(buscarValidando(id, false));
    }

    public void atualizarPatch(FuncionarioDTO dto, String id) {
        var funcAntes = buscarValidando(id, false);
        var funcGerado = gerarEntidade(dto);

        validarCpfTelefone(funcGerado);

        if(dto.getNome()!=null) funcAntes.setNome(funcGerado.getNome());
        if(dto.getTelefone()!=null) funcAntes.setTelefone(funcGerado.getTelefone());
        if(dto.getFuncao()!=null) funcAntes.setFuncao(funcGerado.getFuncao());
        if(dto.getAtivo()!=null) funcAntes.setAtivo(funcGerado.getAtivo());
        if(dto.getSalario()!=null) funcAntes.setSalario(funcGerado.getSalario());

        funcionarioRepository.save(funcAntes);
    }
}
