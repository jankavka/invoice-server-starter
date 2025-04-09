package cz.itnetwork.service;

import cz.itnetwork.constant.Countries;
import cz.itnetwork.dto.PersonDTO;
import cz.itnetwork.dto.mapper.PersonMapper;
import cz.itnetwork.entity.PersonEntity;
import cz.itnetwork.entity.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class PersonServiceImplTest {

    @Autowired
    PersonService personService;

    @Autowired
    PersonMapper personMapper;

    @Autowired
    PersonRepository personRepository;


    @Test
    void addPerson() {
        PersonDTO person = getPersonDTO();
        PersonDTO result = personService.addPerson(person);
        log.info("Person tested with id: {} ", result.getId());

        assertNotNull(person);
        assertEquals(person.getName(), result.getName());

        PersonEntity savedEntity = personRepository.findById(result.getId()).orElse(null);
        assertNotNull(savedEntity);

        assertEquals(person.getName(), savedEntity.getName());


    }

    private static PersonDTO getPersonDTO() {
        PersonDTO person = new PersonDTO();
        person.setName("Jan");
        person.setCity("Ostrava");
        person.setIdentificationNumber("975469");
        person.setCountry(Countries.CZECHIA);
        person.setAccountNumber("1234");
        person.setIban("90987557");
        person.setBankCode("0800");
        person.setMail("prdel");
        person.setNote("Note");
        person.setStreet("ulice");
        person.setTaxNumber("21");
        person.setZip("72000");
        person.setTelephone("87657965");
        return person;
    }
}