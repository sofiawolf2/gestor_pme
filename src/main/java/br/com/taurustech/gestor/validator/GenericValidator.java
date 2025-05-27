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
            case BOOLEANO -> value instanceof Boolean;
            case NUMERICO -> isNumerico(value.toString());
            case DATA -> isDate(value.toString());
            case DOUBLE -> isDouble(value.toString());
            case INTEIRO -> value instanceof Integer;

        };
    }
    private boolean isDouble(String valor) {
        try {
            Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    private boolean isDate(String valor) {
        try {
            LocalDate.parse(valor);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public boolean isNumerico(String str) {
        return str != null && str.matches("\\d+");
    }

}