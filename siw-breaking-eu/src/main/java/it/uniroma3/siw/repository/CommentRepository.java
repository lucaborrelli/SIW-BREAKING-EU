package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import it.uniroma3.siw.model.News;
import it.uniroma3.siw.model.Comment;
import it.uniroma3.siw.model.User;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long>
{
	public List<Comment> findByNews(News news);
	
	public List<Comment> findByWriter(User writer);
}
