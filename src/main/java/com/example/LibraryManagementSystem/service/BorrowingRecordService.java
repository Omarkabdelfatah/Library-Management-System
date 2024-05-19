package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.dto.BorrowingRecordDTO;
import com.example.LibraryManagementSystem.utils.Result;

import java.time.LocalDate;
import java.util.List;

public interface BorrowingRecordService {
    Result<BorrowingRecordDTO> addBorrowingRecord(BorrowingRecordDTO borrowingRecordDTO);
    Result<BorrowingRecordDTO> getBorrowingRecordById(Long id);
    Result<BorrowingRecordDTO> getBorrowingRecordByBookIdAndPatronId(Long bookId, Long patronId);
    Result<BorrowingRecordDTO> getBorrowingRecordByBookIdAndPatronIdAndReturnDate(Long bookId, Long patronId, LocalDate returnDate);
    Result<List<BorrowingRecordDTO>> getAllBorrowingRecords();
    Result<Void> deleteBorrowingRecord(Long id);
    Result<BorrowingRecordDTO> updateBorrowingRecord(Long id, BorrowingRecordDTO borrowingRecordDTO);

}
