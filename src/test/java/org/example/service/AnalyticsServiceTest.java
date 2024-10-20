package org.example.service;

import org.example.entity.PaymentType;
import org.example.entity.Seller;
import org.example.entity.Transaction;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AnalyticsService analyticsService;

    private Seller existingSeller1;
    private Seller existingSeller2;

    private Transaction existingTransaction1;
    private Transaction existingTransaction2;

    @BeforeEach
    void setUp() {
        existingSeller1 = Seller.builder()
                .id(1L)
                .name("Test Seller1")
                .contactInfo("123456789")
                .build();

        existingSeller2 = Seller.builder()
                .id(2L)
                .name("Test Seller2")
                .contactInfo("987654321")
                .build();

        existingTransaction1 = Transaction.builder()
                .id(1L)
                .sellerId(existingSeller1)
                .amount(BigDecimal.valueOf(100))
                .paymentType(PaymentType.CARD)
                .transactionDate(LocalDateTime.now())
                .build();

        existingTransaction2 = Transaction.builder()
                .id(2L)
                .sellerId(existingSeller2)
                .amount(BigDecimal.valueOf(200))
                .paymentType(PaymentType.CARD)
                .transactionDate(LocalDateTime.now())
                .build();
    }

    @Test
    void getTopSellerByDayTest() {
        LocalDateTime startOfDay = LocalDateTime.parse("2024-10-20T00:00:00");
        LocalDateTime endOfDay = LocalDateTime.parse("2024-10-20T23:59:59");

        List<Transaction> transactions = List.of(existingTransaction1, existingTransaction2);

        when(transactionRepository.findAllByPeriod(startOfDay, endOfDay)).thenReturn(transactions);

        Optional<Seller> topSeller = analyticsService.getTopSellerByDay("2024-10-20");

        assertTrue(topSeller.isPresent());
        assertEquals(existingSeller2, topSeller.get());

        verify(transactionRepository, times(1)).findAllByPeriod(startOfDay, endOfDay);
    }

    @Test
    void getTopSellerByDayFailedTest() {
        LocalDateTime startOfDay = LocalDateTime.parse("2024-10-21T00:00:00");
        LocalDateTime endOfDay = LocalDateTime.parse("2024-10-21T23:59:59");

        List<Transaction> transactions = Collections.emptyList();

        when(transactionRepository.findAllByPeriod(startOfDay, endOfDay)).thenReturn(transactions);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> analyticsService.getTopSellerByDay("2024-10-21"));

        assertEquals("В данный период нет транзакций", exception.getMessage());

        verify(transactionRepository, times(1)).findAllByPeriod(startOfDay, endOfDay);
    }

    @Test
    void getBelowThreshold() {
        LocalDateTime startOfDay = LocalDateTime.parse("2024-10-20T00:00:00");
        LocalDateTime endOfDay = LocalDateTime.parse("2024-10-21T23:59:59");

        List<Transaction> transactions = List.of(existingTransaction1, existingTransaction2);

        when(transactionRepository.findAllByPeriod(startOfDay, endOfDay)).thenReturn(transactions);

        List<Seller> createdSellers = analyticsService.getBelowThreshold("2024-10-20", "2024-10-21", BigDecimal.valueOf(200));

        assertEquals(1, createdSellers.size());
        assertEquals(existingSeller1, createdSellers.get(0));
        verify(transactionRepository, times(1)).findAllByPeriod(startOfDay, endOfDay);
    }
}