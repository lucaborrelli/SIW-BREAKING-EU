package it.uniroma3.siw.model;

import java.awt.Image;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.Hibernate;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
public class News
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)

	private Long id;
	@NotNull
	@NotBlank 
	@Size(max = 255)
	private String title;

	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime dateOfPubblication;
	
	@NotNull
	private String text;
	
	private String writer;
	
	private String link;

	@Lob
	@Column(name = "image")
	private byte[] image;


	@ManyToMany(fetch = FetchType.LAZY)
	private Set<Country> countries;
	
	@OneToMany(mappedBy = "news")
	private List<Comment> newsComments;
	
	
	public Long getId() 
	{
		return this.id;
	}

	public void setId(Long id) 
	{
		this.id = id;
	}
	
	

	@Override
	public int hashCode() 
	{
		return Objects.hash(title);
	}

	@Override
	public boolean equals(Object obj) 
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		News other = (News) obj;
		return Objects.equals(title, other.title);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDateTime getDateOfPubblication() {
		return dateOfPubblication;
	}

	public void setDateOfPubblication(LocalDateTime dateOfPubblication) {
		this.dateOfPubblication = dateOfPubblication;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public Set<Country> getCountries() {
		return countries;
	}

	public void setCountries(Set<Country> countries) {
		this.countries = countries;
	}
	
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public List<Comment> getNewsComments() {
		return newsComments;
	}

	public void setNewsComments(List<Comment> newsComments) {
		this.newsComments = newsComments;
	}

}