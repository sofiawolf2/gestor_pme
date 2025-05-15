package br.com.taurus.gestor.model;

import jakarta.persistence.Entity;
import lombok.Getter;

@Getter
@Entity
public enum Status {

    ABERTA("Aguardando pagamento"),
    VENCIDA("Passou do vencimento"),
    PAGA("Quitada"),
    CANCELADA("Cancelada antes do pagamento"),
    PARCELADA("Pagamento realizado em partes"),
    ESTORNADA("Reembolso ou devolução"),
    EM_DISPUTA("Pagamento contestado");

    private final String descricao;
    Status( String descricao) {
        this.descricao = descricao;
    }
}