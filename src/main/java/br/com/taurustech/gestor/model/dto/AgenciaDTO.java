package br.com.taurustech.gestor.model.dto;

import br.com.taurustech.gestor.model.Agencia;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data @JsonInclude(JsonInclude.Include.NON_NULL)
public class AgenciaDTO {
    private Integer id;
    private String nome;
    private String conta;
    private String banco;
    private String funcionario;
    private String tipoConta;

    public static AgenciaDTO createOutput(Agencia agencia){
        ModelMapper modelMapper = new ModelMapper();
        var dto = modelMapper.map(agencia, AgenciaDTO.class);
        dto.setBanco(agencia.getBanco().getDescricao());
        dto.setFuncionario(agencia.getFuncionario().getId().toString());
        dto.setTipoConta(agencia.getTipoConta().getDescricao());
        return dto;
    }
    public Agencia gerarAgenciaSemEntidades () {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, Agencia.class);
    }

    public AgenciaDTO(String nome, String conta, String banco, String funcionario, String tipoConta) {
        this.nome = nome;
        this.conta = conta;
        this.banco = banco;
        this.funcionario = funcionario;
        this.tipoConta = tipoConta;
    }

    public AgenciaDTO() {
    }
}


