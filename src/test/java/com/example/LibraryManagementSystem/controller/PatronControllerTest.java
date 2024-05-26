package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.dto.PatronDTO;
import com.example.LibraryManagementSystem.service.PatronService;
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

public class PatronControllerTest {

    @Mock
    private PatronService patronService;

    @InjectMocks
    private PatronController patronController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllPatronsSuccess() {
        List<PatronDTO> patronDTOList = List.of(new PatronDTO());
        Result<List<PatronDTO>> result = new Result<List<PatronDTO>>(patronDTOList, ResultStatus.SUCCESS, "Success");

        when(patronService.getAllPatrons()).thenReturn(result);

        ResponseEntity<List<PatronDTO>> responseEntity = patronController.getAllPatrons();

        assertEquals(HttpStatus.FOUND, responseEntity.getStatusCode());
        assertEquals(patronDTOList, responseEntity.getBody());
    }

    @Test
    public void testGetAllPatronsError() {
        Result<List<PatronDTO>> result = new Result<>(null, ResultStatus.ERROR, "Error retrieving patrons");

        when(patronService.getAllPatrons()).thenReturn(result);

        ResponseEntity<List<PatronDTO>> responseEntity = patronController.getAllPatrons();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Error retrieving patrons", responseEntity.getBody());
    }

    @Test
    public void testGetPatronByIdSuccess() {
        Long patronId = 1L;
        PatronDTO patronDTO = new PatronDTO();
        Result<PatronDTO> result = new Result<>(patronDTO, ResultStatus.SUCCESS, "Success");

        when(patronService.getPatronById(patronId)).thenReturn(result);

        ResponseEntity<PatronDTO> responseEntity = patronController.getPatronById(patronId);

        assertEquals(HttpStatus.FOUND, responseEntity.getStatusCode());
        assertEquals(patronDTO, responseEntity.getBody());
    }

    @Test
    public void testGetPatronByIdNotFound() {
        Long patronId = 1L;
        Result<PatronDTO> result = new Result<>(null, ResultStatus.NOT_FOUND, "Patron not found");

        when(patronService.getPatronById(patronId)).thenReturn(result);

        ResponseEntity<PatronDTO> responseEntity = patronController.getPatronById(patronId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Patron not found", responseEntity.getBody());
    }

    @Test
    public void testAddPatronSuccess() {
        PatronDTO patronDTO = new PatronDTO();
        Result<PatronDTO> result = new Result<>(patronDTO, ResultStatus.SUCCESS, "Added successfully");

        when(patronService.addPatron(patronDTO)).thenReturn(result);

        ResponseEntity<PatronDTO> responseEntity = patronController.addPatron(patronDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(patronDTO, responseEntity.getBody());
    }

    @Test
    public void testAddPatronError() {
        PatronDTO patronDTO = new PatronDTO();
        Result<PatronDTO> result = new Result<>(null, ResultStatus.ERROR, "Error adding patron");

        when(patronService.addPatron(patronDTO)).thenReturn(result);

        ResponseEntity<PatronDTO> responseEntity = patronController.addPatron(patronDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Error adding patron", responseEntity.getBody());
    }

    @Test
    public void testUpdatePatronSuccess() {
        Long patronId = 1L;
        PatronDTO patronDTO = new PatronDTO();
        Result<PatronDTO> result = new Result<>(patronDTO, ResultStatus.SUCCESS, "Updated successfully");

        when(patronService.updatePatron(patronId, patronDTO)).thenReturn(result);

        ResponseEntity<PatronDTO> responseEntity = patronController.updatePatron(patronId, patronDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(patronDTO, responseEntity.getBody());
    }

    @Test
    public void testUpdatePatronNotFound() {
        Long patronId = 1L;
        PatronDTO patronDTO = new PatronDTO();
        Result<PatronDTO> result = new Result<>(null, ResultStatus.NOT_FOUND, "Patron not found");

        when(patronService.updatePatron(patronId, patronDTO)).thenReturn(result);

        ResponseEntity<PatronDTO> responseEntity = patronController.updatePatron(patronId, patronDTO);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Patron not found", responseEntity.getBody());
    }

    @Test
    public void testUpdatePatronError() {
        Long patronId = 1L;
        PatronDTO patronDTO = new PatronDTO();
        Result<PatronDTO> result = new Result<>(null, ResultStatus.ERROR, "Error updating patron");

        when(patronService.updatePatron(patronId, patronDTO)).thenReturn(result);

        ResponseEntity<PatronDTO> responseEntity = patronController.updatePatron(patronId, patronDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Error updating patron", responseEntity.getBody());
    }

    @Test
    public void testDeletePatronSuccess() {
        Long patronId = 1L;
        Result<Void> result = new Result<>(null, ResultStatus.SUCCESS, "Deleted successfully");

        when(patronService.deletePatron(patronId)).thenReturn(result);

        ResponseEntity<Void> responseEntity = patronController.deletePatron(patronId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testDeletePatronNotFound() {
        Long patronId = 1L;
        Result<Void> result = new Result<>(null, ResultStatus.NOT_FOUND, "Patron not found");

        when(patronService.deletePatron(patronId)).thenReturn(result);

        ResponseEntity<Void> responseEntity = patronController.deletePatron(patronId);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Patron not found", responseEntity.getBody());
    }
}
