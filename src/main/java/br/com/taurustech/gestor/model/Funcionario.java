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
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Integer id;

        @NotBlank(message = "campo obrigatório")
        @Size(min = 2, max = 100, message = "campo fora do tamanho padrão")
        private String nome;

        @ManyToOne @NotNull(message = "campo obrigatório")
        @JoinColumn (name = "funcao_id")
        private Funcao funcao;

        @NotBlank(message = "campo obrigatório")
        @Size(min = 11, max = 14, message = "campo deve ser um numero CPF com no máximo 14 caracteres")
        @ValidarCampo(tipo = TipoValidacao.NUMERICO, message = "campo deve conter apenas números")
        private String cpf;

        @NotBlank(message = "campo obrigatório")
        @Size(min = 11, max = 14, message = "campo deve conter número celular incluindo o codigo do pais e DDD")
        @ValidarCampo(tipo = TipoValidacao.NUMERICO, message = "campo deve conter apenas números")
        private String telefone;

        @NotNull(message = "campo obrigatório")
        @ValidarCampo(tipo = TipoValidacao.DOUBLE, message = "campo deve ser um número decimal")
        private Double salario;

        @NotNull(message = "campo obrigatório")
        @ValidarCampo(tipo = TipoValidacao.BOOLEANO, message = "campo deve ser verdadeiro ou falso")
        private Boolean ativo;

}