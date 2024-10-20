package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entity.PaymentType;
import org.example.entity.Seller;
import org.example.entity.Transaction;
import org.example.repository.SellerRepository;
import org.example.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@AutoConfigureMockMvc
class AnalyticsControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private Seller existingSeller1;
    private Seller existingSeller2;

    private Transaction existingTransaction1;
    private Transaction existingTransaction2;

    @BeforeEach
    void setUp() {
        existingSeller1 = Seller.builder()
                .id(1L)
                .name("Test SellerOne")
                .contactInfo("123456789")
                .build();

        existingSeller2 = Seller.builder()
                .id(2L)
                .name("Test SellerTwo")
                .contactInfo("987654321")
                .build();

        sellerRepository.save(existingSeller1);
        sellerRepository.save(existingSeller2);

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

        transactionRepository.save(existingTransaction1);
        transactionRepository.save(existingTransaction2);
    }

    @Test
    void getTopSellerByDayIT() throws Exception {
        String date = "2024-10-20";
        mockMvc.perform(get("/api/analytics/top-seller/day")
                .param("date", date))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(existingSeller2.getId()))
                .andExpect(jsonPath("$.name").value(existingSeller2.getName()))
                .andExpect(jsonPath("$.contactInfo").value(existingSeller2.getContactInfo()));
    }

    @Test
    void getBelowThresholdIT() throws Exception {
        String startDate = "2024-10-20";
        String endDate = "2024-10-21";
        String threshold = "200";
        mockMvc.perform(get("/api/analytics/below-threshold")
                .param("startDate", startDate)
                .param("endDate", endDate)
                .param("threshold", threshold))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(existingSeller1.getId()))
                .andExpect(jsonPath("$[0].name").value(existingSeller1.getName()))
                .andExpect(jsonPath("$[0].contactInfo").value(existingSeller1.getContactInfo()));
    }
}