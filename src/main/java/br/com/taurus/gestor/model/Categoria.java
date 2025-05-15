package br.com.taurus.gestor.model;

import jakarta.persistence.Entity;
import lombok.Getter;

@Getter
@Entity
public enum Categoria {

    DESPESA_FIXA("Contas recorrentes como aluguel, água, luz"),
    DESPESA_VARIAVEL("Gastos que mudam mensalmente, como materiais e transporte"),
    RECEITA_RECORRENTE("Pagamentos fixos como mensalidades"),
    RECEITA_VARIAVEL("Entradas financeiras de vendas avulsas ou serviços pontuais"),
    IMPOSTOS_TAXAS("Tributos e obrigações legais"),
    INVESTIMENTOS("Aplicações financeiras ou aquisição de bens");

    private final String descricao;
    Categoria(String descricao) {
        this.descricao = descricao;
    }
}