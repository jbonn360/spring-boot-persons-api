package com.example.demo.api;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Person;
import com.example.demo.model.PersonWrapper;
import com.example.demo.service.PersonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RequestMapping("api/v1/person")
@RestController
public class PersonController {
	private final PersonService personService;

	@Autowired
	public PersonController(PersonService personService) {
		this.personService = personService;
	}

	@PostMapping
	public void addPerson(@Valid @NonNull @RequestBody Person person) {
		personService.addPerson(person);
	}

	/*
	 * @PostMapping public void addPersons(@RequestBody @Valid @NonNull String
	 * jsonString) { final ObjectMapper mapper = new ObjectMapper(); JsonNode
	 * jsonObject = null;
	 * 
	 * try { jsonObject = mapper.readTree(jsonString); } catch (IOException e) { //
	 * TODO Auto-generated catch block e.printStackTrace(); }
	 * 
	 * if (jsonObject.isArray()) { for (final JsonNode personJson : jsonObject) {
	 * //personService.addPerson(jsonNodeToPerson(personJson, mapper));
	 * addPerson(jsonNodeToPerson(personJson, mapper)); } } else {
	 * //personService.addPerson(jsonNodeToPerson(jsonObject, mapper));
	 * addPerson(jsonNodeToPerson(jsonObject, mapper)); } }
	 */

	private Person jsonNodeToPerson(JsonNode personJson, ObjectMapper mapper) {
		Person person = null;
		try {
			person = mapper.treeToValue(personJson, Person.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return person;
	}

	@GetMapping
	public List<Person> getAllPeople() {
		return personService.getAllPeople();
	}

	@GetMapping(path = "{id}")
	public Person getPersonById(@PathVariable("id") UUID id) {
		return personService.getPersonById(id).orElse(null);
	}

	@DeleteMapping(path = "{id}")
	public void deletePersonById(@PathVariable("id") UUID id) {
		personService.deletePerson(id);
	}

	@PutMapping(path = "{id}")
	public void updatePerson(@PathVariable("id") UUID id, @Valid @NonNull @RequestBody Person personToUpdate) {
		personService.updatePerson(id, personToUpdate);
	}
}
