package br.com.taurustech.gestor.model.dto;

import br.com.taurustech.gestor.model.Funcionario;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data @JsonInclude(JsonInclude.Include.NON_NULL)
public class FuncionarioDTO {
    private Integer id;
    private String nome;
    private String funcao;
    private String cpf;
    private String telefone;
    private Double salario;
    private Boolean ativo;

    public FuncionarioDTO createOutput(Funcionario funcionario){
        ModelMapper modelMapper = new ModelMapper();
        var dto = modelMapper.map(funcionario, FuncionarioDTO.class);
        dto.setFuncao(funcionario.getFuncao().getDescricao());
        return dto;
    }
    public Funcionario gerarFuncionarioSemEntidades () {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, Funcionario.class);
    }
}
