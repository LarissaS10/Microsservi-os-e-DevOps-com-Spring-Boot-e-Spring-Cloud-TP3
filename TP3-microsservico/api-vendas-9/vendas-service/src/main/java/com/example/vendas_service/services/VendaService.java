package com.example.vendas_service.services;

import org.springframework.stereotype.Service;

import com.example.vendas_service.dto.ProdutoDTO;
import com.example.vendas_service.interfaces.ProdutoInterface;
import com.example.vendas_service.models.Venda;
import com.example.vendas_service.repository.VendasRepository;

@Service
public class VendaService {
    
    private final VendasRepository vendaRepository;
    private final ProdutoInterface produtoInterface;

    public VendaService(VendasRepository vendaRepository, ProdutoInterface produtoInterface) {
        this.vendaRepository = vendaRepository;
        this.produtoInterface = produtoInterface;
    }

    public Venda salvarVenda(Long produtoId, int quantidade){
        ProdutoDTO produto = this.produtoInterface.buscarPorId(produtoId);

        if(produto == null){
            throw new RuntimeException("Produto não encontrado");
        }

        Venda venda = new Venda();
        venda.setIdProduto(produto.getId());
        venda.setQuantidade(quantidade);
        venda.setValorProduto(produto.getPreco());

        return vendaRepository.save(venda);
    }

}

