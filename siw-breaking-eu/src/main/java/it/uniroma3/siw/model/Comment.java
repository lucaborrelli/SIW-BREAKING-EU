package it.uniroma3.siw.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.format.annotation.DateTimeFormat;
@EnableJpaRepositories

@Entity
public class Comment 
{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	
	private Long id;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime date;
	
	
	private String text;
	
	
	
	@ManyToOne
	private News news; 
	
	
	@ManyToOne
	private User writer;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public News getNews() {
		return news;
	}

	public void setNews(News news) {
		this.news = news;
	}

	public User getWriter() {
		return writer;
	}

	public void setWriter(User writer) {
		this.writer = writer;
	}


	
	
	
}