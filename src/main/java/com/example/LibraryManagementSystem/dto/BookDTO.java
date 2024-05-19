package com.example.LibraryManagementSystem.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {


    @NotEmpty
    private Long Id;

    @NotEmpty
    private String title;

    @NotEmpty
    private String author;

    private int publicationYear;

    private String ISBN;

    private String description;

    private double price;

    private boolean availability;


}
