package br.com.taurustech.gestor.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity @Data @Table (name = "tipo_pix")
public class TipoPix {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;
        private String descricao;

        public TipoPix(String descricao) {
                this.descricao = descricao;
        }

        public TipoPix() {
        }
}