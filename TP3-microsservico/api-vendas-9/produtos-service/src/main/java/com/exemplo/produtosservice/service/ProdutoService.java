package com.exemplo.produtosservice.service;

import com.exemplo.produtosservice.model.Produto;
import com.exemplo.produtosservice.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Regra de negocio de Produto. O controller nao fala direto com o repository,
 * fala com este service.
 */
@Service
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }
}
