package br.com.taurustech.gestor.validator;

import br.com.taurustech.gestor.model.User;
import br.com.taurustech.gestor.model.dto.UserDTO;
import br.com.taurustech.gestor.repository.UserRepository;
import br.com.taurustech.gestor.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

import static br.com.taurustech.gestor.validator.ValidatorUtil.*;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRpository;
    private final RoleService roleService;


    public void validarNovoUser (UserDTO dto){
        validarLogin(dto.getLogin());
        validarRole(dto.getRole(),roleService);
        validarAdmin(dto, null);
    }

    public void validarAtualizado (UserDTO dto, UUID id){
        if (naoPodeSalvarLogin(dto, id)) validarLogin(dto.getLogin());
        if (dto.getRole()!=null) {
            validarRole(dto.getRole(),roleService);
            validarAdmin(dto,id);
        }
    }

    private boolean naoPodeSalvarLogin (UserDTO user, UUID id){
        User existeLogin = userRpository.findByLogin(user.getLogin());
        if(existeLogin==null) return false;
        return !existeLogin.getId().equals(id);
    }

    public void validarLogin(String login) {
        if (userRpository.findByLogin(login)!=null){
            gerarErroValidation("login", "já cadastrado.");
        }
    }

    public void validarAdmin(UserDTO dto, UUID id){
        var admin = userRpository.findAdmin();
        if (dto.getRole().equals("ROLE_ADMIN") &&  admin!=null && !admin.getId().equals(id) ) gerarErroValidation("role", "usuário admin já cadastrado.");
    }

    public boolean validarIdUser(String id){
        return userRpository.existsById(Objects.requireNonNull(validarUUID(id)));
    }
}
