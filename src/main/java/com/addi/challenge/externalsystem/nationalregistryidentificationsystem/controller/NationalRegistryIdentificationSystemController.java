package com.addi.challenge.externalsystem.nationalregistryidentificationsystem.controller;

import com.addi.challenge.externalsystem.nationalregistryidentificationsystem.entity.Person;
import com.addi.challenge.externalsystem.nationalregistryidentificationsystem.exception.PersonMismatchException;
import com.addi.challenge.externalsystem.nationalregistryidentificationsystem.exception.PersonNotFoundException;
import com.addi.challenge.externalsystem.nationalregistryidentificationsystem.exception.PersonNotProvidedException;
import com.addi.challenge.externalsystem.nationalregistryidentificationsystem.service.NationalRegistryIdentificationSystemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nationalregistry")
@Slf4j
public class NationalRegistryIdentificationSystemController {

    private final NationalRegistryIdentificationSystemService nationalRegistryIdentificationSystemService;

    public NationalRegistryIdentificationSystemController(NationalRegistryIdentificationSystemService nationalRegistryIdentificationSystemService) {
        this.nationalRegistryIdentificationSystemService = nationalRegistryIdentificationSystemService;
    }

    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Fetched all the people from National Registry",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "503",
                    description = "Service is not available",
                    content = @Content)

    })
    public ResponseEntity<List<Person>> findAll() {
        return new ResponseEntity<>(nationalRegistryIdentificationSystemService.findAll(), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "This method is to save a specific person to National Registry")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Saved a person to National Registry",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "The person was not found at National Registry",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "406",
                    description = "The correct information was not sent to database",
                    content = {@Content(mediaType = "application/json")}),

            @ApiResponse(responseCode = "503",
                    description = "The service is not available",
                    content = @Content)
    })
    public ResponseEntity<Person> save(@RequestBody Person person) {
        try {
            Person savedPerson = nationalRegistryIdentificationSystemService.save(person);
            return new ResponseEntity<>(savedPerson, HttpStatus.CREATED);
        } catch (PersonMismatchException | PersonNotProvidedException e) {
            log.info(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{nationalIdentificationNumber}")
    @ResponseBody
    @Operation(summary = "This is to fetch a specific person stored in National Registry by using its national ID number as key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Fetched a person from National Registry",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "The person was not found at National Registry",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "503",
                    description = "The service is not available",
                    content = @Content)
    })
    public ResponseEntity<Person> findByNationalIdentificationNumber(@PathVariable("nationalIdentificationNumber") String nationalIdentificationNumber) {
        Person person = nationalRegistryIdentificationSystemService.findByNationalIdentificationNumber(nationalIdentificationNumber);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }

    @DeleteMapping("/{nationalIdentificationNumber}")
    @Operation(summary = "This operation is to delete a specific person stored in National Registry by using its national ID number as key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Fetched a person from National Registry",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    description = "The person was not found at National Registry",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "503",
                    description = "The service is not available",
                    content = @Content)
    })
    void deleteByNationalIdentificationNumber(@PathVariable String nationalIdentificationNumber) {
        try {
            nationalRegistryIdentificationSystemService.deleteByNationalIdentificationNumber(nationalIdentificationNumber);
        } catch (PersonNotFoundException e) {
            log.info(e.getMessage());
            new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
