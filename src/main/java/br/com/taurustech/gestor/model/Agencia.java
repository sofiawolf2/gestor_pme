package br.com.taurustech.gestor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
public class Agencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(min = 2, max = 20, message = "campo fora do tamanho padrão")
    @NotBlank(message = "campo obrigatório")
    private String nome;

    @Size(min = 2, max = 20, message = "campo fora do tamanho padrão")
    @NotBlank(message = "campo obrigatório")
    private String conta;

    @ManyToOne
    @NotNull(message = "campo inválido")
    @JoinColumn (name = "banco_id")
    private Banco banco;

    @ManyToOne
    @NotNull(message = "campo inválido")
    @JoinColumn (name = "funcionario_id")
    private Funcionario funcionario;

    @ManyToOne
    @NotNull(message = "campo inválido")
    @JoinColumn (name = "tipo_conta_id")
    private TipoConta tipoConta;

    public Agencia(TipoConta tipoConta, Banco banco) {
        this.tipoConta = tipoConta;
        this.banco = banco;
    }

    public Agencia() {
    }
}
