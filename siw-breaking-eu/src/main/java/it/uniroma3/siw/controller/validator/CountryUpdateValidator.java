package it.uniroma3.siw.controller.validator;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Country;
import it.uniroma3.siw.model.News;
import it.uniroma3.siw.repository.NewsRepository;

@Component
public class CountryUpdateValidator implements Validator 
{
	
	
	@Override 
	public void validate(Object o, Errors errors) 
	{
		Country country= (Country)o; 
		
		
		if(country.getCurrency().isEmpty()) 
		{
			errors.rejectValue("currency","Camponull");
		}
		
		else
			if(!(country.getCurrency().matches("[a-zA-Z ]+")))
				errors.rejectValue("currency","typeincorr");
		
		if(country.getCapital().isEmpty()) 
		{
			errors.rejectValue("capital","Camponull");
		}
		
		else
			if(!(country.getCapital().matches("[a-zA-Z ]+")))
				errors.rejectValue("capital","typeincorr");
		
		if(country.getFormOfGovernment().isEmpty()) 
		{
			errors.rejectValue("formOfGovernment","Camponull");
		}
		
		else
			if(!(country.getFormOfGovernment().matches("[a-zA-Z ]+")))
				errors.rejectValue("formOfGovernment","typeincorr");
		
		
		
		if(country.getPopulation() == null) 
		{
			errors.rejectValue("population","Camponull");
		}
		
		
		if(country.getPil() == null) 
		{
			errors.rejectValue("pil","Camponull");
		}
	}	

	
	@Override
	public boolean supports(Class<?> aClass) 
	{
		return Country.class.equals(aClass);
	}
}