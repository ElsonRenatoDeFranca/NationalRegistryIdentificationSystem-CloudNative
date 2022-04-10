package com.addi.challenge.externalsystem.nationalregistryidentificationsystem.controller;

import com.addi.challenge.externalsystem.nationalregistryidentificationsystem.entity.Person;
import com.addi.challenge.externalsystem.nationalregistryidentificationsystem.exception.PersonMismatchException;
import com.addi.challenge.externalsystem.nationalregistryidentificationsystem.exception.PersonNotFoundException;
import com.addi.challenge.externalsystem.nationalregistryidentificationsystem.exception.PersonNotProvidedException;
import com.addi.challenge.externalsystem.nationalregistryidentificationsystem.service.NationalRegistryIdentificationSystemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NationalRegistryIdentificationSystemControllerTest {

    private static final String PERSON_NOT_FOUND_EXCEPTION_MESSAGE = "Person not found in National Registry";
    private static final String PERSON_ALREADY_EXISTS_IN_NATIONAL_REGISTRY_EXCEPTION_MESSAGE = "Person already exists in National Registry";
    private static final String PERSON_NOT_PROVIDED_EXCEPTION_MESSAGE = "Person not provided to be saved.";

    @Mock
    private NationalRegistryIdentificationSystemService nationalRegistryIdentificationSystemService;

    @InjectMocks
    private NationalRegistryIdentificationSystemController nationalRegistryIdentificationSystemController;

    @Test
    public void shouldReturnANotEmptyListWhenFindAllIsCalledAndThereIsAtLeastOneItemInTheDatabase() {
        when(nationalRegistryIdentificationSystemService.findAll()).thenReturn(createNotEmptyPersonMockList());

        ResponseEntity<List<Person>> actualPerson = this.nationalRegistryIdentificationSystemController.findAll();

        assertThat(actualPerson).isNotNull();

        assertThat(actualPerson.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturnAnEmptyListWhenFindAllIsCalledAndThereIsNoItemInDatabase() {
        when(nationalRegistryIdentificationSystemService.findAll()).thenReturn(createEmptyPersonMockList());

        ResponseEntity<List<Person>> actualPerson = this.nationalRegistryIdentificationSystemController.findAll();

        assertThat(actualPerson).isNotNull();
        assertThat(actualPerson.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void shouldReturnNotNullWhenFindByIdIsCalled() {
        Person expectedPerson = createPersonMock();

        when(nationalRegistryIdentificationSystemService.findByNationalIdentificationNumber(any())).thenReturn(expectedPerson);

        ResponseEntity<Person> actualPerson = this.nationalRegistryIdentificationSystemController.findByNationalIdentificationNumber(expectedPerson.getNationalIdentificationNumber());

        assertThat(actualPerson).isNotNull();
        assertThat(actualPerson.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualPerson.getBody()).isEqualTo(expectedPerson);
    }

    @Test
    public void shouldDeleteAnExistingPersonFromTheDatabaseWhenDeleteByIdIsCalled() throws PersonNotFoundException {
        Person expectedPerson = createPersonMock();

        doNothing().when(nationalRegistryIdentificationSystemService).deleteByNationalIdentificationNumber(any());

        this.nationalRegistryIdentificationSystemController.deleteByNationalIdentificationNumber(expectedPerson.getNationalIdentificationNumber());

        verify(nationalRegistryIdentificationSystemService, atLeast(1)).deleteByNationalIdentificationNumber(any());
    }

    @Test
    public void shouldAddANewPersonToTheDatabaseWhenSaveMethodIsCalled() throws PersonNotProvidedException, PersonMismatchException {
        Person expectedPerson = createPersonMock();

        when(nationalRegistryIdentificationSystemService.save(any())).thenReturn(expectedPerson);

        ResponseEntity<Person> actualPerson = this.nationalRegistryIdentificationSystemController.save(expectedPerson);

        assertThat(actualPerson).isNotNull();
        assertThat(actualPerson.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        verify(nationalRegistryIdentificationSystemService, atLeast(1)).save(any());
    }


    @Test
    public void shouldThrowPersonNotFoundExceptionWhenTriedToDeleteAPersonThatDoesNotExist() throws PersonNotFoundException {
        Person expectedPerson = createPersonMock();

        doThrow(new PersonNotFoundException(PERSON_NOT_FOUND_EXCEPTION_MESSAGE)).when(nationalRegistryIdentificationSystemService).deleteByNationalIdentificationNumber(any());

        Throwable exception = assertThrows(PersonNotFoundException.class,
                () -> this.nationalRegistryIdentificationSystemService.deleteByNationalIdentificationNumber(expectedPerson.getNationalIdentificationNumber()));

        assertThat(PERSON_NOT_FOUND_EXCEPTION_MESSAGE).isEqualTo(exception.getMessage());

    }

    @Test
    public void shouldThrowPersonMismatchExceptionWhenTriedToSaveAnAlreadyExistingPerson() throws PersonNotProvidedException, PersonMismatchException {
        Person expectedPerson = createPersonMock();

        when(nationalRegistryIdentificationSystemService.save(any())).thenThrow(new PersonMismatchException(PERSON_ALREADY_EXISTS_IN_NATIONAL_REGISTRY_EXCEPTION_MESSAGE));

        Throwable exception = assertThrows(PersonMismatchException.class,
                () -> this.nationalRegistryIdentificationSystemService.save(expectedPerson));

        assertThat(PERSON_ALREADY_EXISTS_IN_NATIONAL_REGISTRY_EXCEPTION_MESSAGE).isEqualTo(exception.getMessage());
    }

    @Test
    public void shouldThrowPersonNotProvidedExceptionWhenTriedToSaveWithoutPassingAnyElementInTheBody() throws PersonNotProvidedException, PersonMismatchException {
        Person expectedPerson = createPersonMock();

        when(nationalRegistryIdentificationSystemService.save(any())).thenThrow(new PersonNotProvidedException(PERSON_NOT_PROVIDED_EXCEPTION_MESSAGE));

        Throwable exception = assertThrows(PersonNotProvidedException.class,
                () -> this.nationalRegistryIdentificationSystemService.save(expectedPerson));

        assertThat(PERSON_NOT_PROVIDED_EXCEPTION_MESSAGE).isEqualTo(exception.getMessage());
    }

    private List<Person> createNotEmptyPersonMockList() {
        return Arrays.asList(createPersonMock(), createPersonMock());
    }

    private List<Person> createEmptyPersonMockList() {
        return Collections.emptyList();
    }

    private Person createPersonMock() {
        return Person.builder().id(1L)
                .birthDate("15/02/2001")
                .lastName("Guedes")
                .firstName("Claudia")
                .nationalIdentificationNumber("90001")
                .email("claudiaguedes@gmail.com")
                .build();

    }
}