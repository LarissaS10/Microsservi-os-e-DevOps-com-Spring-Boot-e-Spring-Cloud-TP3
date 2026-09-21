package com.example.vendas_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VendaDTO {
    private Long id;
    private Long idProduto;
    private Integer quantidade;
    private Double preco;
}
