package br.com.taurustech.gestor.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity @Data
public class Origem {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;
        private String descricao;

        public Origem(String descricao) {
                this.descricao = descricao;
        }

        public Origem() {
        }
}