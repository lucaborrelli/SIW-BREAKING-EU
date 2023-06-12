package it.uniroma3.siw.repository;

import java.util.List;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.model.Country;
import it.uniroma3.siw.model.News;
import it.uniroma3.siw.model.Comment;
import it.uniroma3.siw.model.User;

@Repository
public interface CountryRepository extends CrudRepository<Country, Long> 
{
	
	public List<Country> findByName(String name);
	public  List<Country> getCountryByNewsesContains(News news);
	public  List<Country> getCountryByNewsesNotContains(News news);
	
	public boolean existsByName(String name);
	
	
	
}