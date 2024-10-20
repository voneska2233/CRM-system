package org.example;

import org.example.entity.PaymentType;
import org.example.entity.Seller;
import org.example.entity.Transaction;
import org.example.repository.SellerRepository;
import org.example.repository.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class DatabaseInitializer {
    @Bean
    public CommandLineRunner initDatabase(SellerRepository sellerRepository, TransactionRepository transactionRepository) {
        return args -> {
            transactionRepository.deleteAll();
            sellerRepository.deleteAll();

            Seller seller1 = new Seller("Иван Иванов", "ivan@example.com", LocalDateTime.of(2022, 1, 15, 10, 0));
            Seller seller2 = new Seller("Мария Петрова", "maria@example.com", LocalDateTime.of(2022, 3, 10, 11, 0));
            Seller seller3 = new Seller("Александр Смирнов", "alexander@example.com", LocalDateTime.of(2023, 5, 5, 12, 0));
            Seller seller4 = new Seller("Ольга Васильева", "olga@example.com", LocalDateTime.of(2023, 7, 20, 13, 0));
            Seller seller5 = new Seller("Дмитрий Кузнецов", "dmitry@example.com", LocalDateTime.of(2024, 2, 25, 14, 0));

            sellerRepository.save(seller1);
            sellerRepository.save(seller2);
            sellerRepository.save(seller3);
            sellerRepository.save(seller4);
            sellerRepository.save(seller5);

            Transaction transaction1 = new Transaction(seller1, BigDecimal.valueOf(100.00), PaymentType.CASH, LocalDateTime.of(2022, 1, 20, 14, 30));
            Transaction transaction2 = new Transaction(seller2, BigDecimal.valueOf(200.00), PaymentType.CARD, LocalDateTime.of(2022, 3, 15, 15, 0));
            Transaction transaction3 = new Transaction(seller1, BigDecimal.valueOf(150.50), PaymentType.TRANSFER, LocalDateTime.of(2023, 1, 10, 16, 0));
            Transaction transaction4 = new Transaction(seller3, BigDecimal.valueOf(300.00), PaymentType.CASH, LocalDateTime.of(2023, 5, 30, 10, 15));
            Transaction transaction5 = new Transaction(seller4, BigDecimal.valueOf(50.75), PaymentType.CARD, LocalDateTime.of(2024, 3, 22, 11, 0));
            Transaction transaction6 = new Transaction(seller5, BigDecimal.valueOf(125.00), PaymentType.TRANSFER, LocalDateTime.of(2024, 2, 28, 12, 0));

            transactionRepository.save(transaction1);
            transactionRepository.save(transaction2);
            transactionRepository.save(transaction3);
            transactionRepository.save(transaction4);
            transactionRepository.save(transaction5);
            transactionRepository.save(transaction6);
        };
    }
}
