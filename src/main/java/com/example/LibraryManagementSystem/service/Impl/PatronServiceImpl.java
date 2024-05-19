package com.example.LibraryManagementSystem.service.Impl;

import com.example.LibraryManagementSystem.dto.BorrowingRecordDTO;
import com.example.LibraryManagementSystem.dto.PatronDTO;
import com.example.LibraryManagementSystem.entity.Book;
import com.example.LibraryManagementSystem.entity.BorrowingRecord;
import com.example.LibraryManagementSystem.entity.Patron;
import com.example.LibraryManagementSystem.repository.PatronRepository;
import com.example.LibraryManagementSystem.service.PatronService;
import com.example.LibraryManagementSystem.utils.CustomBeanUtils;
import com.example.LibraryManagementSystem.utils.Result;
import com.example.LibraryManagementSystem.utils.ResultStatus;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PatronServiceImpl implements PatronService {

    private final PatronRepository patronRepository;

    private final ModelMapper modelMapper;

    public PatronServiceImpl (PatronRepository patronRepository,ModelMapper modelMapper){
        this.patronRepository =patronRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Result<PatronDTO> addPatron(PatronDTO patronDTO) {
        try {
            Patron savedPatron = patronRepository.save(convertToEntity(patronDTO));
            return  new Result<PatronDTO>(convertToDto(savedPatron),
                    ResultStatus.SUCCESS,
                    "Patron Added Successfully");
        } catch (Exception e) {
            return new Result(null ,
                    ResultStatus.ERROR,
                    e.getMessage());
        }
    }

    @Override
    public Result<PatronDTO> getPatronById(Long id) {

        Optional<Patron> patron = patronRepository.findById(id);
        if (patron.isPresent()) {
            return new Result<PatronDTO>(convertToDto(patron.get()),
                    ResultStatus.SUCCESS,
                    "Patron Found Successfully");
        } else {
            return new Result(null,
                    ResultStatus.NOT_FOUND,
                    "Patron Not Found");
        }
    }

    @Override
    public Result<List<PatronDTO>> getAllPatrons() {
        try {
            List<Patron> patrons = patronRepository.findAll();
            return new Result<List<PatronDTO>>(convertToDTOList(patrons),
                    ResultStatus.SUCCESS,
                    "Patrons Found Successfully");
        } catch (Exception e) {
            return new Result( null,
                    ResultStatus.NOT_FOUND,
                    e.getMessage());
        }
    }

    @Override
    public Result<Void> deletePatron(Long id) {
        if (patronRepository.existsById(id)) {
            patronRepository.deleteById(id);
            return new Result(null,
                    ResultStatus.SUCCESS,
                    "Patron with Id: "+id+"Deleted Successfully");
        } else {
            return new Result(null,
                    ResultStatus.NOT_FOUND,
                    "Patron with id:"+id+" Not Found");
        }
    }

    @Override
    public Result<PatronDTO> updatePatron(Long id, PatronDTO patronDTO) {
        if (patronRepository.existsById(id)) {
            Patron patronToUpdate= patronRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Patron with ID:"+ id +" Not Found! "));
            Patron updatedPatron = convertToEntity(patronDTO);
            CustomBeanUtils.copyNonNullProperties(updatedPatron, patronToUpdate);
            try {
                Patron savedPatron = patronRepository.saveAndFlush(patronToUpdate);
                return new Result<PatronDTO>(convertToDto(savedPatron),
                        ResultStatus.SUCCESS,
                        "Patron With id: "+id+" Updated Successfully");
            } catch (Exception e) {
                return new Result( updatedPatron,ResultStatus.ERROR,
                        "Error Updating Patron: "+e.getMessage());
            }
        } else {
            return new Result(null,ResultStatus.NOT_FOUND,"Patron with id: "+id +" Not Found");
        }
    }

    public PatronDTO convertToDto(Patron entity) {
        return modelMapper.map(entity, PatronDTO.class);
    }

    public List<PatronDTO> convertToDTOList(List<Patron> entities){
        return  entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    public Patron convertToEntity(PatronDTO dto) {
        return modelMapper.map(dto, Patron.class);
    }

}
