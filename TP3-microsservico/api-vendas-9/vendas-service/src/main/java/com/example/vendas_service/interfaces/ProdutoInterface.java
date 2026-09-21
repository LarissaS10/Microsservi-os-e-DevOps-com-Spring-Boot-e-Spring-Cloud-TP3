package com.example.vendas_service.interfaces;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.vendas_service.dto.ProdutoDTO;

@FeignClient(name="produtos-service")
public interface ProdutoInterface {
    
    @GetMapping("/produtos/{id}")
    ProdutoDTO buscarPorId(@PathVariable Long id);
}
