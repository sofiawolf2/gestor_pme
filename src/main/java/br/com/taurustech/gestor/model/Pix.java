package br.com.taurustech.gestor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity @Data @Table (name = "pix")
public class Pix {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;
        @Size(max = 30, message = "campo fora do tamanho padrão")
        @NotBlank(message = "campo obrigatório")
        private String descricao;

        @ManyToOne
        @JoinColumn (name = "funcionario_id")
        private Funcionario funcionario;
        @ManyToOne @NotNull(message = "campo inválido")
        @JoinColumn (name = "tipo_pix_id")
        private TipoPix tipoPix;

        public Pix(TipoPix tipoPix) {
                this.tipoPix = tipoPix;
        }

        public Pix() {
        }
}