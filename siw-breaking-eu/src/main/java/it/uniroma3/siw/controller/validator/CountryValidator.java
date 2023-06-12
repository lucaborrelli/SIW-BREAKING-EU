package it.uniroma3.siw.controller.validator;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import it.uniroma3.siw.model.Country;
import it.uniroma3.siw.repository.CountryRepository;


@Component
public class CountryValidator implements Validator
{

	@Autowired
	private CountryRepository countryRepository;
	
	
	@Override
	public void validate(Object o, Errors errors) 
	{
		
		Country country= (Country)o; 
		
		if(this.countryRepository.existsByName(country.getName()))
			errors.reject("country.duplicate");
		else 
		{
			if(country.getName().isEmpty())
			{
				errors.rejectValue("name","Camponull");
			}
			else
				if(!(country.getName().matches("[a-zA-Z ]+")))
					errors.rejectValue("name","typeincorr");
			
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
			
			
			
			if(country.getPopulation() == null) 
			{
				errors.rejectValue("population","Camponull");
			}
			
			
			if(country.getPil() == null) 
			{
				errors.rejectValue("pil","Camponull");
			}
		}		
	}
	
	@Override
	public boolean supports(Class<?> aClass) 
	{
		return Country.class.equals(aClass);
	}
}