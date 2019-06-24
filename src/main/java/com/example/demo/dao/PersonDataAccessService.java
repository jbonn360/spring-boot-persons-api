package com.example.demo.dao;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.example.demo.model.Person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("postgres")
public class PersonDataAccessService implements PersonDao {

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public PersonDataAccessService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public int insertPerson(UUID id, Person person) {
		final String sql = "INSERT INTO person (id, name) VALUES (?, ?)";
		return jdbcTemplate.update(sql, id, person.getName());
	}

	@Override
	public List<Person> selectAllPeople() {
		final String sql = "SELECT id, name FROM person";
		List<Person> people = jdbcTemplate.query(sql, (resultSet, i) -> {
			UUID id = UUID.fromString(resultSet.getString("id"));
			String name = resultSet.getString("name");
			return new Person(id, name);
		});
		return people;
	}

	@Override
	public Optional<Person> selectPersonById(UUID id) {
		final String sql = "SELECT id, name FROM person WHERE id = ?";
		Person person = jdbcTemplate.queryForObject(sql, new Object[] { id }, (resultSet, i) -> {
			UUID personId = UUID.fromString(resultSet.getString("id"));
			String name = resultSet.getString("name");
			return new Person(personId, name);
		});
		return Optional.ofNullable(person);
	}

	@Override
	public int deletePersonById(UUID id) {
		final String sql = "DELETE FROM person WHERE id = ?";
		return jdbcTemplate.update(sql, id);
	}

	@Override
	public int updatePersonById(UUID id, Person person) {
		Optional<Person> toUpdate = selectPersonById(id);
		if(toUpdate.isPresent()) {
			final String sql = "UPDATE person SET name = ? WHERE id = ?;";
			return jdbcTemplate.update(sql, person.getName(), id);
		}else {
			return -1;
		}
	}

}
