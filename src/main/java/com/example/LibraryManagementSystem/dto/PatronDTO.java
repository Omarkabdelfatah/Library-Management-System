package com.example.LibraryManagementSystem.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatronDTO {


    @NotEmpty
    private Long id;

    @NotEmpty
    private String name;

    @Email
    private String email;

    private String phoneNumber;

}
