package com.example.LibraryManagementSystem.service.Impl;


import com.example.LibraryManagementSystem.dto.BookDTO;
import com.example.LibraryManagementSystem.entity.Book;
import com.example.LibraryManagementSystem.repository.BookRepository;
import com.example.LibraryManagementSystem.service.BookService;
import com.example.LibraryManagementSystem.utils.CustomBeanUtils;
import com.example.LibraryManagementSystem.utils.Result;
import com.example.LibraryManagementSystem.utils.ResultStatus;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final ModelMapper modelMapper;

    public BookServiceImpl(BookRepository bookRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    @Transactional
    public Result<BookDTO> addBook(BookDTO bookDTO) {
        try {
            Book savedBook = bookRepository.save(convertToEntity(bookDTO));
            return new Result<BookDTO>(convertToDto(savedBook),
                    ResultStatus.SUCCESS,
                    "Book Added Successfully");
        } catch (Exception e) {
            return new Result(null,
                    ResultStatus.ERROR,
                    e.getMessage());
        }
    }

    @Override
    @Cacheable(value = "bookCache", key = "#id")
    public Result<BookDTO> getBookById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            return new Result<BookDTO>(convertToDto(book.get()),
                    ResultStatus.SUCCESS,
                    "Book Found Successfully");
        } else {
            return new Result(null,
                    ResultStatus.NOT_FOUND,
                    "Book Not Found");
        }
    }

    @Override
    @Cacheable(value = "bookCache")
    public Result<List<BookDTO>> getAllBooks() {
        try {
            List<Book> books = bookRepository.findAll();
            return new Result<List<BookDTO>>(convertToDTOList(books),
                    ResultStatus.SUCCESS,
                    "Books Found Successfully");
        } catch (Exception e) {
            return new Result(null,
                    ResultStatus.ERROR,
                    e.getMessage());
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = "bookCache", key = "#id")
    public Result deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            return new Result(null,
                    ResultStatus.SUCCESS,
                    "Book with Id: " + id + "Deleted Successfully");
        } else {
            return new Result(null,
                    ResultStatus.NOT_FOUND,
                    "Book with id:" + id + " not found");
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = "bookCache", key = "#id")
    public Result<BookDTO> updateBook(Long id, BookDTO bookDTO) {

        if (bookRepository.existsById(id)) {
            Book bookToUpdate = bookRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Book with ID:" + id + " Not Found! "));

            Book updatedBook = convertToEntity(bookDTO);
            CustomBeanUtils.copyNonNullProperties(updatedBook, bookToUpdate);
            try {
                Book savedBook = bookRepository.save(bookToUpdate);
                return new Result<BookDTO>(convertToDto(savedBook),
                        ResultStatus.SUCCESS,
                        "Book With id: " + id + " Updated Successfully");
            } catch (Exception e) {
                return new Result(updatedBook, ResultStatus.ERROR, "Error Updating Book: " + e.getMessage());
            }
        } else {
            return new Result(null, ResultStatus.NOT_FOUND, "Book with id: " + id + " Not Found");
        }

    }

    public BookDTO convertToDto(Book entity) {
        return modelMapper.map(entity, BookDTO.class);
    }

    public List<BookDTO> convertToDTOList(List<Book> entities) {
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Book convertToEntity(BookDTO dto) {
        return modelMapper.map(dto, Book.class);
    }

}