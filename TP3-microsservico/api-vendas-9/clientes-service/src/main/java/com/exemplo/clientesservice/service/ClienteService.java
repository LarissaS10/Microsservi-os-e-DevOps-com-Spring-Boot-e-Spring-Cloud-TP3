package com.exemplo.clientesservice.service;

import com.exemplo.clientesservice.model.Cliente;
import com.exemplo.clientesservice.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Regra de negocio de Cliente. O controller nao fala direto com o repository,
 * fala com este service.
 */
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteRepository.findById(id);
    }
}
