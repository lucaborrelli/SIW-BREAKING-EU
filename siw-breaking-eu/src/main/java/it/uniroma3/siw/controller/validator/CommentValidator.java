package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.News;
import it.uniroma3.siw.model.Comment;

@Component
public class CommentValidator implements Validator 
{

	
	@Override 
	public void validate(Object o, Errors errors) 
	{
		
		Comment comment= (Comment)o;
		
		if(comment.getText().length()>254)
			errors.rejectValue("text", "maxchar");
	}
	
	@Override
	public boolean supports(Class<?> aClass) 
	{
		return Comment.class.equals(aClass);
	}
}