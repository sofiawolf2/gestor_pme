package br.com.taurustech.gestor.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity @Data
public class Status{
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;
        private String descricao;

        public Status(String descricao) {
                this.descricao = descricao;
        }

        public Status() {
        }
}