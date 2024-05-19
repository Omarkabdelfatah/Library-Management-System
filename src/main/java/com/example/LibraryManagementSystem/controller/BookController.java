package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.dto.BookDTO;
import com.example.LibraryManagementSystem.service.BookService;
import com.example.LibraryManagementSystem.utils.Result;
import com.example.LibraryManagementSystem.utils.ResultStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService){
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity getAllBooks() {

        Result<List<BookDTO>> result = bookService.getAllBooks();
        if (result.getStatus()== ResultStatus.ERROR) {
            return new ResponseEntity(result.getErrorMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<List<BookDTO>>(result.getObject(),HttpStatus.FOUND);

    }

    @GetMapping("/{id}")
    public ResponseEntity getBookById(@PathVariable Long id) {

        Result<BookDTO> result = bookService.getBookById(id);
        if (result.getStatus()== ResultStatus.NOT_FOUND) {
            return new ResponseEntity(result.getErrorMessage(),HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<BookDTO>(result.getObject(),HttpStatus.FOUND);

    }

    @PostMapping
    public ResponseEntity addBook(@RequestBody BookDTO bookDTO) {
        Result<BookDTO> result = bookService.addBook(bookDTO);
        if (result.getStatus()== ResultStatus.ERROR) {
            return new ResponseEntity(result.getErrorMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<BookDTO>(result.getObject(),HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) {

        Result<BookDTO> result = bookService.updateBook(id, bookDTO);

        if (result.getStatus()== ResultStatus.ERROR) {
            return new ResponseEntity(result.getErrorMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else if (result.getStatus()== ResultStatus.NOT_FOUND) {
            return new ResponseEntity(result.getErrorMessage(),HttpStatus.NOT_FOUND);
        }
        System.out.println(result.getObject().getAuthor());
        return new ResponseEntity<BookDTO>(result.getObject(),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteBook(@PathVariable Long id) {
        Result<Void> result = bookService.deleteBook(id);
        if (result.getStatus()== ResultStatus.NOT_FOUND) {
            return new ResponseEntity(result.getErrorMessage(),HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<BookDTO>(HttpStatus.OK);
    }


}
