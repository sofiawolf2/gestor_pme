package br.com.taurus.gestor.model;

import jakarta.persistence.Entity;
import lombok.Getter;

@Getter
@Entity
public enum Origem {

    FORNECEDOR("Contas relacionadas a compras ou serviços adquiridos"),
    CLIENTE("Pagamentos de vendas ou prestação de serviços"),
    FUNCIONARIO("Salários, benefícios e reembolsos"),
    INSTITUICAO_FINANCEIRA("Empréstimos, financiamentos e juros bancários"),
    GOVERNO("Tributos e incentivos fiscais");

    private final String descricao;
    Origem( String descricao) {
        this.descricao = descricao;
    }
}