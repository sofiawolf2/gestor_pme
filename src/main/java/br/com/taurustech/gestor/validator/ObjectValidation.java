package br.com.taurustech.gestor.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.metadata.ConstraintDescriptor;
import org.hibernate.validator.internal.engine.path.PathImpl;

public class ObjectValidation implements ConstraintViolation<Object> {

    private final String atributo;
    private final String mensagemErro;

    public ObjectValidation(String atributo, String mensagemErro) {
        this.atributo = atributo;
        this.mensagemErro = mensagemErro;
    }

    @Override
    public String getMessage() {
        return mensagemErro;
    }

    @Override
    public String getMessageTemplate() {
        return "";
    }

    @Override
    public Path getPropertyPath() {
        return PathImpl.createPathFromString(atributo);
    }

    @Override
    public Object getRootBean() {
        return null;
    }

    @Override
    public Class<Object> getRootBeanClass() {
        return Object.class;
    }

    @Override
    public Object getLeafBean() {
        return null;
    }

    @Override
    public Object[] getExecutableParameters() {
        return new Object[0];
    }

    @Override
    public Object getExecutableReturnValue() {
        return null;
    }

    @Override
    public Object getInvalidValue() {
        return null;
    }

    @Override
    public ConstraintDescriptor<?> getConstraintDescriptor() {
        return null;
    }

    @Override
    public <U> U unwrap(Class<U> type) {
        throw new UnsupportedOperationException("Não suportado!");
    }
}