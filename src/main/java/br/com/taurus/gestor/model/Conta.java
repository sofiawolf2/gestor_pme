package br.com.taurus.gestor.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
@Data
@Entity
public class Conta {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
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
    @JoinColumn (name = "status_enum")
    private Status status;
    @ManyToOne
    @JoinColumn (name = "origem_enum")
    private Origem origem;
    @ManyToOne
    @JoinColumn (name = "categoria_enum")
    private Categoria categoria;
    @ManyToOne
    @JoinColumn (name = "usuario_id")
    private Usuario usuario;
}
