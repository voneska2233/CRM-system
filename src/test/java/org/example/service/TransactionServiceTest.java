package org.example.service;

import org.example.dto.TransactionDTO;
import org.example.entity.PaymentType;
import org.example.entity.Seller;
import org.example.entity.Transaction;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.SellerRepository;
import org.example.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private SellerRepository sellerRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Transaction excitingTransaction;

    private Seller excitingSeller;

    @BeforeEach
    void setUp() {
        excitingSeller = Seller.builder()
                .id(1L)
                .name("Existing Seller")
                .contactInfo("123456789")
                .build();

        excitingTransaction = Transaction.builder()
                .id(1L)
                .sellerId(excitingSeller)
                .amount(BigDecimal.valueOf(123))
                .paymentType(PaymentType.valueOf("CASH"))
                .build();
    }

    @Test
    void createTransactionTest() {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setSellerId(excitingSeller.getId());
        transactionDTO.setAmount(BigDecimal.valueOf(1234));
        transactionDTO.setPaymentType(PaymentType.valueOf("CASH"));

        when(sellerRepository.findById(excitingSeller.getId())).thenReturn(Optional.of(excitingSeller));

        Transaction savedTransaction = Transaction.builder()
                .sellerId(excitingSeller)
                .amount(BigDecimal.valueOf(1234))
                .paymentType(PaymentType.valueOf("CASH"))
                .build();

        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        Transaction createdTransaction = transactionService.createTransaction(transactionDTO);

        assertNotNull(createdTransaction);
        assertEquals(createdTransaction.getSellerId().getId(), transactionDTO.getSellerId());
        assertEquals(createdTransaction.getAmount(), transactionDTO.getAmount());
        assertEquals(createdTransaction.getPaymentType(), transactionDTO.getPaymentType());
        verify(sellerRepository, times(1)).findById(excitingSeller.getId());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void createTransactionFailedTest() {
        Long testId = 100L;
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setSellerId(testId);
        transactionDTO.setAmount(BigDecimal.valueOf(1234));
        transactionDTO.setPaymentType(PaymentType.valueOf("CASH"));

        when(sellerRepository.findById(testId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> transactionService.createTransaction(transactionDTO));

        assertEquals("Не существует продавца с ID " + testId, exception.getMessage());
        verify(sellerRepository, times(1)).findById(testId);
        verify(transactionRepository, never()).save(any(Transaction.class));
    }


    @Test
    void getTransactionByIdTest() {
        when(transactionRepository.findById(excitingTransaction.getId())).thenReturn(Optional.of(excitingTransaction));

        Transaction createdTransaction = transactionService.getTransactionById(excitingTransaction.getId());

        assertNotNull(createdTransaction);
        assertEquals(createdTransaction.getId(), excitingTransaction.getId());
        assertEquals(createdTransaction.getSellerId(), excitingTransaction.getSellerId());
        assertEquals(createdTransaction.getAmount(), excitingTransaction.getAmount());
        assertEquals(createdTransaction.getPaymentType(), excitingTransaction.getPaymentType());
        verify(transactionRepository, times(1)).findById(excitingTransaction.getId());
    }

    @Test
    void getTransactionBySellerIdTest() {
        when(sellerRepository.findById(excitingSeller.getId())).thenReturn(Optional.of(excitingSeller));
        when(transactionRepository.findBySellerId(excitingSeller)).thenReturn(List.of(excitingTransaction));

        List<Transaction> transactions = transactionService.getTransactionBySellerId(excitingSeller.getId());

        assertNotNull(transactions);
        assertEquals(transactions.size(), 1);
        assertEquals(transactions.get(0).getSellerId().getId(), excitingSeller.getId());
        verify(sellerRepository, times(1)).findById(excitingSeller.getId());
        verify(transactionRepository, times(1)).findBySellerId(excitingSeller);
    }

    @Test
    void getTransactionBySellerIdFailedTest() {
        Long testId = 100L;
        when(sellerRepository.findById(testId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> transactionService.getTransactionBySellerId(testId));

        assertEquals("Не существует продавца с ID " + testId, exception.getMessage());
        verify(sellerRepository, times(1)).findById(testId);
        verify(transactionRepository, never()).findBySellerId(excitingSeller);
    }

}