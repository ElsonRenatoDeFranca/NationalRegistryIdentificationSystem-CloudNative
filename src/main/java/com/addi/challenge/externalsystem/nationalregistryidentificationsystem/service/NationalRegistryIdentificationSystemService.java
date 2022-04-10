package com.addi.challenge.externalsystem.nationalregistryidentificationsystem.service;

import com.addi.challenge.externalsystem.nationalregistryidentificationsystem.entity.Person;
import com.addi.challenge.externalsystem.nationalregistryidentificationsystem.exception.PersonMismatchException;
import com.addi.challenge.externalsystem.nationalregistryidentificationsystem.exception.PersonNotFoundException;
import com.addi.challenge.externalsystem.nationalregistryidentificationsystem.exception.PersonNotProvidedException;

import java.util.List;

public interface NationalRegistryIdentificationSystemService {
    List<Person> findAll();

    Person save(Person person) throws PersonMismatchException, PersonNotProvidedException;

    void deleteByNationalIdentificationNumber(String nationalIdentificationNumber) throws PersonNotFoundException;

    Person findByNationalIdentificationNumber(String nationalIdentificationNumber);

}
