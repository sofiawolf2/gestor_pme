package br.com.taurustech.gestor.service;

import br.com.taurustech.gestor.exception.ObjetoNaoEncontradoException;
import br.com.taurustech.gestor.model.Categoria;
import br.com.taurustech.gestor.model.Conta;
import br.com.taurustech.gestor.model.Origem;
import br.com.taurustech.gestor.model.Status;
import br.com.taurustech.gestor.model.dto.ContaDTO;
import br.com.taurustech.gestor.repository.CategoriaRepository;
import br.com.taurustech.gestor.repository.ContaRepository;
import br.com.taurustech.gestor.repository.OrigemRepository;
import br.com.taurustech.gestor.repository.StatusRepository;
import br.com.taurustech.gestor.validator.ContaValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class ContaService {
    private final ContaRepository contaRepository;
    private final ContaValidator contaValidator;
    private final StatusRepository statusRepository;
    private final OrigemRepository origemRepository;
    private final CategoriaRepository categoriaRepository;
    private final UserService userService;


    String erroNotFound = "Conta não encontrada";

    private Conta gerarEntidade (ContaDTO dto){
        Conta conta = dto.gerarContaSemEntidades();

        if (userService.buscarUserAtual().getRole().getNome().equals("ROLE_ADMIN")&& dto.getUser() != null){
            conta.setUser(userService.buscarUserID(dto.getUser()));
        }else {conta.setUser(userService.buscarUserAtual());}

        conta.setStatus(statusRepository.findByDescricaoIgnoreCase(dto.getStatus()));
        conta.setCategoria(categoriaRepository.findByDescricaoIgnoreCase(dto.getCategoria()));
        conta.setOrigem(origemRepository.findByDescricaoIgnoreCase(dto.getOrigem()));

        return conta;
    }

    public void cadastrar(ContaDTO contaDTO) {
        contaValidator.validarDatasConta(contaDTO);
        contaRepository.save(gerarEntidade(contaDTO));
    }

    public List<ContaDTO> listarContas(String status, String origem, String categoria) {
        var conta = new Conta();
        conta.setStatus(new Status(status));
        conta.setOrigem(new Origem(origem));
        conta.setCategoria(new Categoria(categoria));
        conta.setUser(userService.buscarUserAtual());

        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Conta> contaExample = Example.of(conta,matcher);

        var lista = contaRepository.findAll(contaExample);
        if (lista.isEmpty()) throw new ObjetoNaoEncontradoException(erroNotFound);
        return lista.stream().map(ContaDTO::createOutput).toList();
    }
}
