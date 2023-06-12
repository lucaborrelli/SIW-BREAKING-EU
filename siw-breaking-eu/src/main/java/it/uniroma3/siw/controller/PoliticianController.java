package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.orm.jpa.JpaSystemException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

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

import it.uniroma3.siw.controller.validator.PoliticianUpdateValidator;
import it.uniroma3.siw.controller.validator.PoliticianValidator;
import it.uniroma3.siw.model.Politician;
import it.uniroma3.siw.model.News;
import it.uniroma3.siw.model.Country;
import it.uniroma3.siw.repository.PoliticianRepository;
import it.uniroma3.siw.repository.NewsRepository;
import it.uniroma3.siw.repository.CountryRepository;

@Controller
public class PoliticianController 
{
	@Autowired PoliticianRepository politicianRepository;
	@Autowired PoliticianValidator politicianValidator;
	@Autowired PoliticianUpdateValidator politicianUpdateValidator;
	@Autowired CountryRepository countryRepository;
	
	
	 
	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
	}

	
	@GetMapping("/admin/formNewPolitician")
	public String formNewPolitician(Model model) 
	{
		model.addAttribute("politician", new Politician());
		return "admin/formNewPolitician";	
	}
	
	@GetMapping("/public/formSearchPoliticians")
	public String formSearchPoliticians()
	{
		return "public/formSearchPoliticians";
	}
	
	@GetMapping("/admin/formUpdatePolitician/{id}")
	public String getUpdatePolitician(@PathVariable("id") Long id, Model model) 
	{
		
		model.addAttribute("politician", this.politicianRepository.findById(id).get());
		
        
		return "admin/formUpdatePolitician";
	}
	
	@PostMapping("/admin/politician/edit/{id}")
	public String editPolitician(@Valid @ModelAttribute("politician") Politician politician, BindingResult bindingResult,@PathVariable("id")Long id,Model model,
			@RequestParam("name")String name,@RequestParam("surname")String surname,
			@RequestParam(required=false) @DateTimeFormat(pattern="yyyy-MM-dd")LocalDate dateOfBirth, 
			@RequestParam(required=false) @DateTimeFormat(pattern="yyyy-MM-dd")LocalDate dateOfDeath,
			@RequestParam("image") MultipartFile imageProfileFile) throws IOException
	{
				
	
		
		this.politicianUpdateValidator.validate(politician, bindingResult);
		
		if (!bindingResult.hasErrors()) 
		{
		    
		    
		    if (dateOfBirth != null) 
		    {
		    	politician.setDateOfBirth(dateOfBirth);
		    }
		    
		    if (dateOfDeath != null) 
		    {
		    	politician.setDateOfDeath(dateOfDeath);
		    }
		    else
		    	politician.setDateOfDeath(null);
		    
		    if (imageProfileFile != null) 
		    {
		    	byte[] imageBytes = imageProfileFile.getBytes();
				
				
		    	politician.setImage(imageBytes);
		    }
		    
		    this.politicianRepository.save(politician);
		    
		    model.addAttribute("politician",politician);
		    
		    return "public/politician";
	}
		
		model.addAttribute("politician",politician);
		return "admin/formUpdatePolitician";
}
	
	
	
	@GetMapping("/admin/deletePolitician/{id}")
	public String deletePolitician(@PathVariable("id") Long id, Model model) 
	{
	   Politician politician = this.politicianRepository.findById(id).get();
		if(politician.getCountry() != null)
		{
			Country country= politician.getCountry();
			
			//setto a null la relazione dello stato al suo capo di stato
			country.setCurrentBossOfCountry(null);
			this.countryRepository.save(country);
		}
		
		this.politicianRepository.delete(politician);
        
		model.addAttribute("politicians", this.politicianRepository.findAll());
		
		return "admin/managePoliticians";
		
	}
	
	@GetMapping("/admin/managePoliticians")
	public String managePolitician(Model model) 
	{

		model.addAttribute("politicians", this.politicianRepository.findAll());
		
		return "admin/managePoliticians";
		
	}
	
	@PostMapping("/public/searchPoliticians")
	public String formSearchPoliticians(Model model, @RequestParam String name) 
	{
		model.addAttribute("politicians", this.politicianRepository.findByName(name));
		return "public/foundPoliticians";
	}
	

	
	
	@GetMapping("/public/politicians/{id}")
	public String getPolitician(@PathVariable("id") Long id, Model model) 
	{
		
		model.addAttribute("politician", this.politicianRepository.findById(id).get());
		
		
		return "public/politician";
	}
	
	 @GetMapping("/public/politicians")
	 public String showPoliticians(Model model) 
	 {
		 model.addAttribute("politicians",this.politicianRepository.findAll());
		 return "public/politicians";
	 }
	
	
	@PostMapping("/admin/politicians") 
	public String newPolitician(@Valid @ModelAttribute("politicians") Politician politician, BindingResult bindingResult, 
			Model model,@RequestParam("image") MultipartFile imageProfileFile)throws IOException 
	{
		
		this.politicianValidator.validate(politician, bindingResult);
		
		if (!bindingResult.hasErrors()) 
		{
			byte[] imageBytes = imageProfileFile.getBytes();
			
			
			politician.setImage(imageBytes);
    		
    		this.politicianRepository.save(politician);
    		
			model.addAttribute("politician", politician);
			
			
			return "public/politician"; 
		}
		else 
		{
			return "admin/formNewPolitician";
		}
	}
	
	@GetMapping("/image/politician/{id}")
	@ResponseBody
	public byte[] getImage(@PathVariable("id") Long id) {

	    Politician politician = politicianRepository.findById(id).orElse(null);
	    
	    if (politician != null && politician.getImage() != null) {
	       
	        return politician.getImage();
	    }
	    return new byte[0];
	}
}