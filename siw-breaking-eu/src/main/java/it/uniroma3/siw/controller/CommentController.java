package it.uniroma3.siw.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import it.uniroma3.siw.controller.validator.CommentValidator;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.News;
import it.uniroma3.siw.model.Comment;
import it.uniroma3.siw.repository.CountryRepository;
import it.uniroma3.siw.repository.NewsRepository;
import it.uniroma3.siw.repository.CommentRepository;
import it.uniroma3.siw.service.CredentialsService;

@Controller
public class CommentController 
{
	@Autowired CommentRepository commentRepository;
	@Autowired NewsRepository newsRepository;
	@Autowired CountryRepository countryRepository;
	@Autowired CommentValidator commentValidator;
	@Autowired private CredentialsService credentialsService;


	
	@GetMapping("/default/comment/{commentid}/{newsid}")
	public String myComment(Model model,@PathVariable("commentid") Long commentid,
            				@PathVariable("newsid") Long newsid)
	{
		model.addAttribute("comment",this.commentRepository.findById(commentid).get());
		model.addAttribute("news",this.commentRepository.findById(newsid).get());
		
		return "default/commentNews";
	}
	
	@GetMapping("/admin/Comments")
	public String Comments(Model model)
	{
		model.addAttribute("comments",this.commentRepository.findAll());
		return "admin/Comments";
	}
	
	
	
	@GetMapping("/admin/deleteComment/{id}")
	public String deleteComment(@PathVariable("id") Long id, Model model)
	{
		
		Comment comment = this.commentRepository.findById(id).get();
		
		News news = comment.getNews();
		
	
        this.commentRepository.delete(comment);
        
        
    	
    	this.newsRepository.save(news);
    		
    	model.addAttribute("comments", this.commentRepository.findAll());
		
		return "admin/Comments";
	}
	
	
	
	
	
	
	
	@GetMapping("/default/Newcomment/{id}")
	public String newComment(Model model,@PathVariable("id") Long id)
	{
		News news=this.newsRepository.findById(id).get();
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
    		
		model.addAttribute("comment", new Comment());
		model.addAttribute("news",this.newsRepository.findById(id).get());
		
		return "default/newCommentNews";
	}
	
	
	

	
	
	
	@PostMapping("/default/setComment/{id}")
	public String setComment(@Valid @ModelAttribute("comment") Comment comment,BindingResult bindingResult,
	                       Model model,@PathVariable("id") Long id)
	{
		
		News news= this.newsRepository.findById(id).get();
		
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
    	
    	
	    this.commentValidator.validate(comment, bindingResult);

	    	if (!bindingResult.hasErrors()) 
	    	{
	    		this.newsRepository.save(news);
	    		
	    		credentials.getUser().setUsername(credentials.getUsername());
	    		
	    		comment.setWriter(credentials.getUser());
	    		comment.setNews(news);
	    		comment.setDate(LocalDateTime.now());
	    		
	    		this.commentRepository.save(comment);
	    		
	    		
	    		
		    	
		    	model.addAttribute("news",news);
		    	model.addAttribute("comment",comment);
		    	return "public/news";
	    	}
	    	else
	    	{
	    		model.addAttribute("news",news);
	    		return "default/newCommentNews";
	    	}	
		}
	}