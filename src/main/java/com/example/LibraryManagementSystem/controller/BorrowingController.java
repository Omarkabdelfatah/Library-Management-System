package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.dto.BookDTO;
import com.example.LibraryManagementSystem.dto.BorrowingRecordDTO;
import com.example.LibraryManagementSystem.dto.PatronDTO;
import com.example.LibraryManagementSystem.service.BookService;
import com.example.LibraryManagementSystem.service.BorrowingRecordService;
import com.example.LibraryManagementSystem.service.PatronService;
import com.example.LibraryManagementSystem.utils.Result;
import com.example.LibraryManagementSystem.utils.ResultStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/borrow")
public class BorrowingController {

    private final BorrowingRecordService borrowingRecordService;

    private final BookService bookService;

    private final PatronService patronService;

    public BorrowingController(BorrowingRecordService borrowingRecordService,BookService bookService,
                                PatronService patronService) {
        this.borrowingRecordService = borrowingRecordService;
        this.bookService =bookService;
        this.patronService = patronService;
    }

    @PostMapping("/{bookId}/patron/{patronId}")
    public ResponseEntity borrowBook(@PathVariable Long bookId, @PathVariable Long patronId) {

        Result<BorrowingRecordDTO> checkBorrowingRecord = new Result<BorrowingRecordDTO>();
        BorrowingRecordDTO newBorrowingRecord = new BorrowingRecordDTO();

        // check if the book exists
        Result<BookDTO> borrowedBook = bookService.getBookById(bookId);
        // check if the patron exists
        Result<PatronDTO> patron = patronService.getPatronById(patronId);

        if(borrowedBook.getStatus() == ResultStatus.SUCCESS && patron.getStatus() == ResultStatus.SUCCESS){

            // check if there's borrow record with this patron and book id
            checkBorrowingRecord = borrowingRecordService.getBorrowingRecordByBookIdAndPatronId(bookId,patronId);


            if(checkBorrowingRecord.getStatus()==ResultStatus.SUCCESS && checkBorrowingRecord.getObject().getReturnDate()==null) {
                return new ResponseEntity("Patron with id: " + patronId + " is borrowing book with id: "
                                                + bookId + " Now!"
                                                , HttpStatus.BAD_REQUEST);
            }
            // new borrow record for this patron with this book
            else {
                newBorrowingRecord.setBook(borrowedBook.getObject());
                newBorrowingRecord.setPatron(patron.getObject());
                newBorrowingRecord.setBorrowDate(LocalDateTime.now());
                newBorrowingRecord.setReturnDate(null);
            }
        }
        else if(borrowedBook.getStatus()== ResultStatus.NOT_FOUND){
            return new ResponseEntity("Book with id: "+bookId+" Not Found.",HttpStatus.NOT_FOUND);
        }

        else if(patron.getStatus()== ResultStatus.NOT_FOUND){
            return new ResponseEntity("Patron with id: "+patronId+" Not Found.",HttpStatus.NOT_FOUND);
        }

        Result<BorrowingRecordDTO> result = borrowingRecordService.addBorrowingRecord(newBorrowingRecord);

        if (result.getStatus()== ResultStatus.ERROR) {
            return new ResponseEntity(result.getErrorMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<BorrowingRecordDTO>(result.getObject(),HttpStatus.CREATED);

    }

    @PutMapping("/{bookId}/patron/{patronId}")
    public ResponseEntity returnBook(@PathVariable Long bookId, @PathVariable Long patronId) {

        Result<BorrowingRecordDTO> borrowingRecord =
                    borrowingRecordService.getBorrowingRecordByBookIdAndPatronIdAndReturnDate(bookId,patronId,null);
        if(borrowingRecord.getStatus()==ResultStatus.NOT_FOUND){
            return new ResponseEntity(borrowingRecord.getErrorMessage(),HttpStatus.NOT_FOUND);
        }
        borrowingRecord.getObject().setReturnDate(LocalDateTime.now());
        borrowingRecord=borrowingRecordService.updateBorrowingRecord(borrowingRecord.getObject().getId(), borrowingRecord.getObject());

        return new ResponseEntity(borrowingRecord.getObject(),HttpStatus.OK);

    }

}
