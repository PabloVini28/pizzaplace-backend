package com.pardalpizzaria.pizzaria.order.dtos.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record RegisterOrderDto(
    @NotNull
    @Positive
    Double valorTotal,

    @NotNull
    @Size(min = 1)
    List<ItemOrderDto> itens,

    @NotNull
    String status
) {
    public record ItemOrderDto(
        @NotNull
        String nome,

        @NotNull
        @Positive
        Integer quantidade,

        @NotNull
        @Positive
        Double precoUnitario
    ) {}
}
