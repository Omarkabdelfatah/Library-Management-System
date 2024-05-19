package com.example.LibraryManagementSystem.dto;

import com.example.LibraryManagementSystem.entity.Book;
import com.example.LibraryManagementSystem.entity.Patron;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BorrowingRecordDTO {

    @NotEmpty
    private Long id;

    @NotEmpty
    private LocalDateTime borrowDate;

    private LocalDateTime returnDate;


    @NotEmpty
    private BookDTO book;

    @NotEmpty
    private PatronDTO patron;

}
