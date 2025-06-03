package br.com.taurustech.gestor.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity @Data @Table (name = "tipo_conta")
public class TipoConta {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;
        private String descricao;

        public TipoConta(String descricao) {
                this.descricao = descricao;
        }

        public TipoConta() {
        }
}