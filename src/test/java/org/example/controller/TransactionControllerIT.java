package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.SellerDTO;
import org.example.dto.TransactionDTO;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TransactionControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private Transaction excitingTransaction;

    private Seller excitingSeller;

    @BeforeEach
    void setUp() {
        excitingSeller = Seller.builder()
                .id(1L)
                .name("Existing Seller")
                .contactInfo("123456789")
                .build();

        sellerRepository.save(excitingSeller);
        excitingTransaction = Transaction.builder()
                .id(1L)
                .sellerId(excitingSeller)
                .amount(BigDecimal.valueOf(123))
                .paymentType(PaymentType.valueOf("CASH"))
                .build();
        transactionRepository.save(excitingTransaction);
    }

    @Test
    void createTransactionIT() throws Exception {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setSellerId(excitingSeller.getId());
        transactionDTO.setAmount(BigDecimal.valueOf(123));
        transactionDTO.setPaymentType(PaymentType.CASH);

        String transactionDTOJson = objectMapper.writeValueAsString(transactionDTO);

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionDTOJson)
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(jsonPath("$.sellerId.id").value(excitingSeller.getId()))
                .andExpect(jsonPath("$.amount").value(transactionDTO.getAmount()))
                .andExpect(jsonPath("$.paymentType").value(transactionDTO.getPaymentType().toString()));

        long count = transactionRepository.count();
        assertEquals(2, count);
    }

    @Test
    void createTransactionFailedIT() throws Exception {
        Long testId = 2l;
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setSellerId(testId);
        transactionDTO.setAmount(BigDecimal.valueOf(123));
        transactionDTO.setPaymentType(PaymentType.CASH);

        String transactionDTOJson = objectMapper.writeValueAsString(transactionDTO);

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionDTOJson)
                ).andExpect(status().isNotFound())
                .andExpect(content().string("Не существует продавца с ID " + testId));

        long count = transactionRepository.count();
        assertEquals(1, count);
    }

    @Test
    void getTransactionByIdIT() throws Exception {
        mockMvc.perform(get("/api/transactions/{id}", excitingTransaction.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(excitingTransaction.getId()))
                .andExpect(jsonPath("$.sellerId.id").value(excitingTransaction.getSellerId().getId()))
                .andExpect(jsonPath("$.amount").value(excitingTransaction.getAmount().doubleValue()))
                .andExpect(jsonPath("$.paymentType").value(excitingTransaction.getPaymentType().toString()));
    }

    @Test
    void getTransactionByIdFailedIT() throws Exception {
        Long testId = 2l;
        mockMvc.perform(get("/api/transactions/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Не существует транзакции с ID " + testId));
    }

    @Test
    void getTransactionBySellerId() throws Exception {
        mockMvc.perform(get("/api/transactions/seller/{id}", excitingSeller.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(excitingTransaction.getId()))
                .andExpect(jsonPath("$[0].sellerId.id").value(excitingTransaction.getSellerId().getId()))
                .andExpect(jsonPath("$[0].amount").value(excitingTransaction.getAmount().doubleValue()))
                .andExpect(jsonPath("$[0].paymentType").value(excitingTransaction.getPaymentType().toString()));

    }
}