package com.example.LibraryManagementSystem.service.Impl;

import com.example.LibraryManagementSystem.dto.BookDTO;
import com.example.LibraryManagementSystem.dto.BorrowingRecordDTO;
import com.example.LibraryManagementSystem.entity.Book;
import com.example.LibraryManagementSystem.entity.BorrowingRecord;
import com.example.LibraryManagementSystem.entity.Patron;
import com.example.LibraryManagementSystem.repository.BorrowingRecordRepository;
import com.example.LibraryManagementSystem.repository.PatronRepository;
import com.example.LibraryManagementSystem.service.BorrowingRecordService;
import com.example.LibraryManagementSystem.service.PatronService;
import com.example.LibraryManagementSystem.utils.Result;
import com.example.LibraryManagementSystem.utils.ResultStatus;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BorrowingRecordServiceImpl implements BorrowingRecordService {

    private final BorrowingRecordRepository borrowingRecordRepository;

    private final ModelMapper modelMapper;

    public BorrowingRecordServiceImpl (BorrowingRecordRepository borrowingRecordRepository ,ModelMapper modelMapper){
        this.borrowingRecordRepository =borrowingRecordRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public Result<BorrowingRecordDTO> addBorrowingRecord(BorrowingRecordDTO borrowingRecordDTO) {
        try {
            BorrowingRecord savedBorrowingRecord = borrowingRecordRepository.save(convertToEntity(borrowingRecordDTO));
            return  new Result<BorrowingRecordDTO>(convertToDto(savedBorrowingRecord),
                    ResultStatus.SUCCESS,
                    "BorrowingRecord Added Successfully");
        } catch (Exception e) {
            return new Result(null ,
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
        Optional<BorrowingRecord> borrowingRecord = borrowingRecordRepository.findByBookIdAndPatronIdAndReturnDate(bookId, patronId,returnDate);
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
            return new Result( null,
                    ResultStatus.NOT_FOUND,
                    e.getMessage());
        }
    }

    @Override
    public Result<Void> deleteBorrowingRecord(Long id) {
        if (borrowingRecordRepository.existsById(id)) {
            borrowingRecordRepository.deleteById(id);
            return new Result(null,
                    ResultStatus.SUCCESS,
                    "BorrowingRecord with Id: "+id+"Deleted Successfully");
        } else {
            return new Result(null,
                    ResultStatus.NOT_FOUND,
                    "BorrowingRecord with id:"+id+" Not Found");
        }
    }

    @Override
    public Result<BorrowingRecordDTO> updateBorrowingRecord(Long id, BorrowingRecordDTO borrowingRecordDTO) {

        if (borrowingRecordRepository.existsById(id)) {
            BorrowingRecord borrowingRecord = convertToEntity(borrowingRecordDTO);
            borrowingRecord.setId(id); // Set to the one being updated
            try {
                BorrowingRecord updatedBorrowingRecord = borrowingRecordRepository.save(borrowingRecord);
                return new Result<BorrowingRecordDTO>(convertToDto(updatedBorrowingRecord),
                        ResultStatus.SUCCESS,
                        "BorrowingRecord With id: "+id+" Updated Successfully");
            } catch (Exception e) {
                return new Result( convertToDto(borrowingRecord),ResultStatus.ERROR,
                        "Error Updating BorrowingRecord: "+e.getMessage());
            }
        } else {
            return new Result(null,ResultStatus.NOT_FOUND,"BorrowingRecord with id: "+id +" Not Found");
        }

    }

    public BorrowingRecordDTO convertToDto(BorrowingRecord entity) {
        return modelMapper.map(entity, BorrowingRecordDTO.class);
    }

    public List<BorrowingRecordDTO> convertToDTOList(List<BorrowingRecord> entities){
        return  entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public BorrowingRecord convertToEntity(BorrowingRecordDTO dto) {
        return modelMapper.map(dto, BorrowingRecord.class);
    }

}
