package org.example.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.example.entity.Seller;
import org.example.service.AnalyticsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/analytics")
@Validated
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @GetMapping("/top-seller/day")
    public ResponseEntity<Optional<Seller>> getTopSellerByDay(
            @RequestParam("date")
            @NotBlank(message = "Поле даты не дожно быть пустым")
            String date) {
        return new ResponseEntity<>(analyticsService.getTopSellerByDay(date), HttpStatus.OK);
    }

    @GetMapping("/top-seller/month")
    public ResponseEntity<Optional<Seller>> getTopSellerByMonth(
            @RequestParam("year")
            @NotNull(message = "Поле year не должно быть пустым")
            int year,
            @RequestParam("month")
            @NotNull(message = "Поле month не должно быть пустым")
            @Min(value = 1, message = "Месяц должен быть от 1 до 12")
            @Max(value = 12, message = "Месяц должен быть от 1 до 12")
            int month) {
        return new ResponseEntity<>(analyticsService.getTopSellerByMonth(year, month), HttpStatus.OK);
    }

    @GetMapping("/top-seller/quarter")
    public ResponseEntity<Optional<Seller>> getTopSellerByQuarter(
            @RequestParam("year")
            @NotNull(message = "Поле year не должно быть пустым")
            int year,
            @RequestParam("quarter")
            @NotNull(message = "Поле quarter не должно быть пустым")
            @Min(value = 1, message = "Квартал должен быть от 1 до 4")
            @Max(value = 4, message = "Квартал должен быть от 1 до 4")
            int quarter) {
        return new ResponseEntity<>(analyticsService.getTopSellerByQuarter(year, quarter), HttpStatus.OK);
    }

    @GetMapping("/top-seller/year")
    public ResponseEntity<Optional<Seller>> getTopSellerByYear(
            @RequestParam("year")
            @NotNull(message = "Поле year не должно быть пустым")
            int year) {
        return new ResponseEntity<>(analyticsService.getTopSellerByYear(year), HttpStatus.OK);
    }

    @GetMapping("/below-threshold")
    public ResponseEntity<List<Seller>> getBelowThreshold(
            @RequestParam("startDate")
            @NotBlank(message = "Поле startDate не дожно быть пустым")
            String startDate,
            @RequestParam("endDate")
            @NotBlank(message = "Поле endDate не дожно быть пустым")
            String endDate,
            @RequestParam("threshold")
            @NotNull(message = "Поле threshold не должно быть пустым")
            BigDecimal threshold) {
        return new ResponseEntity<>(analyticsService.getBelowThreshold(startDate, endDate, threshold), HttpStatus.OK);
    }
}
