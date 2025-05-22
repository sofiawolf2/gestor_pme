package br.com.taurustech.gestor.model.dto;

import br.com.taurustech.gestor.model.Conta;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data @JsonInclude(JsonInclude.Include.NON_NULL)
public class ContaDTO {

    private Integer id;
    private String vencimento;
    private String descricao;
    private String valor;
    private String dataPagamento;
    private String observacao;
    private String imagem;
    private String status;
    private String origem;
    private String categoria;
    private String user;

    public static ContaDTO createOutput(Conta conta){
        ModelMapper modelMapper = new ModelMapper();
        var dto = modelMapper.map(conta, ContaDTO.class);
        dto.vencimento = conta.getVencimento().toString();
        dto.valor = conta.getValor().toString();
        if (conta.getDataPagamento()!=null) dto.dataPagamento = conta.getDataPagamento().toString();
        dto.status = conta.getStatus().getDescricao();
        dto.origem = conta.getOrigem().getDescricao();
        dto.categoria = conta.getCategoria().getDescricao();
        dto.user = null;
        return dto;
    }

    public static ContaDTO createInput(Conta conta){
        var dto = createOutput(conta);
        dto.id = null;
        dto.user = conta.getUser().getId().toString();
        return dto;
    }

    public Conta gerarContaSemEntidades () {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, Conta.class);
    }
}
