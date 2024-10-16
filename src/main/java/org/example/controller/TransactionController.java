package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.dto.TransactionDTO;
import org.example.entity.Transaction;
import org.example.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        return new ResponseEntity<>(transactionService.createTransaction(transactionDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return new ResponseEntity<>(transactionService.getAllTransactions(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable Long id) {
        return new ResponseEntity<>(transactionService.getTransactionById(id), HttpStatus.OK);
    }

    @GetMapping("/seller/{id}")
    public ResponseEntity<List<Transaction>> getTransactionBySellerId(@PathVariable Long id) {
        return new ResponseEntity<>(transactionService.getTransactionBySellerId(id), HttpStatus.OK);
    }
}
