package it.uniroma3.siw.model;

import java.awt.Image;
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
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.Hibernate;


@Entity
public class Country
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)

	private Long id;
	@NotNull
	@NotBlank 
	@Size(max = 255)
	private String name;

	@NotNull
	private Integer population;

	
	private String currency;
	
	private Integer pil;
	
	private String capital;
	
	private String formOfGovernment;
	
	@OneToOne(mappedBy = "country")
	private Politician currentBossOfCountry;
	
	
	@Lob
	@Column(name = "image")
	private byte[] image;


	@ManyToMany(mappedBy="countries")
	private List<News> newses;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	

	@Override
	public int hashCode() 
	{
		return Objects.hash(name);
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
		Country other = (Country) obj;
		return Objects.equals(name, other.name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPopulation() {
		return population;
	}

	public void setPopulation(Integer population) {
		this.population = population;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Integer getPil() {
		return pil;
	}

	public void setPil(Integer pil) {
		this.pil = pil;
	}

	public String getCapital() {
		return capital;
	}

	public void setCapital(String capital) {
		this.capital = capital;
	}

	public String getFormOfGovernment() {
		return formOfGovernment;
	}

	public void setFormOfGovernment(String formOfGovernment) {
		this.formOfGovernment = formOfGovernment;
	}


	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public List<News> getNewses() {
		return newses;
	}

	public void setNewses(List<News> newses) {
		this.newses = newses;
	}

	public Politician getCurrentBossOfCountry() {
		return currentBossOfCountry;
	}

	public void setCurrentBossOfCountry(Politician currentBossOfCountry) {
		this.currentBossOfCountry = currentBossOfCountry;
	}

}