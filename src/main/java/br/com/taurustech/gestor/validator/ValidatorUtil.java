package br.com.taurustech.gestor.validator;


import br.com.taurustech.gestor.repository.RoleRepository;

import java.util.UUID;

import static br.com.taurustech.gestor.validator.ObjectValidation.gerarErroValidation;

public class ValidatorUtil {
    private ValidatorUtil() {
    }

    public static void validarRole(String nome, RoleRepository repository) {
        if (repository.findByNomeIgnoreCase(nome)==null){
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


    public static Integer isInteger(String valor, String atributo) {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            gerarErroValidation(atributo, "deve ser um inteiro");
        }
        return 0;
    }





}