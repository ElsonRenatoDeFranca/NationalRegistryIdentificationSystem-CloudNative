package com.addi.challenge.externalsystem.nationalregistryidentificationsystem.service;

import com.addi.challenge.externalsystem.nationalregistryidentificationsystem.entity.Person;
import com.addi.challenge.externalsystem.nationalregistryidentificationsystem.exception.PersonMismatchException;
import com.addi.challenge.externalsystem.nationalregistryidentificationsystem.exception.PersonNotFoundException;
import com.addi.challenge.externalsystem.nationalregistryidentificationsystem.exception.PersonNotProvidedException;
import com.addi.challenge.externalsystem.nationalregistryidentificationsystem.repository.NationalRegistryIdentificationSystemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NationalRegistryIdentificationSystemServiceImpl implements NationalRegistryIdentificationSystemService {

    private final NationalRegistryIdentificationSystemRepository repository;

    private static final String PERSON_NOT_FOUND_EXCEPTION_MESSAGE = "Person not found.";
    private static final String PERSON_ALREADY_EXISTS_IN_NATIONAL_REGISTRY_MESSAGE = "Person already exists in National Registry";
    private static final String PERSON_NOT_PROVIDED_EXCEPTION_MESSAGE = "Person not provided to be saved.";

    public NationalRegistryIdentificationSystemServiceImpl(NationalRegistryIdentificationSystemRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Person> findAll() {
        return repository.findAll();
    }

    @Override
    public Person findByNationalIdentificationNumber(String nationalIdentificationNumber) {
        return repository.findByNationalIdentificationNumber(nationalIdentificationNumber);
    }

    @Override
    public Person save(Person person) throws PersonMismatchException, PersonNotProvidedException {
        if (person != null) {
            if (personAlreadyExists(person.getNationalIdentificationNumber())) {
                throw new PersonMismatchException(PERSON_ALREADY_EXISTS_IN_NATIONAL_REGISTRY_MESSAGE);
            }
            return repository.save(person);
        }
        throw new PersonNotProvidedException(PERSON_NOT_PROVIDED_EXCEPTION_MESSAGE);
    }

    @Override
    public void deleteByNationalIdentificationNumber(String nationalIdentificationNumber) throws PersonNotFoundException {
        if (personAlreadyExists(nationalIdentificationNumber)) {
            repository.deleteByNationalIdentificationNumber(nationalIdentificationNumber);
        } else {
            throw new PersonNotFoundException(PERSON_NOT_FOUND_EXCEPTION_MESSAGE);
        }
    }

    private boolean personAlreadyExists(String nationalIdentificationNumber) {
        Person existingPerson = findByNationalIdentificationNumber(nationalIdentificationNumber);
        return existingPerson != null;
    }
}
