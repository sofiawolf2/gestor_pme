package br.com.taurustech.gestor.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
@Data
@Entity
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Date vencimento;
    @Column(length = 100)
    private String descricao;
    private Double valor;
    @Column(name = "data_pagamento")
    private Date dataPagamento;
    private String observacao;
    private String imagem;
    @ManyToOne
    @JoinColumn (name = "status_id")
    private Status status;
    @ManyToOne
    @JoinColumn (name = "origem_id")
    private Origem origem;
    @ManyToOne
    @JoinColumn (name = "categoria_id")
    private Categoria categoria;
    @ManyToOne
    @JoinColumn (name = "usuario_id")
    private User user;
}
