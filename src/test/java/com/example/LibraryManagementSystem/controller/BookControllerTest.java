package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.dto.BookDTO;
import com.example.LibraryManagementSystem.service.BookService;
import com.example.LibraryManagementSystem.utils.Result;
import com.example.LibraryManagementSystem.utils.ResultStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllBooksSuccess() {
        List<BookDTO> bookDTOList = List.of(new BookDTO());
        Result<List<BookDTO>> result = new Result<List<BookDTO>>(bookDTOList, ResultStatus.SUCCESS, "Success");

        when(bookService.getAllBooks()).thenReturn(result);

        ResponseEntity<List<BookDTO>> responseEntity = bookController.getAllBooks();

        assertEquals(HttpStatus.FOUND, responseEntity.getStatusCode());
        assertEquals(bookDTOList, responseEntity.getBody());
    }

    @Test
    public void testGetAllBooksError() {
        Result<List<BookDTO>> result = new Result<>(null, ResultStatus.ERROR, "Error retrieving books");

        when(bookService.getAllBooks()).thenReturn(result);

        ResponseEntity<List<BookDTO>> responseEntity = bookController.getAllBooks();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Error retrieving books", responseEntity.getBody());
    }

    @Test
    public void testGetBookByIdSuccess() {
        Long bookId = 1L;
        BookDTO bookDTO = new BookDTO();
        Result<BookDTO> result = new Result<>(bookDTO, ResultStatus.SUCCESS, "Success");

        when(bookService.getBookById(bookId)).thenReturn(result);

        ResponseEntity<BookDTO> responseEntity = bookController.getBookById(bookId);

        assertEquals(HttpStatus.FOUND, responseEntity.getStatusCode());
        assertEquals(bookDTO, responseEntity.getBody());
    }

    @Test
    public void testGetBookByIdNotFound() {
        Long bookId = 1L;
        Result<BookDTO> result = new Result<>(null, ResultStatus.NOT_FOUND, "Book not found");

        when(bookService.getBookById(bookId)).thenReturn(result);

        ResponseEntity<BookDTO> responseEntity = bookController.getBookById(bookId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Book not found", responseEntity.getBody());
    }

    @Test
    public void testAddBookSuccess() {
        BookDTO bookDTO = new BookDTO();
        Result<BookDTO> result = new Result<>(bookDTO, ResultStatus.SUCCESS, "Added successfully");

        when(bookService.addBook(bookDTO)).thenReturn(result);

        ResponseEntity<BookDTO> responseEntity = bookController.addBook(bookDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(bookDTO, responseEntity.getBody());
    }

    @Test
    public void testAddBookError() {
        BookDTO bookDTO = new BookDTO();
        Result<BookDTO> result = new Result<>(null, ResultStatus.ERROR, "Error adding book");

        when(bookService.addBook(bookDTO)).thenReturn(result);

        ResponseEntity<BookDTO> responseEntity = bookController.addBook(bookDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Error adding book", responseEntity.getBody());
    }

    @Test
    public void testUpdateBookSuccess() {
        Long bookId = 1L;
        BookDTO bookDTO = new BookDTO();
        Result<BookDTO> result = new Result<>(bookDTO, ResultStatus.SUCCESS, "Updated successfully");

        when(bookService.updateBook(bookId, bookDTO)).thenReturn(result);

        ResponseEntity<BookDTO> responseEntity = bookController.updateBook(bookId, bookDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(bookDTO, responseEntity.getBody());
    }

    @Test
    public void testUpdateBookNotFound() {
        Long bookId = 1L;
        BookDTO bookDTO = new BookDTO();
        Result<BookDTO> result = new Result<>(null, ResultStatus.NOT_FOUND, "Book not found");

        when(bookService.updateBook(bookId, bookDTO)).thenReturn(result);

        ResponseEntity<BookDTO> responseEntity = bookController.updateBook(bookId, bookDTO);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Book not found", responseEntity.getBody());
    }

    @Test
    public void testUpdateBookError() {
        Long bookId = 1L;
        BookDTO bookDTO = new BookDTO();
        Result<BookDTO> result = new Result<>(null, ResultStatus.ERROR, "Error updating book");

        when(bookService.updateBook(bookId, bookDTO)).thenReturn(result);

        ResponseEntity<BookDTO> responseEntity = bookController.updateBook(bookId, bookDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Error updating book", responseEntity.getBody());
    }

    @Test
    public void testDeleteBookSuccess() {
        Long bookId = 1L;
        Result<Void> result = new Result<>(null, ResultStatus.SUCCESS, "Deleted successfully");

        when(bookService.deleteBook(bookId)).thenReturn(result);

        ResponseEntity<Void> responseEntity = bookController.deleteBook(bookId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteBookNotFound() {
        Long bookId = 1L;
        Result<Void> result = new Result<>(null, ResultStatus.NOT_FOUND, "Book not found");

        when(bookService.deleteBook(bookId)).thenReturn(result);

        ResponseEntity<Void> responseEntity = bookController.deleteBook(bookId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Book not found", responseEntity.getBody());
    }
}
