package br.com.crud.service;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.crud.domain.model.Person;
import br.com.crud.domain.repository.PersonRepository;
import br.com.crud.dto.PersonDataTransferObject;
import br.com.crud.service.convert.PersonConvert;

@SpringBootTest
@RunWith(SpringRunner.class)
public class PersonServiceTest {

  @Autowired
  private PersonService personService;

  @Autowired
  private PersonRepository personRepository;

  @Test
  public void shouldFindAll() {
    assertEquals(0, personRepository.findAll().size());

    persistPerson();
    persistPerson();

    assertEquals(2, personRepository.findAll().size());
    assertEquals(2, personService.findAll().size());

    personRepository.deleteAll();
  }

  @Test
  public void shouldDelete() {
    assertEquals(0, personRepository.findAll().size());

    Person person = persistPerson();

    assertEquals(1, personRepository.findAll().size());

    personService.delete(person.getUuid().toString());

    assertEquals(0, personRepository.findAll().size());

    personRepository.deleteAll();
  }

  @Test
  public void shouldUpdatePersonByPersonDTOAndUUID() {
    Person person = persistPerson();
    person.setFullName("Murilo Batista");

    PersonDataTransferObject personDTO = PersonConvert.convertToPatternDTO(person);

    personService.update(person.getUuid().toString(), personDTO);

    Person personUpdate = personRepository.findById(person.getUuid()).get();

    assertEquals("Murilo Batista", personUpdate.getFullName());

    personRepository.deleteAll();
  }

  @Test
  public void shouldFindPersonByUUID() {
    Person person = persistPerson();

    PersonDataTransferObject personDTO = personService.find(person.getUuid().toString());

    assertEquals(person.getBirthDate().toString(), personDTO.getBirthDate());
    assertEquals(person.getFullName(), personDTO.getFullName());

    personRepository.deleteAll();
  }

  @Test
  public void shouldSavePerson() {
    assertEquals(0, personRepository.findAll().size());

    PersonDataTransferObject personDataTransferObject = createObjectPersonDTO();
    personService.save(personDataTransferObject);

    assertEquals(1, personRepository.findAll().size());

    Person person = personRepository.findAll().get(0);

    assertEquals(personDataTransferObject.getFullName(), person.getFullName());
    assertEquals(personDataTransferObject.getBirthDate(), person.getBirthDate().toString());

    personRepository.deleteAll();
  }

  private PersonDataTransferObject createObjectPersonDTO() {
    return new PersonDataTransferObject("Murilo Alves", LocalDate.parse("2019-01-21").toString(),
        "88822");
  }

  private Person persistPerson() {
    PersonDataTransferObject personDTO = new PersonDataTransferObject("Murilo Alves",
        LocalDate.parse("1995-01-21").toString(), "88822");

    Person person = PersonConvert.convert(personDTO);
    return personRepository.saveAndFlush(person);
  }

}
