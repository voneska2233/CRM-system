package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.entity.Seller;
import org.example.service.AnalyticsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/analytics")
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @GetMapping("/top-seller/day")
    public ResponseEntity<Optional<Seller>> getTopSellerByDay(@RequestParam String date) {
        return new ResponseEntity<>(analyticsService.getTopSellerByDay(date), HttpStatus.OK);
    }

    @GetMapping("/top-seller/month")
    public ResponseEntity<Optional<Seller>> getTopSellerByMonth(@RequestParam int year, @RequestParam int month) {
        return new ResponseEntity<>(analyticsService.getTopSellerByMonth(year, month), HttpStatus.OK);
    }

    @GetMapping("/top-seller/quarter")
    public ResponseEntity<Optional<Seller>> getTopSellerByQuarter(@RequestParam int year, @RequestParam int quarter) {
        return new ResponseEntity<>(analyticsService.getTopSellerByQuarter(year, quarter), HttpStatus.OK);
    }

    @GetMapping("/top-seller/year")
    public ResponseEntity<Optional<Seller>> getTopSellerByYear(@RequestParam int year) {
        return new ResponseEntity<>(analyticsService.getTopSellerByYear(year), HttpStatus.OK);
    }

    @GetMapping("/below-threshold")
    public ResponseEntity<List<Seller>> getBelowThreshold(@RequestParam String startDate, @RequestParam String endDate, @RequestParam BigDecimal threshold) {
        return new ResponseEntity<>(analyticsService.getBelowThreshold(startDate, endDate, threshold), HttpStatus.OK);
    }
}
