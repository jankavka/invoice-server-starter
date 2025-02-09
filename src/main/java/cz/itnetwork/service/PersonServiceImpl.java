/*  _____ _______         _                      _
 * |_   _|__   __|       | |                    | |
 *   | |    | |_ __   ___| |___      _____  _ __| | __  ___ ____
 *   | |    | | '_ \ / _ \ __\ \ /\ / / _ \| '__| |/ / / __|_  /
 *  _| |_   | | | | |  __/ |_ \ V  V / (_) | |  |   < | (__ / /
 * |_____|  |_|_| |_|\___|\__| \_/\_/ \___/|_|  |_|\_(_)___/___|
 *                                _
 *              ___ ___ ___ _____|_|_ _ _____
 *             | . |  _| -_|     | | | |     |  LICENCE
 *             |  _|_| |___|_|_|_|_|___|_|_|_|
 *             |_|
 *
 *   PROGRAMOVÁNÍ  <>  DESIGN  <>  PRÁCE/PODNIKÁNÍ  <>  HW A SW
 *
 * Tento zdrojový kód je součástí výukových seriálů na
 * IT sociální síti WWW.ITNETWORK.CZ
 *
 * Kód spadá pod licenci prémiového obsahu a vznikl díky podpoře
 * našich členů. Je určen pouze pro osobní užití a nesmí být šířen.
 * Více informací na http://www.itnetwork.cz/licence
 */
package cz.itnetwork.service;

import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.dto.PersonDTO;
import cz.itnetwork.dto.mapper.InvoiceMapper;
import cz.itnetwork.dto.mapper.PersonMapper;
import cz.itnetwork.dto.statistics.PersonStatistics;
import cz.itnetwork.entity.InvoiceEntity;
import cz.itnetwork.entity.PersonEntity;
import cz.itnetwork.entity.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl implements PersonService {


    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private InvoiceMapper invoiceMapper;

    public PersonDTO addPerson(PersonDTO personDTO) {
        PersonEntity entity = personMapper.toEntity(personDTO);
        entity = personRepository.save(entity);

        return personMapper.toDTO(entity);
    }

    @Override
    public void removePerson(long personId) {
        try {
            PersonEntity person = fetchPersonById(personId);
            person.setHidden(true);

            personRepository.save(person);
        } catch (NotFoundException ignored) {
            // The contract in the interface states, that no exception is thrown, if the entity is not found.
        }
    }

    @Override
    public List<PersonDTO> getAll() {
        return personRepository.findByHidden(false)
                .stream()
                .map(i -> personMapper.toDTO(i))
                .collect(Collectors.toList());
    }

    @Override
    public PersonDTO getPerson(long personId) {
        try {
            return personMapper.toDTO(fetchPersonById(personId));
        }catch(NotFoundException e){
            throw new EntityNotFoundException();
        }
    }

    @Override
    public PersonDTO updatePerson(long personId, PersonDTO personDTO) {
        PersonEntity fetchedEntity = fetchPersonById(personId);
        fetchedEntity.setHidden(true);
        PersonEntity newEntity = personMapper.toEntity(personDTO);
        PersonEntity savedEntity = personRepository.save(newEntity);
        PersonDTO returnDTO = personMapper.toDTO(savedEntity);
        returnDTO.setId(personId);

        return personMapper.toDTO(savedEntity);
    }


    // region: Private methods

    /**
     * <p>Attempts to fetch a person.</p>
     * <p>In case a person with the passed [id] doesn't exist a [{@link org.webjars.NotFoundException}] is thrown.</p>
     *
     * @param id Person to fetch
     * @return Fetched entity
     * @throws org.webjars.NotFoundException In case a person with the passed [id] isn't found
     */
    private PersonEntity fetchPersonById(long id) {
        try {
            return personRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Person with id " + id + " wasn't found in the database."));
        }catch(NotFoundException e) {
            throw new EntityNotFoundException();
        }
    }
    // endregion

    public PersonEntity getPersonByIdentificationNumber(String identificationNumber) {
        Long personId = 0L;
        for (PersonDTO personDTO : getAll()) {
            if (personDTO.getIdentificationNumber().equals(identificationNumber)) {
                personId = personDTO.getId();
            }
        }
        return personRepository.getReferenceById(personId);

    }

    @Override
    public List<PersonStatistics> getPersonsStatistics() {
        return personRepository.getRevenueByPerson().stream()
                .map(a -> new PersonStatistics((Long) a[0], (String) a[1], (Long) a[2]))
                .collect(Collectors.toList());

    }

    @Override
    public List<InvoiceDTO> getInvoices(String identificationNumber, boolean isSales) {
        List<InvoiceEntity> invoices = isSales ?
                getPersonByIdentificationNumber(identificationNumber).getSales()
                :
                getPersonByIdentificationNumber(identificationNumber).getPurchases();
        return invoices.stream()
                .map(invoiceMapper::toDTO)
                .collect(Collectors.toList());
    }

}
