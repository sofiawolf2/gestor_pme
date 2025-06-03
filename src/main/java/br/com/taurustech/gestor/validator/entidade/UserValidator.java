package br.com.taurustech.gestor.validator.entidade;

import br.com.taurustech.gestor.model.User;
import br.com.taurustech.gestor.model.dto.UserDTO;
import br.com.taurustech.gestor.repository.RoleRepository;
import br.com.taurustech.gestor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static br.com.taurustech.gestor.validator.ObjectValidation.gerarErroValidation;
import static br.com.taurustech.gestor.validator.ValidatorUtil.validarRole;

@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserRepository userRpository;
    private final RoleRepository roleRepository;


    public void validarNovoUser (UserDTO dto){
        validarLogin(dto.getLogin());
        validarRole(dto.getRole(),roleRepository);
        validarAdmin(dto, null);
    }

    public void validarAtualizado (UserDTO dto, UUID id){
        if (naoPodeSalvarLogin(dto, id)) validarLogin(dto.getLogin());
        if (dto.getRole()!=null) {
            validarRole(dto.getRole(),roleRepository);
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
}
