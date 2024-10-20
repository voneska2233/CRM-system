package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    public Transaction(Seller sellerId, BigDecimal amount, PaymentType paymentType, LocalDateTime transactionDate) {
        this.sellerId = sellerId;
        this.amount = amount;
        this.paymentType = paymentType;
        this.transactionDate = transactionDate;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller sellerId;
    @NotNull(message = "Поле amount не должен быть null")
    @PositiveOrZero(message = "Значение amount должно быть неотрицательным")
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    @CreationTimestamp
    private LocalDateTime transactionDate;
}
