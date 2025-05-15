package br.com.taurus.gestor.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Entity @Data
public class Usuario {

    @Id @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;
    @NotBlank(message = "campo obrigatório")
    @Size(min = 2, max = 50, message = "campo fora do tamanho padrão")
    private String nome;
    @NotBlank(message = "campo obrigatório")
    @Size(min = 2, max = 50, message = "campo fora do tamanho padrão")
    private String login;
    @NotBlank(message = "campo obrigatório")
    @Size(min = 2, max = 100, message = "campo fora do tamanho padrão")
    private String senha;
    @NotBlank(message = "campo obrigatório")
    @Size(min = 2, max = 50, message = "campo fora do tamanho padrão")
    @Email
    private String email;

}