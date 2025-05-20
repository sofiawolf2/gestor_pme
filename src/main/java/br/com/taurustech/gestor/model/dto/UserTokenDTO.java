package br.com.taurustech.gestor.model.dto;

import br.com.taurustech.gestor.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.modelmapper.ModelMapper;


@Data @JsonInclude (JsonInclude.Include.NON_NULL)
public class UserTokenDTO {
    private String login;
    private String nome;
    private String email;

    // token jwt
    private String token;

    private String role;

    public static UserTokenDTO create(User user) {
        ModelMapper modelMapper = new ModelMapper();
        UserTokenDTO dto = modelMapper.map(user, UserTokenDTO.class);
        dto.role = user.getRole().getNome();
        return dto;
    }
    public static UserTokenDTO create(User user, String token) {
        UserTokenDTO dto = create(user);
        dto.token = token;
        return dto;
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper m = new ObjectMapper();
        return m.writeValueAsString(this);// converte para json
    }
}
