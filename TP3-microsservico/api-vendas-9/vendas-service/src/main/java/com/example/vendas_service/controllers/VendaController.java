package com.example.vendas_service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.vendas_service.dto.VendaDTO;
import com.example.vendas_service.models.Venda;
import com.example.vendas_service.services.VendaService;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/vendas")
public class VendaController {
    
    private final VendaService service;

    public VendaController(VendaService service) {
        this.service = service;
    }

    @GetMapping
    public String get() {
        return "ESTOU AQUI!";
    }
    

    @PostMapping
    public ResponseEntity<VendaDTO> salvarVenda(@RequestBody VendaDTO vendaDTO) {
        Venda savedVenda = service.salvarVenda(vendaDTO.getIdProduto(), vendaDTO.getQuantidade());
        VendaDTO dto = new VendaDTO(savedVenda.getId(), savedVenda.getIdProduto(), savedVenda.getQuantidade(), savedVenda.getValorProduto());
        return ResponseEntity.ok(dto);
    }

}
