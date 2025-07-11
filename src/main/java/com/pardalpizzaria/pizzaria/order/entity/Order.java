package com.pardalpizzaria.pizzaria.order.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.pardalpizzaria.pizzaria.order.enums.StatusPedidos;
import com.pardalpizzaria.pizzaria.user.entity.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedido", orphanRemoval = true)
    private List<ItemPedido> itens;

    @Column(name="valorTotal", nullable=false)
    private Double valorTotal;

    @Enumerated(jakarta.persistence.EnumType.STRING)
    private StatusPedidos status;

    @Column(name="dataHora", nullable=false)
    private LocalDateTime dataHora;

    @Column(name="observacao", length=500)
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @Column(name="createdAt", nullable=false,updatable=false)
    private LocalDateTime createdAt;

    @Column(name="updatedAt", nullable=false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.dataHora = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate(){
        this.updatedAt = LocalDateTime.now();
    }

}
