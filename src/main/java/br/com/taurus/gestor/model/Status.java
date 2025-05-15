package br.com.taurus.gestor.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public record Status (
        @Id
        String nome,
        String descricao) {
}