package cz.itnetwork.service;

import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.dto.PersonDTO;
import cz.itnetwork.dto.statistics.PersonStatistics;
import cz.itnetwork.entity.PersonEntity;

import java.util.List;

public interface PersonService {

    /**
     * Creates a new person
     *
     * @param personDTO Person to create
     * @return newly created person
     */
    PersonDTO addPerson(PersonDTO personDTO);

    /**
     * <p>Sets hidden flag to true for the person with the matching [id]</p>
     * <p>In case a person with the passed [id] isn't found, the method <b>silently fails</b></p>
     *
     * @param id Person to delete
     */
    void removePerson(long id);

    /**
     * Fetches all non-hidden persons
     *
     * @return List of all non-hidden persons
     */
    List<PersonDTO> getAll();

    PersonDTO getPerson(long personId);

    PersonDTO updatePerson(long personId, PersonDTO personDTO);

    PersonEntity getPersonByIdentificationNumber(String identificationNumber);

    List<PersonStatistics> getPersonsStatistics();

    List<InvoiceDTO> getAllInvoicesBySeller(String identificationNumber);

    List<InvoiceDTO> getAllInvoicesByBuyer(String identificationNumber);



}
