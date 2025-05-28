package br.com.taurustech.gestor.model;

import br.com.taurustech.gestor.validator.ValidarCampo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity @Data
public class Funcionario {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @NotBlank(message = "campo obrigatório")
        @Size(min = 2, max = 100, message = "campo fora do tamanho padrão")
        private String nome;

        @ManyToOne @NotNull(message = "campo inválido")
        @JoinColumn (name = "funcao_id")
        private Funcao funcao;

        @NotBlank(message = "campo obrigatório")
        @ValidarCampo(tipo = TipoValidacao.CPF, message = "campo deve seguir o padrão XXX.XXX.XXX-XX preenchendo xX com numeros")
        private String cpf;

        @NotBlank(message = "campo obrigatório")
        @ValidarCampo(tipo = TipoValidacao.TELEFONE, message = "campo deve conter número celular incluindo DDD e o codigo do pais")
        private String telefone;

        @NotNull(message = "campo obrigatório")
        private Double salario;

        @NotNull(message = "campo obrigatório")
        private Boolean ativo;

        public Funcionario(Funcao funcao) {
                this.funcao = funcao;
        }

        public Funcionario() {
        }
}