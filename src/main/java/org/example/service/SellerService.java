package org.example.service;

import lombok.AllArgsConstructor;
import org.example.dto.SellerDTO;
import org.example.entity.Seller;
import org.example.repository.SellerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SellerService {
    private final SellerRepository sellerRepository;

    public Seller createSeller(SellerDTO sellerDTO) {
        return sellerRepository.save(Seller.builder()
                .name(sellerDTO.getName())
                .contactInfo(sellerDTO.getContactInfo())
                .build());
    }

    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    public void deleteSeller(Long sellerId) {
        sellerRepository.deleteById(sellerId);
    }

    public Seller getSellerById(Long sellerId) {
        return sellerRepository.findById(sellerId)
                .orElseThrow();
    }
}
