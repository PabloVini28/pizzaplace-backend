package com.pardalpizzaria.pizzaria.order.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.pardalpizzaria.pizzaria.email.EmailService;
import com.pardalpizzaria.pizzaria.order.dtos.request.RegisterOrderDto;
import com.pardalpizzaria.pizzaria.order.dtos.response.OrderResponseDto;
import com.pardalpizzaria.pizzaria.order.entity.Order;
import com.pardalpizzaria.pizzaria.order.enums.StatusPedidos;
import com.pardalpizzaria.pizzaria.order.repository.OrderRepository;
import com.pardalpizzaria.pizzaria.order.service.OrderService;
import com.pardalpizzaria.pizzaria.user.entity.User;
import com.pardalpizzaria.pizzaria.user.repository.UserRepository;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    @Value("${api.security.token.secret}")
    private String secret;

    @PostMapping("/create")
    public ResponseEntity<OrderResponseDto> createOrder(
            @RequestBody RegisterOrderDto orderDto,
            @RequestHeader("Authorization") String authorizationHeader) {

        Long userId = extractUserIdFromToken(authorizationHeader);

        OrderResponseDto createdOrder = orderService.createOrder(orderDto, userId);
        return ResponseEntity.ok(createdOrder);
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrderResponseDto>> listAllOrders() {
        List<OrderResponseDto> orders = orderService.listAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/today")
    public List<OrderResponseDto> getTodayOrders() {
        LocalDate today = LocalDate.now();

        return orderRepository.findAll().stream()
                .filter(order -> order.getCreatedAt().toLocalDate().isEqual(today))
                .map(OrderResponseDto::new)
                .toList();
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> statusMap) {
        String newStatus = statusMap.get("status");
        
        return orderRepository.findById(id).map(order -> {
            try {
                order.setStatus(StatusPedidos.valueOf(newStatus));
                orderRepository.save(order);
                return ResponseEntity.ok().build();
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Status inválido");
            }
        }).orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    private Long extractUserIdFromToken(String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.require(algorithm)
                .withIssuer("auth-api")
                .build()
                .verify(token)
                .getClaim("userId").asLong();
    }

    @PostMapping("/{id}/send-email")
    public ResponseEntity<Void> sendEmailOrderConfimation(@PathVariable Long id) {
        Optional<Order> order = orderRepository.findById(id);
        Optional<User> user = userRepository.findById(order.get().getUsuario().getId());
        emailService.sendEmailConfirmation(user);
        return ResponseEntity.noContent().build();
    }


}
