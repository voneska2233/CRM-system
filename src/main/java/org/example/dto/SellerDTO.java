package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SellerDTO {
    @NotBlank(message = "Поле имени не должно быть пустым")
    @Pattern(regexp = "^[A-Za-zА-Яа-яЁё\\s]+$", message = "Некорректно введеные данные (имя содержит только буквы")
    private String name;
    @NotBlank(message = "Поле контактной информации не должно быть пустым")
    private String contactInfo;
}
