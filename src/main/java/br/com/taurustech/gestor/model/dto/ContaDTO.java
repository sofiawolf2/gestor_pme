package br.com.taurustech.gestor.model.dto;

import br.com.taurustech.gestor.model.Conta;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;

@Data @JsonInclude(JsonInclude.Include.NON_NULL)
public class ContaDTO {

    private Integer id;
    private LocalDate vencimento;
    private String descricao;
    private Double valor;
    private LocalDate dataPagamento;
    private String observacao;
    private String imagem;
    private String status;
    private String origem;
    private String categoria;
    private String user;

    public static ContaDTO createOutput(Conta conta){
        ModelMapper modelMapper = new ModelMapper();
        var dto = modelMapper.map(conta, ContaDTO.class);
        dto.status = conta.getStatus().getDescricao();
        dto.origem = conta.getOrigem().getDescricao();
        dto.categoria = conta.getCategoria().getDescricao();
        dto.user = null;
        return dto;
    }

    public Conta gerarContaSemEntidades () {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, Conta.class);

    }
}
