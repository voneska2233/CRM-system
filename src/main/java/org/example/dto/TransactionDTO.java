package org.example.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.example.entity.PaymentType;

import java.math.BigDecimal;

@Data
public class TransactionDTO {
    private Long sellerId;
    @NotNull(message = "Поле amount не должен быть null")
    @PositiveOrZero(message = "Значение amount должно быть неотрицательным")
    private BigDecimal amount;
    private PaymentType paymentType;
}
