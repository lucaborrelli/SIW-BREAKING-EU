package it.uniroma3.siw.model;


import java.time.LocalDate;
import java.util.List;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import it.uniroma3.siw.model.Comment;
@EnableJpaRepositories
@Entity
@Table(name = "users")

public class User 
{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	
	private Long id;
	
	private String name;
	private String surname;
	private String username;
	
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfBirth; //giorno, mese, anno
	
	@OneToMany(mappedBy="writer")
	private List<Comment> myComments;
	
	public Long getId() 
	{
		return id;
	}

	public void setId(Long id) 
	{
		this.id = id;
	}
	
	
	public String getName() 
	{
		return name;
	}
	
	public void setName(String name) 
	{
		this.name = name;
	}
	
	public String getSurname() 
	{
		return surname;
	}
	
	public void setSurname(String surname) 
	{
		this.surname = surname;
	}
	
	public LocalDate getDateOfBirth() 
	{
		return dateOfBirth;
	}
	
	public void setDateOfBirth(LocalDate dateOfBirth) 
	{
		this.dateOfBirth = dateOfBirth;
	}
	
	
	
	public List<Comment> getMyComments() 
	{
		return myComments;
	}
	
	public void setMyComments(List<Comment> myComments) 
	{
		this.myComments = myComments;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}