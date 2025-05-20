package br.com.taurustech.gestor.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public record Categoria (
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        Integer id,
        String descricao) {
}