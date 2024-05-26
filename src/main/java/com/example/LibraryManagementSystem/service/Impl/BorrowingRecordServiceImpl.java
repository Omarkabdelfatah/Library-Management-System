package com.example.LibraryManagementSystem.service.Impl;

import com.example.LibraryManagementSystem.dto.BookDTO;
import com.example.LibraryManagementSystem.dto.BorrowingRecordDTO;
import com.example.LibraryManagementSystem.dto.PatronDTO;
import com.example.LibraryManagementSystem.entity.BorrowingRecord;
import com.example.LibraryManagementSystem.repository.BorrowingRecordRepository;
import com.example.LibraryManagementSystem.service.BookService;
import com.example.LibraryManagementSystem.service.BorrowingRecordService;
import com.example.LibraryManagementSystem.service.PatronService;
import com.example.LibraryManagementSystem.utils.Result;
import com.example.LibraryManagementSystem.utils.ResultStatus;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BorrowingRecordServiceImpl implements BorrowingRecordService {

    private final BorrowingRecordRepository borrowingRecordRepository;

    private final BookService bookService;

    private final PatronService patronService;

    private final ModelMapper modelMapper;

    public BorrowingRecordServiceImpl(BorrowingRecordRepository borrowingRecordRepository, BookService bookService, PatronService patronService, ModelMapper modelMapper) {
        this.borrowingRecordRepository = borrowingRecordRepository;
        this.bookService = bookService;
        this.patronService = patronService;
        this.modelMapper = modelMapper;
    }


    @Override
    @Transactional
    public Result borrowBook(Long bookId, Long patronId) {

        Result<BorrowingRecordDTO> checkBorrowingRecord = new Result<BorrowingRecordDTO>();

        BorrowingRecordDTO newBorrowingRecord = new BorrowingRecordDTO();

        // check if the book exists
        Result<BookDTO> borrowedBook = bookService.getBookById(bookId);

        // check if the patron exists
        Result<PatronDTO> patron = patronService.getPatronById(patronId);

        if (borrowedBook.getStatus() == ResultStatus.SUCCESS && patron.getStatus() == ResultStatus.SUCCESS) {

            // check if there's borrow record with this patron and book id
            checkBorrowingRecord = getBorrowingRecordByBookIdAndPatronId(bookId, patronId);


            if (checkBorrowingRecord.getStatus() == ResultStatus.SUCCESS
                    && checkBorrowingRecord.getObject().getReturnDate() == null) {

                return new Result<BorrowingRecordDTO>(checkBorrowingRecord.getObject(),
                        ResultStatus.ERROR,
                        "Patron with id: " + patronId + " is borrowing book with id: "
                                + bookId + " Now!");

            }
            // new borrow record for this patron with this book
            else {

                newBorrowingRecord.setBook(borrowedBook.getObject());
                newBorrowingRecord.setPatron(patron.getObject());
                newBorrowingRecord.setBorrowDate(LocalDateTime.now());
                newBorrowingRecord.setReturnDate(null);

            }
        } else if (borrowedBook.getStatus() == ResultStatus.NOT_FOUND) {

            return new Result(bookId,
                    ResultStatus.NOT_FOUND,
                    "Book with id: " + bookId + " Not Found."
            );

        } else if (patron.getStatus() == ResultStatus.NOT_FOUND) {

            return new Result(patronId,
                    ResultStatus.NOT_FOUND,
                    "Patron with id: " + patronId + " Not Found."
            );

        }

        Result<BorrowingRecordDTO> resultOfAdding = addBorrowingRecord(newBorrowingRecord);

        if (resultOfAdding.getStatus() == ResultStatus.ERROR) {
            return new Result(null,
                    ResultStatus.ERROR,
                    resultOfAdding.getErrorMessage()
            );

        }

        return new Result<BorrowingRecordDTO>(resultOfAdding.getObject(),
                ResultStatus.SUCCESS,
                resultOfAdding.getErrorMessage()
        );

    }

    @Override
    @Transactional
    public Result returnBook(Long bookId, Long patronId) {

        Result<BorrowingRecordDTO> borrowingRecord = getBorrowingRecordByBookIdAndPatronIdAndReturnDate(bookId, patronId, null);

        if (borrowingRecord.getStatus() == ResultStatus.NOT_FOUND) {

            return new Result(borrowingRecord.getObject(),
                    ResultStatus.NOT_FOUND,
                    borrowingRecord.getErrorMessage());

        }

        borrowingRecord.getObject().setReturnDate(LocalDateTime.now());

        borrowingRecord = updateBorrowingRecord(borrowingRecord.getObject().getId(), borrowingRecord.getObject());

        return new Result(borrowingRecord.getObject(),
                ResultStatus.SUCCESS,
                borrowingRecord.getErrorMessage());


    }

    @Override
    @Transactional
    public Result<BorrowingRecordDTO> addBorrowingRecord(BorrowingRecordDTO borrowingRecordDTO) {
        try {

            BorrowingRecord savedBorrowingRecord = borrowingRecordRepository.save(convertToEntity(borrowingRecordDTO));

            return new Result<BorrowingRecordDTO>(convertToDto(savedBorrowingRecord),
                    ResultStatus.SUCCESS,
                    "BorrowingRecord Added Successfully");

        } catch (Exception e) {

            return new Result(null,
                    ResultStatus.ERROR,
                    e.getMessage());

        }
    }

    @Override
    public Result<BorrowingRecordDTO> getBorrowingRecordById(Long id) {
        Optional<BorrowingRecord> borrowingRecord = borrowingRecordRepository.findById(id);
        if (borrowingRecord.isPresent()) {
            return new Result<BorrowingRecordDTO>(convertToDto(borrowingRecord.get()),
                    ResultStatus.SUCCESS,
                    "BorrowingRecord Found Successfully");
        } else {
            return new Result(null,
                    ResultStatus.NOT_FOUND,
                    "BorrowingRecord Not Found");
        }
    }

    @Override
    public Result<BorrowingRecordDTO> getBorrowingRecordByBookIdAndPatronId(Long bookId, Long patronId) {
        Optional<BorrowingRecord> borrowingRecord = borrowingRecordRepository.findFirstByBookIdAndPatronIdOrderByReturnDateAsc(bookId, patronId);
        if (borrowingRecord.isPresent()) {
            return new Result<BorrowingRecordDTO>(convertToDto(borrowingRecord.get()),
                    ResultStatus.SUCCESS,
                    "BorrowingRecord Found Successfully");
        } else {
            return new Result(null,
                    ResultStatus.NOT_FOUND,
                    "BorrowingRecord Not Found");
        }
    }

    @Override
    public Result<BorrowingRecordDTO> getBorrowingRecordByBookIdAndPatronIdAndReturnDate(Long bookId, Long patronId, LocalDate returnDate) {
        Optional<BorrowingRecord> borrowingRecord = borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDate(bookId, patronId, returnDate);
        if (borrowingRecord.isPresent()) {
            return new Result<BorrowingRecordDTO>(convertToDto(borrowingRecord.get()),
                    ResultStatus.SUCCESS,
                    "BorrowingRecord Found Successfully");
        } else {
            return new Result(null,
                    ResultStatus.NOT_FOUND,
                    "BorrowingRecord Not Found");
        }
    }

    @Override
    public Result<List<BorrowingRecordDTO>> getAllBorrowingRecords() {
        try {
            List<BorrowingRecord> borrowingRecords = borrowingRecordRepository.findAll();
            return new Result<List<BorrowingRecordDTO>>(convertToDTOList(borrowingRecords),
                    ResultStatus.SUCCESS,
                    "BorrowingRecords Found Successfully");
        } catch (Exception e) {
            return new Result(null,
                    ResultStatus.NOT_FOUND,
                    e.getMessage());
        }
    }

    @Override
    @Transactional
    public Result<Void> deleteBorrowingRecord(Long id) {
        if (borrowingRecordRepository.existsById(id)) {
            borrowingRecordRepository.deleteById(id);
            return new Result(null,
                    ResultStatus.SUCCESS,
                    "BorrowingRecord with Id: " + id + "Deleted Successfully");
        } else {
            return new Result(null,
                    ResultStatus.NOT_FOUND,
                    "BorrowingRecord with id:" + id + " Not Found");
        }
    }

    @Override
    @Transactional
    public Result<BorrowingRecordDTO> updateBorrowingRecord(Long id, BorrowingRecordDTO borrowingRecordDTO) {

        if (borrowingRecordRepository.existsById(id)) {
            BorrowingRecord borrowingRecord = convertToEntity(borrowingRecordDTO);
            borrowingRecord.setId(id); // Set to the one being updated
            try {
                BorrowingRecord updatedBorrowingRecord = borrowingRecordRepository.save(borrowingRecord);
                return new Result<BorrowingRecordDTO>(convertToDto(updatedBorrowingRecord),
                        ResultStatus.SUCCESS,
                        "BorrowingRecord With id: " + id + " Updated Successfully");
            } catch (Exception e) {
                return new Result(convertToDto(borrowingRecord), ResultStatus.ERROR,
                        "Error Updating BorrowingRecord: " + e.getMessage());
            }
        } else {
            return new Result(null, ResultStatus.NOT_FOUND, "BorrowingRecord with id: " + id + " Not Found");
        }

    }

    public BorrowingRecordDTO convertToDto(BorrowingRecord entity) {
        return modelMapper.map(entity, BorrowingRecordDTO.class);
    }

    public List<BorrowingRecordDTO> convertToDTOList(List<BorrowingRecord> entities) {
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public BorrowingRecord convertToEntity(BorrowingRecordDTO dto) {
        return modelMapper.map(dto, BorrowingRecord.class);
    }

}
