package br.com.taurustech.gestor.service;

import br.com.taurustech.gestor.repository.DespFuncRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class DespFuncService {
    private final DespFuncRepository repository;
}
