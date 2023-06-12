package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


import it.uniroma3.siw.model.Politician;
import it.uniroma3.siw.model.Country;


public interface PoliticianRepository extends CrudRepository<Politician, Long> 
{
	public List<Politician> findByName(String name);
	
	public boolean existsByNameAndSurname(String name, String surname);
	
}