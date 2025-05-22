package br.com.taurustech.gestor.validator;


import br.com.taurustech.gestor.service.RoleService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.UUID;

import static br.com.taurustech.gestor.validator.ObjectValidation.gerarErroValidation;

public class ValidatorUtil {
    private ValidatorUtil() {
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

    public static void isDate(String valor, String atributo) {
        try {
            LocalDate.parse(valor);
        } catch (DateTimeParseException e) {
            gerarErroValidation(atributo, "deve ser uma Data seguindo o formato AAAA-MM-DD");
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

    public static void isDouble(String valor, String atributo) {
        try {
            Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            gerarErroValidation(atributo, "deve ser um valor numérico com casas decimais");
        }
    }




}