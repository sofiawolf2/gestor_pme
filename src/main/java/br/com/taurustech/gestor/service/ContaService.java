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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.taurustech.gestor.validator.ValidatorUtil.isInteger;

@Service @RequiredArgsConstructor
public class ContaService {
    private final ContaRepository contaRepository;
    private final ContaValidator contaValidator;
    private final StatusRepository statusRepository;
    private final OrigemRepository origemRepository;
    private final CategoriaRepository categoriaRepository;
    private final UserService userService;
    private final ImagemService imagemService;


    String erroNotFound = "Conta não encontrada";

    private Conta buscarValidando (String id){
        return contaRepository.findById(isInteger(id, "id")).orElseThrow(() -> new ObjetoNaoEncontradoException(erroNotFound));
    }
    private void verificarUser (Conta conta){
        if (conta.getUser().getId()!= userService.buscarUserAtual().getId() && !userService.buscarUserAtual().getRole().getNome().equals("ROLE_ADMIN"))
            throw new ObjetoNaoEncontradoException(erroNotFound);
    }


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

    public void cadastrar(ContaDTO dto) {
        contaValidator.validarDatasConta(dto, false);
        if (dto.getImagem() != null) dto.setImagem(imagemService.cadastrar(dto.getImagem()));
        contaRepository.save(gerarEntidade(dto));
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
        if (lista.size() == 1) return List.of(ContaDTO.createOutput(lista.get(0)));
        return lista.stream().map(ContaDTO::createOutput).toList();
    }

    public ContaDTO buscarById(String id) {
        var conta = buscarValidando(id);
        verificarUser(conta);
        return ContaDTO.createOutput(conta);
    }

    public void deleteById(String id) {
        var conta = buscarValidando(id);
        verificarUser(conta);
        contaRepository.deleteById(conta.getId());
        imagemService.deletarImagemMemoria(conta.getImagem());
    }

    public void atualizarPatch(ContaDTO dto, String id) {
        var contaAntes = buscarValidando(id);
        verificarUser(contaAntes);

        var contaGerada = gerarEntidade(dto);

        if (dto.getDataPagamento()!=null) {
            contaValidator.validarDatasConta(dto, true);
            contaAntes.setDataPagamento(contaGerada.getDataPagamento());
        }
        if (dto.getObservacao()!=null) contaAntes.setObservacao(contaGerada.getObservacao());
        if (dto.getImagem()!=null){
            if (contaAntes.getImagem()==null){
                contaAntes.setImagem(imagemService.cadastrar(contaGerada.getImagem()));
            }else contaAntes.setImagem(imagemService.atualizar(contaGerada.getImagem(), contaAntes.getImagem()));
        }
        if (dto.getStatus()!=null) contaAntes.setStatus(contaGerada.getStatus());
        if (dto.getCategoria()!=null) contaAntes.setCategoria(contaGerada.getCategoria());

        contaRepository.save(contaAntes);
    }

    public ResponseEntity<byte[]> imprimirImagemConta(String id) {
       return imagemService.imprimirPNG(buscarById(id).getImagem());
    }
    public void deletarImagemById(String id) {
        var conta = buscarValidando(id);
        imagemService.deletarImagemMemoria(conta.getImagem());
        conta.setImagem(null);
        contaRepository.save(conta);
    }

    public void deletarTodosMenos (List<Integer> lista) {contaRepository.deleteAllExcept(lista);}


}
