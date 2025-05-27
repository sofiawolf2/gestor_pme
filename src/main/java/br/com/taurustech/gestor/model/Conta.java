package br.com.taurustech.gestor.model;

import br.com.taurustech.gestor.validator.ValidarCampo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;
@Data
@Entity
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Future(message = "campo deve ser uma data futura")
    @NotNull(message = "campo obrigatório")
    @ValidarCampo(tipo = TipoValidacao.DATA, message = "campo deve ser uma Data seguindo o formato AAAA-MM-DD")
    private Date vencimento;

    @Column(length = 100)
    @NotBlank(message = "campo obrigatório")
    private String descricao;

    @NotNull(message = "campo obrigatório")
    @ValidarCampo(tipo = TipoValidacao.DOUBLE, message = "campo deve ser um número decimal")
    private Double valor;

    @Column(name = "data_pagamento")
    @ValidarCampo(tipo = TipoValidacao.DATA, message = "campo deve ser uma Data seguindo o formato AAAA-MM-DD")
    private Date dataPagamento;

    private String observacao;
    private String imagem;

    @ManyToOne @NotNull(message = "campo inválido")
    @JoinColumn (name = "status_id")
    private Status status;
    @ManyToOne @NotNull(message = "campo inválido")
    @JoinColumn (name = "origem_id")
    private Origem origem;
    @ManyToOne @NotNull(message = "campo inválido")
    @JoinColumn (name = "categoria_id")
    private Categoria categoria;
    @ManyToOne @NotNull(message = "campo inválido")
    @JoinColumn (name = "user_id")
    private User user;
}
