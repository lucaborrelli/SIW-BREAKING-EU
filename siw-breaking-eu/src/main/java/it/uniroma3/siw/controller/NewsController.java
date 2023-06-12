package it.uniroma3.siw.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.hibernate.Session;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Connection;

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

import it.uniroma3.siw.controller.validator.NewsUpdateValidator;
import it.uniroma3.siw.controller.validator.NewsValidator;
import it.uniroma3.siw.model.Country;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.News;
import it.uniroma3.siw.model.Comment;
import it.uniroma3.siw.repository.CountryRepository;

import it.uniroma3.siw.repository.NewsRepository;
import it.uniroma3.siw.repository.CommentRepository;
import it.uniroma3.siw.service.CredentialsService;

@Controller
public class NewsController 
{
	@Autowired NewsRepository newsRepository;
	@Autowired CountryRepository countryRepository;
	@Autowired NewsValidator newsValidator;
	@Autowired NewsUpdateValidator newsUpdateValidator;
	@Autowired CommentRepository commentRepository;
	@Autowired 
	private CredentialsService credentialsService;
	
	 
	@InitBinder
	public void initBinder(WebDataBinder binder) {
	    binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
	}

	
	
	@GetMapping("/admin/formNewNews")
	public String formNewNews(Model model) 
	{
		model.addAttribute("news", new News());
		return "admin/formNewNews";
	}

	@GetMapping("/admin/manageNewses")
	public String manageNewses(Model model) 
	{
		model.addAttribute("newses", this.newsRepository.findAll());
		return "admin/manageNewses";
		
	}
	
	
	@PostMapping("/admin/news/edit/{id}")
	public String editNews(@Valid @ModelAttribute("news") News news,BindingResult bindingResult,@PathVariable("id") Long id,Model model,
			@RequestParam("image") MultipartFile imageProfileFile,@RequestParam("text")String text,
			@RequestParam("writer")String writer) throws IOException
	{
		
		
		
		  
		  this.newsUpdateValidator.validate(news, bindingResult);

		  if (!bindingResult.hasErrors()) 
		  {
			  news.setDateOfPubblication(LocalDateTime.now());
    		 if (text != null && !text.isEmpty()) 
    		 {
    			 news.setText(text);
    		 }
		    
    		 if (writer != null && !writer.isEmpty()) 
    		 {
    			 news.setWriter(writer);
    		 }
    		 
    		 
    		 if (imageProfileFile != null) 
    		 {
    			 byte[] imageBytes = imageProfileFile.getBytes();
    			
				
    			 news.setImage(imageBytes);
    		 }
		 
    		 this.newsRepository.save(news);
    		 model.addAttribute("news",news);
    		 return "public/news";
    	}
    	 
    	else 
    	{
    		return "admin/formUpdateNews";
    	}
	}
	
	@GetMapping("/admin/deleteNews/{id}")
	public String deleteNews(@PathVariable("id") Long id, Model model)
	{
		
		News news = this.newsRepository.findById(id).get();
		
		List<Comment> comments= this.commentRepository.findByNews(news);
        
        
	
        for(Comment comment: comments) 
        	this.commentRepository.delete(comment);
        
        
        this.newsRepository.delete(news);
		model.addAttribute("newses", this.newsRepository.findAll());
		
		return "admin/manageNewses";
	}
	

	
	@GetMapping("/admin/updateCountriesOfNews/{id}")
	public String updateCountries(@PathVariable("id") Long id, Model model)
	{
	
		
		News news= this.newsRepository.findById(id).get();
		
		model.addAttribute("countries",news.getCountries());
		
		model.addAttribute("news",news);
		
		model.addAttribute("otherCountries",this.countryRepository.getCountryByNewsesNotContains(news)); 

		
		
		
		return "admin/countriesToAdd";
	}
	

	
	@GetMapping("/admin/addCountryToNews/{countryid}/{newsid}")
	public String addCountryToNews(@PathVariable("countryid") Long countryid,@PathVariable("newsid")Long newsid, Model model) 
	{
		
	        News news = this.newsRepository.findById(newsid).get();
	        Country country = this.countryRepository.findById(countryid).get();
	        
	        Set<Country> countriesOfNews = news.getCountries();
	        countriesOfNews.add(country);
	        
	        news.setCountries(countriesOfNews);
	        this.newsRepository.save(news);
	        
	        
	        country.getNewses().add(news);
	        this.countryRepository.save(country);

	        
	        model.addAttribute("news",news);
	        model.addAttribute("countriesOfNews",this.newsRepository.findById(newsid).get().getCountries());
	        
	        model.addAttribute("otherCountries",this.countryRepository.getCountryByNewsesNotContains(news)); 
	        
	        return "admin/countriesToAdd";
	    }

	
	@GetMapping("/admin/removeCountryFromNews/{countryid}/{newsid}")
	public String removeCountryFromNews(@PathVariable("countryid") Long countryid,@PathVariable("newsid")Long newsid, Model model) {
		
	        News news = this.newsRepository.findById(newsid).get();
	        Country country = this.countryRepository.findById(countryid).get();
	        
	        Set<Country> countriesOfNews = news.getCountries();
	        
	        
	        countriesOfNews.remove(country);
	        news.setCountries(countriesOfNews);
	        this.newsRepository.save(news);
	        
	        country.getNewses().remove(news);
	        this.countryRepository.save(country);

	        
	        model.addAttribute("news",news);
	        model.addAttribute("countriesOfNews",this.newsRepository.findById(newsid).get().getCountries());
	        
	        model.addAttribute("otherCountries",this.countryRepository.getCountryByNewsesNotContains(news));
	        
	        return "admin/countriesToAdd";
	    }
	


	
	

	@PostMapping("/admin/newses")
	public String newses(@Valid @ModelAttribute("news") News news,
	                       BindingResult bindingResult,
	                       Model model,
	                       @RequestParam("image") MultipartFile imageFile)throws IOException{

	    this.newsValidator.validate(news, bindingResult);

	    	if (!bindingResult.hasErrors()) 
	    	{
	    		news.setDateOfPubblication(LocalDateTime.now());
	    	
	    		byte[] imageBytes = imageFile.getBytes();
	        
	    	
	    		news.setImage(imageBytes);
	        
	    
	    		this.newsRepository.save(news);
	    		model.addAttribute("news", news);
	    		model.addAttribute("countries", news.getCountries());
	    		return "/public/news";
	    	}
	    	else 
	    	{
	    		return "admin/formNewNews";
	    	}
	        
	    }
	
	@GetMapping("/image/{id}")
	@ResponseBody
	public byte[] getImage(@PathVariable("id") Long id) {
	    
	    News news = newsRepository.findById(id).orElse(null);
	    if (news != null && news.getImage() != null) {
	      
	        return news.getImage();
	    }
	    return new byte[0];
	}
	
	
	
	@GetMapping("/admin/formUpdateNews/{id}")
	public String getUpdateNews(@PathVariable("id") Long id, Model model) 
	{
		
		model.addAttribute("news", this.newsRepository.findById(id).get());
		
		News news=this.newsRepository.findById(id).get();
        model.addAttribute("countries", news.getCountries());
        
		return "admin/formUpdateNews";
	}
	
	
	
	@GetMapping("/public/newses/{id}")
	public String getNews(@PathVariable("id") Long id, Model model, Authentication authentication) 
	{
		
		News news= this.newsRepository.findById(id).get();
		
		
		
		List<Comment> commentAllMovie= this.commentRepository.findByNews(news); 
	    if (authentication != null && authentication.isAuthenticated()) 
	    {
	    	
	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
	        
	        
	        
	        for(Comment comment: commentAllMovie)
	        {
	        	if( comment.getWriter().getUsername().equals(credentials.getUser().getUsername())) 
	        	{
	        		model.addAttribute("comment",comment); 
	        		model.addAttribute("comments",commentAllMovie); 
	        		model.addAttribute("countries",this.countryRepository.getCountryByNewsesContains(news)); 
	        		model.addAttribute("news", news);
	        		return "public/news";
	        	}
	        }
	    }
		model.addAttribute("comments",commentAllMovie); 
		model.addAttribute("countries",this.countryRepository.getCountryByNewsesContains(news)); 
	    model.addAttribute("comment",null);
	    model.addAttribute("news",news);
	    return "public/news";
	}


	@GetMapping("/public/newses")
	public String showNewses(Model model) 
	{
		model.addAttribute("newses", this.newsRepository.findAll());
		return "public/newses";
	}
	
	@GetMapping("/public/formSearchNewses")
	public String formSearchNewses() 
	{
		return "public/formSearchNewses";
	}

	@PostMapping("/public/searchNewses")
	public String searchNewses(Model model,@RequestParam String title, @RequestParam String writer)
	{
		if(title == "" && writer == "")
		{
			model.addAttribute("newses", null);
		}
		
		if(title == "" && writer != "")
		{
			model.addAttribute("newses", this.newsRepository.findByWriter(writer));
		}
		
		if(writer == "" && title != "")
		{
			model.addAttribute("newses", this.newsRepository.findByTitle(title));
		}
		
		if(title != "" && writer != "")
		{
			model.addAttribute("newses", this.newsRepository.findByTitleAndWriter(title,writer));
		}
		
		return "public/foundNewses";
	}
}