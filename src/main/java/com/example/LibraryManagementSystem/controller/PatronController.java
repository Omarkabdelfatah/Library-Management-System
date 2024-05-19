package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.dto.BookDTO;
import com.example.LibraryManagementSystem.dto.PatronDTO;
import com.example.LibraryManagementSystem.service.BookService;
import com.example.LibraryManagementSystem.service.PatronService;
import com.example.LibraryManagementSystem.utils.Result;
import com.example.LibraryManagementSystem.utils.ResultStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patrons")
public class PatronController {
    private final PatronService patronService;

    public PatronController(PatronService patronService){
        this.patronService = patronService;
    }

    @GetMapping
    public ResponseEntity<List<PatronDTO>> getAllPatrons() {
        Result<List<PatronDTO>> result = patronService.getAllPatrons();
        if (result.getStatus()== ResultStatus.ERROR) {
            return new ResponseEntity(result.getErrorMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<List<PatronDTO>>(result.getObject(),HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatronDTO> getPatronById(@PathVariable Long id) {
        Result<PatronDTO> result = patronService.getPatronById(id);
        if (result.getStatus()== ResultStatus.NOT_FOUND) {
            return new ResponseEntity(result.getErrorMessage(),HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<PatronDTO>(result.getObject(),HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<PatronDTO> addPatron(@RequestBody PatronDTO patronDTO) {
        Result<PatronDTO> result = patronService.addPatron(patronDTO);
        if (result.getStatus()== ResultStatus.ERROR) {
            return new ResponseEntity(result.getErrorMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<PatronDTO>(result.getObject(),HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatronDTO> updatePatron(@PathVariable Long id, @RequestBody PatronDTO patronDTO) {

        Result<PatronDTO> result = patronService.updatePatron(id, patronDTO);

        if (result.getStatus()== ResultStatus.ERROR) {
            return new ResponseEntity(result.getErrorMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else if (result.getStatus()== ResultStatus.NOT_FOUND) {
            return new ResponseEntity(result.getErrorMessage(),HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<PatronDTO>(result.getObject(),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePatron(@PathVariable Long id) {
        Result<Void> result = patronService.deletePatron(id);
        if (result.getStatus()== ResultStatus.NOT_FOUND) {
            return new ResponseEntity(result.getErrorMessage(),HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<BookDTO>(HttpStatus.OK);
    }
}
