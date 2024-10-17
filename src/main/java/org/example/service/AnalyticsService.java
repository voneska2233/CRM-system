package org.example.service;

import lombok.AllArgsConstructor;
import org.example.entity.Seller;
import org.example.entity.Transaction;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AnalyticsService {
    private final TransactionRepository transactionRepository;

    public Optional<Seller> getTopSellerByDay(String date){
        LocalDateTime startOfDay = LocalDateTime.parse(date + "T00:00:00");
        LocalDateTime endOfDay = LocalDateTime.parse(date + "T23:59:59");
        return findTopSellerByPeriod(startOfDay, endOfDay);
    }

    public Optional<Seller> getTopSellerByMonth(int year, int month){
        LocalDateTime startOfDay = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endOfDay = startOfDay.plusMonths(1).minusSeconds(1);
        return findTopSellerByPeriod(startOfDay, endOfDay);
    }

    public Optional<Seller> getTopSellerByQuarter(int year, int quarter){
        int startMonth = (quarter - 1) * 3 + 1;
        LocalDateTime startOfDay = LocalDateTime.of(year, startMonth, 1, 0, 0);
        LocalDateTime endOfDay = startOfDay.plusMonths(3).minusSeconds(1);
        return findTopSellerByPeriod(startOfDay, endOfDay);
    }

    public Optional<Seller> getTopSellerByYear(int year){
        LocalDateTime startOfDay = LocalDateTime.of(year, 1, 1, 0, 0, 0);
        LocalDateTime endOfDay = startOfDay.plusYears(1).minusSeconds(1);
        return findTopSellerByPeriod(startOfDay, endOfDay);
    }

    private Optional<Seller> findTopSellerByPeriod(LocalDateTime startOfDay, LocalDateTime endOfDay) {
        List<Transaction> transactions = transactionRepository.findAllByPeriod(startOfDay, endOfDay);

        Map<Seller, BigDecimal> sellerSales = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getSellerId,
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)));

        return Optional.ofNullable(sellerSales.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new ResourceNotFoundException("В данный период нет транзакций")));
    }


    public List<Seller> getBelowThreshold(String startDate, String endDate, BigDecimal thresholdAmount) {
        LocalDateTime startOfDay = LocalDateTime.parse(startDate + "T00:00:00");
        LocalDateTime endOfDay = LocalDateTime.parse(endDate + "T23:59:59");
        List<Transaction> transactions = transactionRepository.findAllByPeriod(startOfDay, endOfDay);

        Map<Seller, BigDecimal> sellerSales = transactions.stream()
                .collect(Collectors.groupingBy(Transaction::getSellerId,
                        Collectors.reducing(BigDecimal.ZERO, Transaction::getAmount, BigDecimal::add)));

        return sellerSales.entrySet().stream()
                .filter(entry -> entry.getValue().compareTo(thresholdAmount) < 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
