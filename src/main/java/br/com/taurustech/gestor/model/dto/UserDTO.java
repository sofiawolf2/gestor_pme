package br.com.taurustech.gestor.model.dto;

import br.com.taurustech.gestor.model.User;
import br.com.taurustech.gestor.service.RoleService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
     String id;
     String login;
     String nome;
     String email;
     String senha;
     String role;
     String token;

    private static UserDTO baseCreate(User user){
        ModelMapper modelMapper = new ModelMapper();
        var dto = modelMapper.map(user, UserDTO.class);
        dto.role = user.getRole().getNome();
        if (user.getId() != null) dto.id = user.getId().toString();
        return dto;
    }

    public static UserDTO createInput(User user) {
        var dto = baseCreate(user);
        dto.id = null;
        dto.token = null;
        return dto;
    }

    public static UserDTO createOutput(User user) {
        var dto = baseCreate(user);
        dto.token = null;
        dto.senha = null;
        return dto;
    }

    public static UserDTO createWToken(User user, String token){
        var dto = baseCreate(user);
        dto.token = token;
        dto.senha = null;
        dto.id = null;
        return dto;

    }

    public User getUser(RoleService roleService){
        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(this, User.class);
        user.setRole(roleService.buscarNome(this.role));
        if (this.id != null) user.setId(UUID.fromString(id));

        return user;
    }

    public String toJson() throws JsonProcessingException {
        ObjectMapper m = new ObjectMapper();
        return m.writeValueAsString(this);// converte para json
    }
}
