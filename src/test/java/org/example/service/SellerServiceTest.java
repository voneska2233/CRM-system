package org.example.service;

import org.example.dto.SellerDTO;
import org.example.entity.Seller;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.SellerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SellerServiceTest {

    @Mock
    private SellerRepository sellerRepository;

    @InjectMocks
    private SellerService sellerService;

    private Seller existingSeller;

    @BeforeEach
    void setUp() {
        existingSeller = Seller.builder()
                .id(1L)
                .name("Existing Seller")
                .contactInfo("123456789")
                .build();
    }

    @Test
    void createSellerTest() {
        SellerDTO sellerDTO = new SellerDTO();
        sellerDTO.setName("Test Seller");
        sellerDTO.setContactInfo("89675435453");

        Seller savedSeller = Seller.builder()
                .name("Test Seller")
                .contactInfo("89675435453")
                .build();
        when(sellerRepository.save(savedSeller)).thenReturn(savedSeller);

        Seller createdSeller = sellerService.createSeller(sellerDTO);

        assertNotNull(createdSeller);
        assertEquals(createdSeller.getName(), sellerDTO.getName());
        assertEquals(createdSeller.getContactInfo(), sellerDTO.getContactInfo());
        verify(sellerRepository, times(1)).save(savedSeller);
    }


    @Test
    void getSellerByIdFailedTest() {
        Long id = 100L;
        when(sellerRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> sellerService.getSellerById(id));

        assertEquals("Продавец с ID " + id + " не найден", exception.getMessage());
    }

    @Test
    void getSellerByIdTest() {
        when(sellerRepository.findById(existingSeller.getId())).thenReturn(Optional.of(existingSeller));

        Seller foundSeller = sellerService.getSellerById(existingSeller.getId());

        assertNotNull(foundSeller);
        assertEquals(existingSeller.getId(), foundSeller.getId());
        assertEquals(existingSeller.getName(), foundSeller.getName());
        assertEquals(existingSeller.getContactInfo(), foundSeller.getContactInfo());
        verify(sellerRepository).findById(existingSeller.getId());
    }


    @Test
    void updateSellerTest() {
        SellerDTO sellerDTO = new SellerDTO();
        sellerDTO.setName("Test Seller");
        sellerDTO.setContactInfo("89675435453");

        when(sellerRepository.findById(existingSeller.getId())).thenReturn(Optional.of(existingSeller));
        when(sellerRepository.save(existingSeller)).thenAnswer(invocation -> {
            Seller sellerToSave = invocation.getArgument(0);
            existingSeller.setName(sellerToSave.getName());
            existingSeller.setContactInfo(sellerToSave.getContactInfo());
            return existingSeller;
        });

        Seller updateSeller = sellerService.updateSeller(existingSeller.getId(), sellerDTO);

        assertNotNull(updateSeller);
        assertEquals(updateSeller.getId(), existingSeller.getId());
        assertEquals(updateSeller.getName(), sellerDTO.getName());
        assertEquals(updateSeller.getContactInfo(), sellerDTO.getContactInfo());
        verify(sellerRepository, times(1)).findById(existingSeller.getId());
        verify(sellerRepository, times(1)).save(argThat(seller ->
                seller.getName().equals(sellerDTO.getName()) &&
                        seller.getContactInfo().equals(sellerDTO.getContactInfo())
        ));
    }

    @Test
    void updateSellerFailedTest() {
        SellerDTO sellerDTO = new SellerDTO();
        sellerDTO.setName("Test Seller");
        sellerDTO.setContactInfo("89675435453");
        Long testId = 100L;

        when(sellerRepository.findById(testId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> sellerService.updateSeller(testId, sellerDTO));

        assertEquals("Продавец с ID " + testId + " не найден", exception.getMessage());
        verify(sellerRepository, times(1)).findById(testId);
    }

    @Test
    void deleteSellerTest() {
        when(sellerRepository.existsById(existingSeller.getId())).thenReturn(true);

        sellerService.deleteSeller(existingSeller.getId());

        verify(sellerRepository, times(1)).deleteById(existingSeller.getId());

    }

    @Test
    void deleteSellerFailedTest() {
        Long testId = 100L;

        when(sellerRepository.existsById(testId)).thenReturn(false);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> sellerService.deleteSeller(testId));

        assertEquals("Продавец с ID " + testId + " не найден", exception.getMessage());
        verify(sellerRepository, never()).deleteById(existingSeller.getId());

    }
}