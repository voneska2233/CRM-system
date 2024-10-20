package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.SellerDTO;
import org.example.entity.Seller;
import org.example.repository.SellerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class SellerControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Seller excitingSeller;

    @Autowired
    private SellerRepository sellerRepository;

    @BeforeEach
    void setUp() {
        excitingSeller = new Seller();
        excitingSeller.setId(1L);
        excitingSeller.setName("Test Seller");
        excitingSeller.setContactInfo("123456789");
        sellerRepository.save(excitingSeller);
    }

    @Test
    void createSellerIT() throws Exception {
        SellerDTO sellerDTO = new SellerDTO();
        sellerDTO.setName("Test");
        sellerDTO.setContactInfo("123456789");

        String sellerDTOJson = objectMapper.writeValueAsString(sellerDTO);

        mockMvc.perform(post("/api/sellers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sellerDTOJson)
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(jsonPath("$.name").value("Test"))
                .andExpect(jsonPath("$.contactInfo").value("123456789"))
                .andExpect(jsonPath("$.registrationDate").exists());

        long count = sellerRepository.count();
        assertEquals(2, count);
    }

    @Test
    void createSellerFailedIT() throws Exception {
        SellerDTO sellerDTO = new SellerDTO();
        sellerDTO.setName("");
        sellerDTO.setContactInfo("123456789");

        String sellerDTOJson = objectMapper.writeValueAsString(sellerDTO);

        mockMvc.perform(post("/api/sellers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sellerDTOJson)
                ).andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Поле имени не должно быть пустым"));
    }

    @Test
    void getSellerByIdIT() throws Exception {
        mockMvc.perform(get("/api/sellers/{id}", excitingSeller.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(excitingSeller.getId()))
                .andExpect(jsonPath("$.name").value(excitingSeller.getName()))
                .andExpect(jsonPath("$.contactInfo").value(excitingSeller.getContactInfo()));
    }

    @Test
    void getSellerByIdFailedIT() throws Exception {
        Long testId = 2L;
        mockMvc.perform(get("/api/sellers/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Продавец с ID " + testId + " не найден"));
    }

    @Test
    void updateSellerIT() throws Exception {
        SellerDTO sellerDTO = new SellerDTO();
        sellerDTO.setName("Seller Update");
        sellerDTO.setContactInfo("123456789");

        String sellerDTOJson = objectMapper.writeValueAsString(sellerDTO);

        mockMvc.perform(put("/api/sellers/{id}", excitingSeller.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(sellerDTOJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(excitingSeller.getId()))
                .andExpect(jsonPath("$.name").value(sellerDTO.getName()))
                .andExpect(jsonPath("$.contactInfo").value(sellerDTO.getContactInfo()));
    }

    @Test
    void updateSellerFailedIT() throws Exception {
        SellerDTO sellerDTO = new SellerDTO();
        sellerDTO.setName("Seller Update");
        sellerDTO.setContactInfo("123456789");
        Long testId = 2L;

        String sellerDTOJson = objectMapper.writeValueAsString(sellerDTO);

        mockMvc.perform(put("/api/sellers/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(sellerDTOJson))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Продавец с ID " + testId + " не найден"));
    }

    @Test
    void deleteSellerIT() throws Exception {
        mockMvc.perform(delete("/api/sellers/{id}", excitingSeller.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        long count = sellerRepository.count();
        assertEquals(0, count);
    }

    @Test
    void deleteSellerFailedIT() throws Exception {
        Long testId = 2L;
        mockMvc.perform(delete("/api/sellers/{id}", testId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Продавец с ID " + testId + " не найден"));
        long count = sellerRepository.count();
        assertEquals(1, count);
    }
}