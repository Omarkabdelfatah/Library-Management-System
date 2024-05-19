package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.dto.PatronDTO;
import com.example.LibraryManagementSystem.entity.BorrowingRecord;
import com.example.LibraryManagementSystem.entity.Patron;
import com.example.LibraryManagementSystem.utils.Result;

import java.util.List;

public interface PatronService {
    Result<PatronDTO> addPatron(PatronDTO patronDTO);
    Result<PatronDTO> getPatronById(Long id);
    Result<List<PatronDTO>> getAllPatrons();
    Result<Void> deletePatron(Long id);
    Result<PatronDTO> updatePatron(Long id, PatronDTO patronDTO);

}
