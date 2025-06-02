package br.com.taurustech.gestor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity @Data @Table (name = "despesa_funcionario")
@EntityListeners(AuditingEntityListener.class)
public class DespesaFuncionario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @NotNull(message = "campo inválido")
    @JoinColumn (name = "funcionario_id")
    private Funcionario funcionario;

    @CreatedDate
    @Column (name = "dt_entrada")
    private LocalDate dtEntrada;

    @NotNull(message = "campo obrigatório")
    private Double valor;

    private String observacao;

    @ManyToOne @NotNull(message = "campo inválido")
    @JoinColumn (name = "tipoDespesa_id")
    private TipoDespesa tipoDespesa;

    public DespesaFuncionario(TipoDespesa tipoDespesa) {
        this.tipoDespesa = tipoDespesa;
    }

    public DespesaFuncionario() {
    }
}
