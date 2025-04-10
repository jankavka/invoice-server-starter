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
package cz.itnetwork.controller;

import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.dto.PersonDTO;
import cz.itnetwork.dto.statistics.PersonStatistics;
import cz.itnetwork.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PersonController {

    @Autowired
    private PersonService personService;

    @PostMapping("/persons")
    public PersonDTO addPerson(@RequestBody @Valid PersonDTO personDTO) {
        return  personService.addPerson(personDTO);
    }

    @GetMapping("/persons")
    public List<PersonDTO> getAllPersons() {
        return personService.getAll();
    }

    @GetMapping("/persons/{personId}")
    public PersonDTO getPerson(@PathVariable long personId){
        return personService.getPerson(personId);
    }

    @DeleteMapping("/persons/{personId}")
    public HttpStatus deletePerson(@PathVariable Long personId) {
        personService.removePerson(personId);
        return HttpStatus.NO_CONTENT;
    }

    @PutMapping("/persons/{personId}")
    public PersonDTO editPerson(@PathVariable @Valid Long personId, @RequestBody PersonDTO personDTO){
        return personService.updatePerson(personId, personDTO);
    }

    @GetMapping("/persons/statistics")
    public List<PersonStatistics> showPersonsStatistics(){
        return personService.getPersonsStatistics();
    }

    @GetMapping("/identification/{identificationNumber}/sales")
    public List<InvoiceDTO> showAllInvoicesBySeller(@PathVariable String identificationNumber) {
        return personService.getInvoicesByPerson(identificationNumber,true);
    }

    @GetMapping("/identification/{identificationNumber}/purchases")
    public List<InvoiceDTO> showAllInvoicesByBuyer(@PathVariable String identificationNumber) {
        return personService.getInvoicesByPerson(identificationNumber, false);
    }
}

