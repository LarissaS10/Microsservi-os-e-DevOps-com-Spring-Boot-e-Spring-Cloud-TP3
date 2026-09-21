package com.exemplo.clientesservice.config;

import com.exemplo.clientesservice.model.Cliente;
import com.exemplo.clientesservice.repository.ClienteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Popula o banco H2 em memoria com clientes de teste assim que a aplicacao sobe.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final ClienteRepository clienteRepository;

    public DataInitializer(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public void run(String... args) {
        clienteRepository.save(new Cliente("Ana Souza", "ana.souza@exemplo.com"));
        clienteRepository.save(new Cliente("Bruno Lima", "bruno.lima@exemplo.com"));
        clienteRepository.save(new Cliente("Carla Mendes", "carla.mendes@exemplo.com"));
        clienteRepository.save(new Cliente("Diego Rocha", "diego.rocha@exemplo.com"));
        clienteRepository.save(new Cliente("Elisa Prado", "elisa.prado@exemplo.com"));
        clienteRepository.save(new Cliente("Felipe Nunes", "felipe.nunes@exemplo.com"));
        clienteRepository.save(new Cliente("Gabriela Reis", "gabriela.reis@exemplo.com"));
        clienteRepository.save(new Cliente("Henrique Alves", "henrique.alves@exemplo.com"));
        clienteRepository.save(new Cliente("Isabela Cruz", "isabela.cruz@exemplo.com"));
        clienteRepository.save(new Cliente("Joao Vieira", "joao.vieira@exemplo.com"));
    }
}
