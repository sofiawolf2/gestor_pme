package br.com.taurustech.gestor.service;

import br.com.taurustech.gestor.exception.ObjetoNaoEncontradoException;
import br.com.taurustech.gestor.model.Agencia;
import br.com.taurustech.gestor.model.Banco;
import br.com.taurustech.gestor.model.TipoConta;
import br.com.taurustech.gestor.model.dto.AgenciaDTO;
import br.com.taurustech.gestor.repository.AgenciaRepository;
import br.com.taurustech.gestor.repository.BancoRepository;
import br.com.taurustech.gestor.repository.TipoContaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.taurustech.gestor.validator.ValidatorUtil.isInteger;

@Service
@RequiredArgsConstructor
public class AgenciaService {
    private final AgenciaRepository repository;
    private final BancoRepository bancoRepository;
    private final FuncionarioService funcionarioService;
    private final TipoContaRepository tipoContaRepository;
    String erroNotFound = "agencia não encontrada";

    private Agencia gerarEntidade(AgenciaDTO dto){
        Agencia entidade;
        entidade = dto.gerarAgenciaSemEntidades();
        if (dto.getFuncionario()!=null) entidade.setFuncionario(funcionarioService.buscarValidando(dto.getFuncionario(), true));
        entidade.setBanco(bancoRepository.findByDescricaoIgnoreCase(dto.getBanco()));
        entidade.setTipoConta(tipoContaRepository.findByDescricaoIgnoreCase(dto.getTipoConta()));
        return entidade;
    }

    public Agencia buscarValidando (String id){
        return repository.findById(isInteger(id, "id")).orElseThrow(() -> new ObjetoNaoEncontradoException(erroNotFound));
    }

    public void cadastrar(AgenciaDTO dto) {
        repository.save(gerarEntidade(dto));
    }

    public List<AgenciaDTO> listarAgencias(String tipoConta, String banco) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Agencia> example = Example.of(new Agencia(new TipoConta(tipoConta), new Banco(banco)),matcher);

        var lista = repository.findAll(example);

        if (lista.isEmpty()) throw new ObjetoNaoEncontradoException(erroNotFound);
        return lista.stream().map(AgenciaDTO::createOutput).toList();
    }

    public AgenciaDTO buscarById(String id) {
        return AgenciaDTO.createOutput(buscarValidando(id));
    }

    public void deletarById(String id) {
        repository.deleteById(buscarValidando(id).getId());
    }

    public void atualizarPatch(AgenciaDTO dto, String id) {
        var aAntes = buscarValidando(id);
        var aGerado = gerarEntidade(dto);
        if (dto.getNome()!=null) aAntes.setNome(aGerado.getNome());
        if (dto.getConta()!=null) aAntes.setConta(aGerado.getConta());
        if (dto.getBanco()!=null) aAntes.setBanco(aGerado.getBanco());
        if (dto.getFuncionario()!=null) aAntes.setFuncionario(aGerado.getFuncionario());
        if (dto.getTipoConta()!=null) aAntes.setTipoConta(aGerado.getTipoConta());
        repository.save(aAntes);
    }
}
