package br.com.taurustech.gestor.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Entity @Data
@Table(name = "role")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;

    @Override
    public String getAuthority() {
        return nome;
    }

    public Role(String nome) {
        this.nome = nome;
    }

    public Role() {
    }
}

