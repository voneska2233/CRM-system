package org.example.controller;

import lombok.AllArgsConstructor;
import org.example.dto.SellerDTO;
import org.example.entity.Seller;
import org.example.service.SellerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class SellerController {
    private final SellerService sellerService;

    @PostMapping("api/sellers")
    public ResponseEntity<Seller> create(@RequestBody SellerDTO sellerDTO) {
        return new ResponseEntity<>(sellerService.createSeller(sellerDTO), HttpStatus.CREATED);
    }

    @GetMapping("api/sellers")
    public ResponseEntity<List<Seller>> getAllSellers() {
        return new ResponseEntity<>(sellerService.getAllSellers(), HttpStatus.OK);
    }

}
