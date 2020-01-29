package br.com.crud.service.convert;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.crud.domain.model.Person;
import br.com.crud.domain.repository.PersonRepository;
import br.com.crud.dto.PersonDataTransferObject;
import br.com.crud.service.exception.PersonException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PersonConvertTest {

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void shouldConvertDataTransferObjectToEntity() {
        PersonDataTransferObject personDTO = new PersonDataTransferObject(
                "Murilo", "Alves", LocalDate.parse("1995-01-21").toString());

        Person person = PersonConvert.convert(personDTO);

        assertEquals("Murilo", person.getFirstName());
        assertEquals("Alves", person.getLastName());
        assertEquals("1995-01-21", person.getBirthDate().toString());
    }

    @Test
    public void shouldConvertOptionalPersonToPerson() throws PersonException {
        Person person = persistPerson();

        Optional<Person> optionalPerson = personRepository
                .findById(person.getUuid());

        Person personConverted = PersonConvert.convertOptional(optionalPerson);

        assertEquals("Murilo", personConverted.getFirstName());
        assertEquals("Alves", personConverted.getLastName());
        assertEquals("1995-01-21", personConverted.getBirthDate().toString());
        assertEquals(person.getUuid(), personConverted.getUuid());

        personRepository.deleteAll();

    }

    @Test
    public void shouldConvertPersonEntityToPersonDataTransferObject() {
        Person person = persistPerson();

        PersonDataTransferObject personDataTransferObject = PersonConvert
                .convertToPatternDTO(person);

        assertEquals("Murilo", personDataTransferObject.getFirstName());
        assertEquals("Alves", personDataTransferObject.getLastName());
        assertEquals("1995-01-21",
                personDataTransferObject.getBirthDate().toString());

        personRepository.deleteAll();
    }

    private Person persistPerson() {
        PersonDataTransferObject personDTO = new PersonDataTransferObject(
                "Murilo", "Alves", LocalDate.parse("1995-01-21").toString());

        Person person = PersonConvert.convert(personDTO);
        return personRepository.saveAndFlush(person);
    }

}
