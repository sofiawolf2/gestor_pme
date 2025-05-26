package br.com.taurustech.gestor.validator;

import br.com.taurustech.gestor.model.dto.ContaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static br.com.taurustech.gestor.validator.ValidatorUtil.isDate;
import static br.com.taurustech.gestor.validator.ValidatorUtil.isDouble;

@Component
@RequiredArgsConstructor
public class ContaValidator {


    public void validarDatasConta(ContaDTO dto, Boolean atualizacao){

        if (Boolean.TRUE.equals(atualizacao)) isDate(dto.getDataPagamento(), "dataPagamento");
        else {
        isDate(dto.getVencimento(), "vencimento");
        isDate(dto.getDataPagamento(), "dataPagamento");
        isDouble(dto.getValor(), "valor");
        }
    }

}
