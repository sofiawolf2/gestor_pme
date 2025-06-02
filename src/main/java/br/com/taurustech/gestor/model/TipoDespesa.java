package br.com.taurustech.gestor.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity @Data @Table (name = "tipo_despesa")
public class TipoDespesa {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;
        private String descricao;

        public TipoDespesa(String descricao) {
                this.descricao = descricao;
        }

        public TipoDespesa() {
        }
}