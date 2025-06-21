package com.pardalpizzaria.pizzaria.order.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pardalpizzaria.pizzaria.order.dtos.request.RegisterOrderDto;
import com.pardalpizzaria.pizzaria.order.dtos.response.OrderResponseDto;
import com.pardalpizzaria.pizzaria.order.entity.ItemPedido;
import com.pardalpizzaria.pizzaria.order.entity.Order;
import com.pardalpizzaria.pizzaria.order.enums.StatusPedidos;
import com.pardalpizzaria.pizzaria.order.repository.OrderRepository;
import com.pardalpizzaria.pizzaria.user.entity.User;
import com.pardalpizzaria.pizzaria.user.repository.UserRepository;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public OrderResponseDto createOrder(RegisterOrderDto dto, Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Order order = new Order();
        order.setUsuario(user);
        order.setValorTotal(dto.valorTotal());
        order.setStatus(StatusPedidos.valueOf(dto.status())); // converter string para enum
        order.setDataHora(LocalDateTime.now());

        // Criar lista de itens do pedido
        var itens = dto.itens().stream().map(itemDto -> {
            ItemPedido item = new ItemPedido();
            item.setNome(itemDto.nome());
            item.setQuantidade(itemDto.quantidade());
            item.setPrecoUnitario(itemDto.precoUnitario());
            item.setPedido(order); // importante para o relacionamento bidirecional
            return item;
        }).collect(Collectors.toList());

        order.setItens(itens);

        // Salvar o pedido (cascade salvará itens também)
        orderRepository.save(order);

        // Retornar DTO de resposta
        return new OrderResponseDto(order);
    }

    public List<OrderResponseDto> listAllOrders(){
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(order->new OrderResponseDto(order)).collect(Collectors.toList());
    }

    public void deleteOrder(Long id){
        orderRepository.deleteById(id);
    }
}
