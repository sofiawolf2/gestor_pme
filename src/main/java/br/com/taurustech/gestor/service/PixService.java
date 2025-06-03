package br.com.taurustech.gestor.service;

import br.com.taurustech.gestor.exception.ObjetoNaoEncontradoException;
import br.com.taurustech.gestor.model.Pix;
import br.com.taurustech.gestor.model.TipoPix;
import br.com.taurustech.gestor.model.dto.PixDTO;
import br.com.taurustech.gestor.repository.PixRepository;
import br.com.taurustech.gestor.repository.TipoPixRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.taurustech.gestor.validator.ValidatorUtil.isInteger;

@Service @RequiredArgsConstructor
public class PixService {
    private final PixRepository repository;
    private final TipoPixRepository tipoPixRepository;
    private final FuncionarioService funcionarioService;
    String erroNotFound = "pix não encontrado";

    private Pix gerarEntidade(PixDTO dto){
        Pix entidade;
        entidade = dto.gerarPixSemEntidades();
        entidade.setFuncionario(funcionarioService.buscarValidando(dto.getFuncionario(), true));
        entidade.setTipoPix(tipoPixRepository.findByDescricaoIgnoreCase(dto.getTipoPix()));
        return entidade;
    }
    private Pix buscarValidando (String id){
        return repository.findById(isInteger(id, "id")).orElseThrow(() -> new ObjetoNaoEncontradoException(erroNotFound));
    }


    public void cadastrar(PixDTO dto) {
        dto.setId(null);
        repository.save(gerarEntidade(dto));
    }

    public List<PixDTO> listarpixs(String tipoPix) {
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Pix> pixExample = Example.of(new Pix(new TipoPix(tipoPix)),matcher);

        var lista = repository.findAll(pixExample);

        if (lista.isEmpty()) throw new ObjetoNaoEncontradoException(erroNotFound);
        return lista.stream().map(PixDTO::createOutput).toList();
    }

    public PixDTO buscarById(String id) {
        return PixDTO.createOutput(buscarValidando(id));
    }

    public void deletarById(String id) {
        repository.deleteById(buscarValidando(id).getId());
    }

    public void atualizarPatch(PixDTO dto, String id) {
        var pixAntes = buscarValidando(id);
        if (dto.getDescricao()!=null){
            pixAntes.setDescricao(dto.getDescricao());
            repository.save(pixAntes);
        }
    }
}
