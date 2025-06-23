package com.pardalpizzaria.pizzaria.order.dtos.response;

import com.pardalpizzaria.pizzaria.order.entity.Order;

public record OrderResponseDto(
    Long id,
    String nomeCliente,
    String telefoneCliente,
    String enderecoEntrega,
    Double valorTotal,
    String statusPedidos,
    String dataCriacao
) {
    public OrderResponseDto(Order order){
        this(
            order.getId(),
            order.getUsuario().getName(),
            order.getUsuario().getPhoneNumber(),
            order.getUsuario().getAddress(),
            order.getValorTotal(),
            order.getStatus().name(),
            order.getCreatedAt().toString()
        );
    }
}
