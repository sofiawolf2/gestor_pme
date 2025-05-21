package br.com.taurustech.gestor.service;

import br.com.taurustech.gestor.model.Conta;
import br.com.taurustech.gestor.model.dto.ContaDTO;
import br.com.taurustech.gestor.repository.ContaRepository;
import br.com.taurustech.gestor.validator.ContaValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ContaService {
    private final ContaRepository contaRepository;
    private final ContaValidator contaValidator;

    public ContaService(ContaRepository contaRepository, ContaValidator contaValidator) {
        this.contaRepository = contaRepository;
        this.contaValidator = contaValidator;
    }

    String erroNotFound = "Conta não encontrado";
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public void cadastrar(ContaDTO contaDTO) {
        Conta conta = contaDTO.getContaSemEntidades();
        Conta contaComEntidades = contaValidator.validarERetornarEntidades(contaDTO);
        conta.setStatus(contaComEntidades.getStatus());
        conta.setCategoria(contaComEntidades.getCategoria());
        conta.setOrigem(contaComEntidades.getOrigem());
        conta.setUser(contaComEntidades.getUser());

        contaRepository.save(conta);
    }
}
