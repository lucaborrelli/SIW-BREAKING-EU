package it.uniroma3.siw.controller.validator;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import it.uniroma3.siw.model.Politician;
import it.uniroma3.siw.repository.PoliticianRepository;


@Component
public class PoliticianValidator implements Validator
{

	@Autowired
	private PoliticianRepository politicianRepository;
	
	
	@Override
	public void validate(Object o, Errors errors) 
	{
		
		LocalDate today= LocalDate.now(); 
		Politician politic= (Politician)o; 
		
		if(this.politicianRepository.existsByNameAndSurname(politic.getName(), politic.getSurname()))
			errors.reject("politic.duplicate");
		else 
		{
			if(politic.getDateOfBirth() == null)
				errors.rejectValue("dateOfBirth", "typenull");
			else
			{
				
				
				if(!politic.getDateOfBirth().isBefore(today))
					errors.rejectValue("dateOfBirth","typeincorr");
			}
				
			if(politic.getName().isEmpty())
			{
				errors.rejectValue("name","typenull");
			}
			else
				if(!(politic.getName().matches("[a-zA-Z ]+")))
					errors.rejectValue("name","typeincorr");
			
			if(politic.getSurname().isEmpty()) 
			{
				errors.rejectValue("surname","typenull");
			}
			
			else
				if(!(politic.getSurname().matches("[a-zA-Z ]+")))
					errors.rejectValue("surname","typeincorr");
			
			
			
			if(politic.getDateOfDeath() != null)
			{
				if(!politic.getDateOfDeath().isBefore(today)) 
				{
					errors.rejectValue("dateOfDeath", "typeincorr");
				}
			
				if(!politic.getDateOfBirth().isBefore(politic.getDateOfDeath()))
					errors.rejectValue("dateOfDeath", "typeincorr");
			}
		}		
	}
	
	@Override
	public boolean supports(Class<?> aClass) 
	{
		return Politician.class.equals(aClass);
	}
}
