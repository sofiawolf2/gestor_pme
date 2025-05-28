package br.com.taurustech.gestor.validator;

import br.com.taurustech.gestor.model.TipoValidacao;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class GenericValidator implements ConstraintValidator<ValidarCampo, Object> {
    private TipoValidacao tipo;

    @Override
    public void initialize(ValidarCampo constraintAnnotation) {
        this.tipo = constraintAnnotation.tipo();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) return true;// nao valida a onrigatoriedade

        return switch (tipo) {
            case NUMERICO -> isNumerico(value.toString());
            case DATA -> isDate(value.toString());
            case INTEIRO -> value instanceof Integer;
            case CPF -> isCPF(value.toString());
            case TELEFONE -> isTelefone(value.toString());

        };
    }
    private boolean isDate(String valor) {
        try {
            LocalDate.parse(valor);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private boolean isNumerico(String str) {
        return str != null && str.matches("\\d+");
    }

    private boolean isCPF(String cpf){
        return (isNumerico(cpf) && cpf.length()==11);
    }

    private boolean isTelefone (String string){
        return  (isNumerico(string) && string.length()>=12 && string.length()<=14);
    }



}