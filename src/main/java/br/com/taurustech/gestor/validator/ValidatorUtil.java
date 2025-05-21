package br.com.taurustech.gestor.validator;


import br.com.taurustech.gestor.service.RoleService;
import jakarta.validation.ConstraintViolationException;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public class ValidatorUtil {
    private ValidatorUtil() {
    }

    public static void gerarErroValidation(String atributo, String mensagemErro){
        ObjectValidation validation = new ObjectValidation(atributo,mensagemErro);
        Set<ObjectValidation> violations = Collections.singleton(validation);
        throw new ConstraintViolationException(violations);
    }

    public static void validarRole(String nome, RoleService service) {
        if (!service.existeByNome(nome)){
            gerarErroValidation("role", "campo inválido");
        }
    }

    public static UUID validarUUID (String texto){
        try{
            return UUID.fromString(texto);
        }catch (IllegalArgumentException e){
            gerarErroValidation("UUID", "valor inválido");
            return null;
        }
    }


}