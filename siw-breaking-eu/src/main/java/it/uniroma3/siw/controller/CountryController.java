package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.orm.jpa.JpaSystemException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.hibernate.id.IdentifierGenerationException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import it.uniroma3.siw.controller.validator.CountryUpdateValidator;
import it.uniroma3.siw.controller.validator.CountryValidator;
import it.uniroma3.siw.model.Politician;
import it.uniroma3.siw.model.Country;
import it.uniroma3.siw.model.News;
import it.uniroma3.siw.repository.PoliticianRepository;
import it.uniroma3.siw.repository.CountryRepository;
import it.uniroma3.siw.repository.NewsRepository;

@Controller
public class CountryController 
{
	@Autowired CountryRepository countryRepository;
	@Autowired CountryValidator countryValidator;
	@Autowired CountryUpdateValidator countryUpdateValidator;
	@Autowired NewsRepository newsRepository;
	@Autowired PoliticianRepository politicianRepository;
	
	/*Nota bene "@InitBinder è essenziale per effettuare una conversione dell'oggetto "MultipartFile" in un array di byte[]
	 * in quanto le immagini sono conservate all'interno del database come una sequenza degli stessi. Inserendo @InitBinder
	 * all'interno del controller permette al "binding" di non evidenziare errori nella conversione e quindi procede con il
	 * salvataggio del movie all'interno del database. */ 
	 
	@InitBinder
	public void initBinder(WebDataBinder binder) 
	{
	    binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
	}

	
	@GetMapping("/admin/formNewCountry")
	public String formNewCountry(Model model) 
	{
		model.addAttribute("country", new Country());
		return "admin/formNewCountry";	
	}
	
	@GetMapping("/public/formSearchCountries")
	public String formSearchCountries()
	{
		return "public/formSearchCountries";
	}
	
	@GetMapping("/admin/formUpdateCountry/{id}")
	public String getUpdateCountry(@PathVariable("id") Long id, Model model) 
	{
		
		model.addAttribute("country", this.countryRepository.findById(id).get());
		
        
		return "admin/formUpdateCountry";
	}
	
	@PostMapping("/admin/country/edit/{id}")
	public String editCountry(@Valid @ModelAttribute("country") Country country, BindingResult bindingResult,@PathVariable("id")Long id,Model model,
			@RequestParam("name")String name,@RequestParam("currency")String currency,@RequestParam("capital")String capital,
			@RequestParam("formOfGovernment")String formOfGovernment,
			@RequestParam("pil")Integer pil,@RequestParam("population")Integer population,
			@RequestParam("image") MultipartFile imageProfileFile) throws IOException
	{
				
		
		
		this.countryUpdateValidator.validate(country, bindingResult);
		
		if (!bindingResult.hasErrors()) 
		{
		    
		    if (currency != null && !currency.isEmpty()) 
		    {
		        country.setCurrency(currency);
		    }
		    
		    if (capital != null && !capital.isEmpty()) 
		    {
		        country.setCapital(capital);
		    }
		    
		    if (formOfGovernment != null && !formOfGovernment.isEmpty()) 
		    {
		        country.setFormOfGovernment(formOfGovernment);
		    }
		    
		    
		    if (pil != null) 
		    {
		        country.setPil(pil);
		    }
		    
		    if (population != null) 
		    {
		        country.setPopulation(population);
		    }
		    
		    if (imageProfileFile != null) 
		    {
		    	byte[] imageBytes = imageProfileFile.getBytes();
				
				
	    		country.setImage(imageBytes);
		    }
		    
		    this.countryRepository.save(country);
		    
		    model.addAttribute("country",country);
		    
		    return "public/country";
		}
			
			model.addAttribute("country",country);
			return "admin/formUpdateCountry";
	}
	
	
	@Transactional
	@GetMapping("/admin/deleteCountry/{id}")
	public String deleteCountry(@PathVariable("id") Long id, Model model) 
	{
	   Country country = this.countryRepository.findById(id).get();
	   Politician politician = null;
	   if(country.getCurrentBossOfCountry() != null)
	   {
		   politician = country.getCurrentBossOfCountry();
		   politician.setCountry(null);
		   this.politicianRepository.save(politician);
	   }
		  
	   
	   
		List<News> newsesOfCountry= country.getNewses(); 
		
		for(News news: newsesOfCountry)
		{
			news.getCountries().remove(country);
			this.newsRepository.save(news);
		}
		
		
		
        this.countryRepository.delete(country);
        
		model.addAttribute("countries", this.countryRepository.findAll());
		
		return "admin/manageCountries";
		
	}
	
	@GetMapping("/admin/manageCountries")
	public String manageCountry(Model model) 
	{

		model.addAttribute("countries", this.countryRepository.findAll());
		
		return "admin/manageCountries";
		
	}
	
	@PostMapping("/public/searchCountries")
	public String formSearchCountries(Model model, @RequestParam String name) 
	{
		model.addAttribute("countries", this.countryRepository.findByName(name));
		return "public/foundCountries.html";
	}
	

	
	
	@GetMapping("/public/countries/{id}")
	public String getCountry(@PathVariable("id") Long id, Model model) 
	{
		
		model.addAttribute("country", this.countryRepository.findById(id).get());
	
		
		return "public/country.html";
	}
	
	 @GetMapping("/public/countries")
	 public String showCountries(Model model) 
	 {
		 model.addAttribute("countries",this.countryRepository.findAll());
		 return "public/countries";
	 }
	
	
	@PostMapping("/admin/countries") 
	public String newCountry(@Valid @ModelAttribute("country") Country country, BindingResult bindingResult, 
			Model model,@RequestParam("image") MultipartFile imageProfileFile)throws IOException 
	{
		
		this.countryValidator.validate(country, bindingResult);
		
		if (!bindingResult.hasErrors()) 
		{
			byte[] imageBytes = imageProfileFile.getBytes();
			
			
			country.setImage(imageBytes);
    		
    		this.countryRepository.save(country);
    		
			model.addAttribute("country", country);
			
			
			return "public/country"; 
		}
		else 
		{
			return "admin/formNewCountry";
		}
	}
	
	@GetMapping("/image/country/{id}")
	@ResponseBody
	public byte[] getImage(@PathVariable("id") Long id) {
	
	    Country country = countryRepository.findById(id).orElse(null);
	    
	    if (country != null && country.getImage() != null) {
	
	        return country.getImage();
	    }
	    return new byte[0];
	}
	
	
	
	@GetMapping("/admin/addPoliticianToCountry/{id}")
	public String addPoliticianToCountry(@PathVariable("id") Long id, Model model) { 
		
		Politician politician= this.countryRepository.findById(id).get().getCurrentBossOfCountry();
		
		
		List<Politician> politicians= (List<Politician>) this.politicianRepository.findAll();
		
		politicians.remove(politician);
		
		
		model.addAttribute("country",this.countryRepository.findById(id).get());
		model.addAttribute("politicians",politicians);
		
		return "admin/politicianToAdd";
	}
	
	@GetMapping("/admin/setPoliticianToCountry/{politicianid}/{countryid}")
	public String setPoliticianToCountry(@PathVariable("politicianid") Long politicianid,@PathVariable("countryid") Long countryid, Model model)
	{ 
		
		Politician politician = this.politicianRepository.findById(politicianid).get();
		Country country = this.countryRepository.findById(countryid).get();
		//setto country e politician
		politician.setCountry(country);
		country.setCurrentBossOfCountry(politician);
		this.politicianRepository.save(politician);
		this.countryRepository.save(country);
		
		List<Politician> politicians= (List<Politician>) this.politicianRepository.findAll();
		
		politicians.remove(politician);
		
		
		model.addAttribute("country",this.countryRepository.findById(countryid).get());
		model.addAttribute("politicians",politicians);
		
		return "admin/politicianToAdd";
	}
	
	
}