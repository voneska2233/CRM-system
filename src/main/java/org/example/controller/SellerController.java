package org.example.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.dto.SellerDTO;
import org.example.entity.Seller;
import org.example.service.SellerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/sellers")
@Validated
public class SellerController {
    private final SellerService sellerService;

    @PostMapping
    public ResponseEntity<Seller> createSeller(@Valid @RequestBody SellerDTO sellerDTO) {
        return new ResponseEntity<>(sellerService.createSeller(sellerDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Seller>> getAllSellers() {
        return new ResponseEntity<>(sellerService.getAllSellers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id) {
        return new ResponseEntity<>(sellerService.getSellerById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Seller> updateSeller(@PathVariable Long id, @Valid @RequestBody SellerDTO sellerDTO) {
        return new ResponseEntity<>(sellerService.updateSeller(id, sellerDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public HttpStatus deleteSeller(@PathVariable Long id) {
        sellerService.deleteSeller(id);
        return HttpStatus.OK;
    }
}
