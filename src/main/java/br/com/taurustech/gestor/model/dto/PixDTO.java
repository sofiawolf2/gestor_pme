package br.com.taurustech.gestor.model.dto;

import br.com.taurustech.gestor.model.Pix;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data @JsonInclude(JsonInclude.Include.NON_NULL)
public class PixDTO {
    private Integer id;
    private String descricao;
    private String funcionario;
    private String tipoPix;

    public static PixDTO createOutput(Pix pix){
        ModelMapper modelMapper = new ModelMapper();
        var dto = modelMapper.map(pix, PixDTO.class);
        dto.setFuncionario(pix.getFuncionario().getId().toString());
        dto.setTipoPix(pix.getTipoPix().getDescricao());
        return dto;
    }
    public Pix gerarPixSemEntidades () {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, Pix.class);
    }

    public PixDTO(String descricao, String funcionario, String tipoPix) {
        this.descricao = descricao;
        this.funcionario = funcionario;
        this.tipoPix = tipoPix;
    }

    public PixDTO(String descricao) {
        this.descricao = descricao;
    }

    public PixDTO() {
    }
}

