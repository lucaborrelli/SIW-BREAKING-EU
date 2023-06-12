package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.News;
import it.uniroma3.siw.repository.NewsRepository;

@Component
public class NewsUpdateValidator implements Validator 
{
	
	@Autowired
	private NewsRepository newsRepository;
	
	@Override 
	public void validate(Object o, Errors errors) 
	{
		
		News news= (News)o;
			if(news.getText().length() > 254 ) 
			{
				errors.rejectValue("text", "maxchar");
			}
			
			if(news.getWriter().length() > 254 ) 
			{
				errors.rejectValue("writer", "maxchar");
			}
			
			if(news.getLink().length() > 254 ) 
			{
				errors.rejectValue("link", "maxchar");
			}
	}
	
	@Override
	public boolean supports(Class<?> aClass) 
	{
		return News.class.equals(aClass);
	}
}