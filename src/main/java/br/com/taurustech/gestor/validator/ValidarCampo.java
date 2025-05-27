package br.com.taurustech.gestor.validator;
import br.com.taurustech.gestor.model.TipoValidacao;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = GenericValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidarCampo {
    String message() default "Valor inválido!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    TipoValidacao tipo(); // Define o tipo de validação que será usada
}

