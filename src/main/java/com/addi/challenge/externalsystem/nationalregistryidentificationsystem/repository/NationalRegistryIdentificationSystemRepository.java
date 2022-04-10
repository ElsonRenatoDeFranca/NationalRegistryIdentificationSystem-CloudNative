package com.addi.challenge.externalsystem.nationalregistryidentificationsystem.repository;

import com.addi.challenge.externalsystem.nationalregistryidentificationsystem.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NationalRegistryIdentificationSystemRepository extends JpaRepository<Person, Long> {
    Person findByNationalIdentificationNumber(String nationalIdentificationNumber);
    void deleteByNationalIdentificationNumber(String nationalIdentificationNumber);
}
