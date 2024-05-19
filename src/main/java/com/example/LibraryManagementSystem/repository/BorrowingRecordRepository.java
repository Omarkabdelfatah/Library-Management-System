package com.example.LibraryManagementSystem.repository;

import com.example.LibraryManagementSystem.entity.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {
    Optional<BorrowingRecord> findFirstByBookIdAndPatronIdOrderByReturnDateAsc(Long bookId, Long patronId);
    Optional<BorrowingRecord> findByBookIdAndPatronIdAndReturnDate(Long bookId, Long patronId, LocalDate returnDate);

}