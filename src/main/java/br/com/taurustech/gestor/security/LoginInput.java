package br.com.taurustech.gestor.security;

import lombok.Data;

@Data
class LoginInput {
    private String username;
    private String password;

}