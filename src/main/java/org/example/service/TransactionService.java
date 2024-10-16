package org.example.service;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.dto.TransactionDTO;
import org.example.entity.Seller;
import org.example.entity.Transaction;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.SellerRepository;
import org.example.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@AllArgsConstructor
@Validated
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final SellerRepository sellerRepository;

    public Transaction createTransaction(@Valid TransactionDTO transactionDTO) {
        Seller seller = sellerRepository.findById(transactionDTO.getSellerId())
                .orElseThrow(() -> new ResourceNotFoundException("Не существует продавца с ID " + transactionDTO.getSellerId()));
        return transactionRepository.save(Transaction.builder()
                        .sellerId(seller)
                        .amount(transactionDTO.getAmount())
                        .paymentType(transactionDTO.getPaymentType())
                        .build());
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Не существует транзакции с ID " + id));
    }

    public List<Transaction> getTransactionBySellerId(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Не существует продавца с ID " + sellerId));
        return transactionRepository.findBySellerId(seller);
    }
}
