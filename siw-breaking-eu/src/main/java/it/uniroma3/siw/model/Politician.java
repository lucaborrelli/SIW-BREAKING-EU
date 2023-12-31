package it.uniroma3.siw.model;


import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.format.annotation.DateTimeFormat;



@Entity
public class Politician 
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	
	private Long id;
	private String name;
	private String surname;
	
	
	@Basic(fetch = FetchType.LAZY)
	@Lob
	@Column(name = "image")
	private byte[] image;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfBirth; //giorno, mese, anno
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dateOfDeath; /*da inserire controllo sull'inserimento*/
	
	@OneToOne
    @JoinColumn(name = "country_id")
	private Country country;
	
	
	
	
	
	public Long getId()
	{
		return this.id;
	}
	
	public void setId(Long id)
	{
		this.id = id;
	}
	
	public String getSurname() 
	{
		return this.surname;
	}
	
	public void setSurname(String surname)
	{
		this.surname = surname;
	}
	
	public LocalDate getDateOfBirth() 
	{
		return this.dateOfBirth;
	}
	
	public void setDateOfBirth(LocalDate dateOfBirth)
	{
		this.dateOfBirth = dateOfBirth;
	}
	public String getName() 
	{
		return this.name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}
	@Override
	public int hashCode() {
		return Objects.hash(name, surname);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Politician other = (Politician) obj;
		return Objects.equals(name, other.name) && Objects.equals(surname, other.surname);
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public LocalDate getDateOfDeath() {
		return dateOfDeath;
	}

	public void setDateOfDeath(LocalDate dateOfDeath) {
		this.dateOfDeath = dateOfDeath;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}
	


}