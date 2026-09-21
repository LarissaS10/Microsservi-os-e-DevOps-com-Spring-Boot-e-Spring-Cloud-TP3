package com.exemplo.produtosservice.config;

import com.exemplo.produtosservice.model.Produto;
import com.exemplo.produtosservice.repository.ProdutoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Popula o banco H2 em memoria com produtos de teste assim que a aplicacao sobe.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final ProdutoRepository produtoRepository;

    public DataInitializer(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Override
    public void run(String... args) {
        produtoRepository.save(new Produto("Notebook", new BigDecimal("3500.00")));
        produtoRepository.save(new Produto("Mouse sem fio", new BigDecimal("79.90")));
        produtoRepository.save(new Produto("Teclado mecanico", new BigDecimal("299.90")));
        produtoRepository.save(new Produto("Monitor 27 polegadas", new BigDecimal("1299.00")));
        produtoRepository.save(new Produto("Webcam Full HD", new BigDecimal("199.90")));
        produtoRepository.save(new Produto("Headset gamer", new BigDecimal("249.50")));
        produtoRepository.save(new Produto("SSD 1TB", new BigDecimal("459.90")));
        produtoRepository.save(new Produto("Cadeira de escritorio", new BigDecimal("899.00")));
        produtoRepository.save(new Produto("Carregador USB-C 65W", new BigDecimal("129.90")));
        produtoRepository.save(new Produto("Smartphone", new BigDecimal("2199.00")));
    }
}
