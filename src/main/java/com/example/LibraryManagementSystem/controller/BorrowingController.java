package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.dto.BorrowingRecordDTO;
import com.example.LibraryManagementSystem.service.BorrowingRecordService;
import com.example.LibraryManagementSystem.utils.Result;
import com.example.LibraryManagementSystem.utils.ResultStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrow")
public class BorrowingController {

    private final BorrowingRecordService borrowingRecordService;

    public BorrowingController(BorrowingRecordService borrowingRecordService) {
        this.borrowingRecordService = borrowingRecordService;
    }

    @PostMapping("/{bookId}/patron/{patronId}")
    public ResponseEntity borrowBook(@PathVariable Long bookId, @PathVariable Long patronId) {

        Result resultOfBorrow = borrowingRecordService.borrowBook(bookId,patronId);

        // Book Not Found
        if(resultOfBorrow.getObject()==bookId){
            return new ResponseEntity(resultOfBorrow.getErrorMessage(),HttpStatus.NOT_FOUND);
        }
        // Patron Not Found
        else if(resultOfBorrow.getObject()==patronId){
            return new ResponseEntity(resultOfBorrow.getErrorMessage(),HttpStatus.NOT_FOUND);
        }
        // Active Borrowing Record
        else if(resultOfBorrow.getObject()!=null && resultOfBorrow.getStatus()==ResultStatus.ERROR){
            return new ResponseEntity(resultOfBorrow.getErrorMessage()
                    , HttpStatus.BAD_REQUEST);
        }
        // Server Error while adding new borrow record
        else if(resultOfBorrow.getObject()==null && resultOfBorrow.getStatus()==ResultStatus.ERROR){
            return new ResponseEntity(resultOfBorrow.getErrorMessage()
                    , HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // Success Added borrow record
        return new ResponseEntity<BorrowingRecordDTO>((BorrowingRecordDTO) resultOfBorrow.getObject(),HttpStatus.CREATED);

    }

    @PutMapping("/{bookId}/patron/{patronId}")
    public ResponseEntity returnBook(@PathVariable Long bookId, @PathVariable Long patronId) {

        Result<BorrowingRecordDTO> borrowingRecord = borrowingRecordService.returnBook(bookId,patronId);
        // Check if the record exists
        if(borrowingRecord.getStatus()==ResultStatus.NOT_FOUND){
            return new ResponseEntity(borrowingRecord.getErrorMessage(),HttpStatus.NOT_FOUND);
        }
       // Book returned successfully
        return new ResponseEntity(borrowingRecord.getObject(),HttpStatus.OK);

    }

}
