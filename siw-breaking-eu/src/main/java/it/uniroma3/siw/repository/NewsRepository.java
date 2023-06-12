package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.model.News;
import it.uniroma3.siw.model.Country;
import it.uniroma3.siw.model.Comment;
import it.uniroma3.siw.model.User;

@Repository
public interface NewsRepository extends CrudRepository<News, Long> 
{

	
	public List<News> findByWriter(String Writer);
	public List<News> findByTitle(String Title);
	public List<News> findByTitleAndWriter(String title, String writer);
	public List<News> findAllByOrderByDateOfPubblication(); 

	public boolean existsByTitle(String title);
	
	
	
}