package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.dto.BookDTO;
import com.example.LibraryManagementSystem.entity.Book;
import com.example.LibraryManagementSystem.utils.Result;

import java.util.List;

public interface BookService {

    Result<BookDTO> addBook(BookDTO bookDTO);
    Result<BookDTO> getBookById(Long id);
    Result<List<BookDTO>> getAllBooks();
    Result<Void> deleteBook(Long id);
    Result<BookDTO> updateBook(Long id, BookDTO bookDTO);

}