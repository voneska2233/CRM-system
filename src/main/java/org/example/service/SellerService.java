package org.example.service;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.dto.SellerDTO;
import org.example.entity.Seller;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.SellerRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@AllArgsConstructor
@Validated
public class SellerService {
    private final SellerRepository sellerRepository;

    public Seller createSeller(@Valid SellerDTO sellerDTO) {
        return sellerRepository.save(Seller.builder()
                .name(sellerDTO.getName())
                .contactInfo(sellerDTO.getContactInfo())
                .build());
    }

    public Seller getSellerById(Long sellerId) {
        return sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Продавец с ID " + sellerId + " не найден"));
    }

    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }

    public Seller updateSeller(Long sellerId, @Valid SellerDTO sellerDTO) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Продавец с ID " + sellerId + " не найден"));
        seller.setName(sellerDTO.getName());
        seller.setContactInfo(sellerDTO.getContactInfo());
        return sellerRepository.save(seller);
    }

    public void deleteSeller(Long sellerId) {
        if (!sellerRepository.existsById(sellerId))
            throw new ResourceNotFoundException("Продавец с ID " + sellerId + " не найден");
        sellerRepository.deleteById(sellerId);
    }

}
