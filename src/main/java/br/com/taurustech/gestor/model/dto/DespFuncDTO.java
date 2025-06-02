package br.com.taurustech.gestor.model.dto;

import br.com.taurustech.gestor.model.DespesaFuncionario;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DespFuncDTO {
    private Integer id;
    private String funcionario;
    private Double valor;
    private String observacao;
    private String tipoDespesa;

    public static DespFuncDTO createOutput(DespesaFuncionario dp){
        ModelMapper modelMapper = new ModelMapper();
        var dto = modelMapper.map(dp, DespFuncDTO.class);
        dto.setFuncionario(dp.getFuncionario().getId().toString());
        dto.setTipoDespesa(dp.getTipoDespesa().getDescricao());
        return dto;
    }
    public DespesaFuncionario gerarPixSemEntidades () {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, DespesaFuncionario.class);
    }

}
