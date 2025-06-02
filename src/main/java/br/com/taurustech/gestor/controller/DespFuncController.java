package br.com.taurustech.gestor.controller;

import br.com.taurustech.gestor.service.DespFuncService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/despesaFuncionarios")
@RequiredArgsConstructor
public class DespFuncController {
    private final DespFuncService service;
}
