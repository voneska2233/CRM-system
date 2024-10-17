package org.example.repository;

import org.example.entity.Seller;
import org.example.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySellerId(Seller sellerId);
    @Query("SELECT t FROM Transaction t " +
            "WHERE t.transactionDate BETWEEN :startDate AND :endDate")
    List<Transaction> findAllByPeriod(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);
}
