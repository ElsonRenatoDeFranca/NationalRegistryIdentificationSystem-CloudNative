package com.addi.challenge.externalsystem.nationalregistryidentificationsystem.service;

import com.addi.challenge.externalsystem.nationalregistryidentificationsystem.entity.Person;
import com.addi.challenge.externalsystem.nationalregistryidentificationsystem.exception.PersonMismatchException;
import com.addi.challenge.externalsystem.nationalregistryidentificationsystem.exception.PersonNotFoundException;
import com.addi.challenge.externalsystem.nationalregistryidentificationsystem.exception.PersonNotProvidedException;
import com.addi.challenge.externalsystem.nationalregistryidentificationsystem.repository.NationalRegistryIdentificationSystemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NationalRegistryIdentificationSystemServiceImplTest {

    @Mock
    private NationalRegistryIdentificationSystemRepository repository;

    @InjectMocks
    private NationalRegistryIdentificationSystemServiceImpl nationalRegistryIdentificationSystemService;

    @Test
    public void shouldReturnANotEmptyListWhenFindAllIsCalledAndThereIsAtLeastOneItemInTheDatabase() {
        when(repository.findAll()).thenReturn(createNotEmptyPersonMockList());
        List<Person> actualPerson = this.nationalRegistryIdentificationSystemService.findAll();

        assertThat(actualPerson).isNotNull();
        assertThat(actualPerson.isEmpty()).isFalse();
    }

    @Test
    public void shouldReturnAnEmptyListWhenFindAllIsCalledAndThereIsNoItemInDatabase() {
        when(repository.findAll()).thenReturn(createEmptyPersonMockList());
        List<Person> actualPerson = this.nationalRegistryIdentificationSystemService.findAll();

        assertThat(actualPerson).isNotNull();
        assertThat(actualPerson.isEmpty()).isTrue();
    }

    @Test
    public void shouldReturnNotNullWhenFindByIdIsCalled() {
        Person expectedPersonMock = createPersonMock();

        when(repository.findByNationalIdentificationNumber(any())).thenReturn(expectedPersonMock);

        Person actualPerson = this.nationalRegistryIdentificationSystemService.findByNationalIdentificationNumber(expectedPersonMock.getNationalIdentificationNumber());

        assertThat(actualPerson).isNotNull();
        assertThat(actualPerson).isEqualTo(expectedPersonMock);
    }

    @Test
    public void shouldDeleteAnExistingPersonFromTheDatabaseWhenDeleteByIdIsCalled() throws PersonNotFoundException {
        Person expectedPerson = createPersonMock();

        doNothing().when(repository).deleteByNationalIdentificationNumber(any());
        when(repository.findByNationalIdentificationNumber(any())).thenReturn(expectedPerson);

        this.nationalRegistryIdentificationSystemService.deleteByNationalIdentificationNumber(expectedPerson.getNationalIdentificationNumber());

        verify(repository, atLeast(1)).deleteByNationalIdentificationNumber(any());

    }


    @Test
    public void shouldAddANewPersonToTheDatabaseWhenSaveIsCalled() throws PersonNotProvidedException, PersonMismatchException {
        Person expectedPerson = createPersonMock();

        when(repository.save(any())).thenReturn(expectedPerson);

        Person actualPerson = this.nationalRegistryIdentificationSystemService.save(expectedPerson);

        assertThat(actualPerson).isNotNull();
        assertThat(actualPerson).isEqualTo(expectedPerson);
        verify(repository, atLeast(1)).save(any());
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