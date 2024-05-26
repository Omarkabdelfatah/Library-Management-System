
package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.dto.BorrowingRecordDTO;
import com.example.LibraryManagementSystem.service.BorrowingRecordService;
import com.example.LibraryManagementSystem.utils.Result;
import com.example.LibraryManagementSystem.utils.ResultStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class BorrowingControllerTest {

    @Mock
    private BorrowingRecordService borrowingRecordService;

    @InjectMocks
    private BorrowingController borrowingController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBorrowBookSuccess() {
        Long bookId = 1L;
        Long patronId = 1L;
        BorrowingRecordDTO borrowingRecordDTO = new BorrowingRecordDTO();
        Result<BorrowingRecordDTO> result = new Result<>(borrowingRecordDTO, ResultStatus.SUCCESS, "Borrowed successfully");

        when(borrowingRecordService.borrowBook(bookId, patronId)).thenReturn(result);

        ResponseEntity<?> responseEntity = borrowingController.borrowBook(bookId, patronId);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(borrowingRecordDTO, responseEntity.getBody());
    }

    @Test
    public void testBorrowBookBookNotFound() {
        Long bookId = 1L;
        Long patronId = 1L;
        Result result = new Result<>(bookId, ResultStatus.NOT_FOUND, "Book not found");

        when(borrowingRecordService.borrowBook(bookId, patronId)).thenReturn(result);

        ResponseEntity<?> responseEntity = borrowingController.borrowBook(bookId, patronId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Book not found", responseEntity.getBody());
    }

    @Test
    public void testBorrowBookPatronNotFound() {
        Long bookId = 1L;
        Long patronId = 1L;
        Result result = new Result<>(patronId, ResultStatus.NOT_FOUND, "Patron not found");

        when(borrowingRecordService.borrowBook(bookId, patronId)).thenReturn(result);

        ResponseEntity<?> responseEntity = borrowingController.borrowBook(bookId, patronId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Patron not found", responseEntity.getBody());
    }

    @Test
    public void testBorrowBookActiveBorrowingRecord() {
        Long bookId = 1L;
        Long patronId = 1L;
        BorrowingRecordDTO borrowingRecordDTO = new BorrowingRecordDTO();
        Result<BorrowingRecordDTO> result = new Result<>(borrowingRecordDTO, ResultStatus.ERROR, "Already borrowing");

        when(borrowingRecordService.borrowBook(bookId, patronId)).thenReturn(result);

        ResponseEntity<?> responseEntity = borrowingController.borrowBook(bookId, patronId);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Already borrowing", responseEntity.getBody());
    }

    @Test
    public void testBorrowBookServerError() {
        Long bookId = 1L;
        Long patronId = 1L;
        Result<BorrowingRecordDTO> result = new Result<>(null, ResultStatus.ERROR, "Server error");

        when(borrowingRecordService.borrowBook(bookId, patronId)).thenReturn(result);

        ResponseEntity<?> responseEntity = borrowingController.borrowBook(bookId, patronId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Server error", responseEntity.getBody());
    }
}
