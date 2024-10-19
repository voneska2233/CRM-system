package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Поле имени не должно быть пустым")
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁё\\s]+$", message = "Некорректно введеные данные (имя содержит только буквы")
    private String name;
    @NotBlank(message = "Поле контактной информации не должно быть пустым")
    private String contactInfo;
    @CreationTimestamp
    private LocalDateTime registrationDate;
}
