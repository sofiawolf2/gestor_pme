package br.com.taurustech.gestor.validator;

import br.com.taurustech.gestor.model.Conta;
import br.com.taurustech.gestor.model.dto.ContaDTO;
import br.com.taurustech.gestor.repository.CategoriaRepository;
import br.com.taurustech.gestor.repository.OrigemRepository;
import br.com.taurustech.gestor.repository.StatusRepository;
import br.com.taurustech.gestor.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static br.com.taurustech.gestor.validator.ValidatorUtil.isDate;

@Component
@RequiredArgsConstructor
public class ContaValidator {
    private final StatusRepository statusRepository;
    private final OrigemRepository origemRepository;
    private final CategoriaRepository categoriaRepository;
    private final UserService userService;
    String textoInvalido = "valor invalido";

    public Conta validarERetornarEntidades(ContaDTO dto){
        isDate(dto.getVencimento(), "vencimento");
        isDate(dto.getDataPagamento(), "dataPagamento");
        Conta conta = new Conta();
        conta.setUser(userService.buscarUserID(dto.getUser()));
        conta.setCategoria(categoriaRepository.findByDescricaoIgnoreCase(dto.getCategoria()));
        conta.setOrigem(origemRepository.findByDescricaoIgnoreCase(dto.getOrigem()));
        conta.setStatus(statusRepository.findByDescricaoIgnoreCase(dto.getStatus()));
        return conta;
    }
}
