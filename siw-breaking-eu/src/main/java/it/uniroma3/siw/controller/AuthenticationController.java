package it.uniroma3.siw.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Sort;


import it.uniroma3.siw.controller.validator.CredentialsValidator;
import it.uniroma3.siw.controller.validator.UserValidator;
import it.uniroma3.siw.model.Country;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.News;
import it.uniroma3.siw.model.Comment;
import it.uniroma3.siw.model.User;
import java.util.ArrayList;
import it.uniroma3.siw.repository.CountryRepository;
import it.uniroma3.siw.repository.NewsRepository;
import it.uniroma3.siw.repository.CommentRepository;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.UserService;

@Controller
public class AuthenticationController 
{
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserValidator userValidator;
	
	@Autowired
	private CredentialsValidator credentialsValidator;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private NewsRepository newsRepository;
	

	
	@RequestMapping(value = {"/", "/index"}, method = RequestMethod.GET) 
	public String showindex(Model model, Principal principal) 
	{
	    if(principal != null) 
	    {
	        String currentUsername = principal.getName();
	        if(currentUsername.equals("admin")) 
	        {
	            return "admin/index";
	        }
	    }
	    
	    List<News> newses = this.newsRepository.findAllByOrderByDateOfPubblication();
		int size=newses.size();
		
		model.addAttribute("newses",newses);
		model.addAttribute("size",size);
		
	    return "index";
	}
	
	
	@RequestMapping(value = "/public/index", method = RequestMethod.GET) 
	
	public String showindexPublic (Model model)
	{
		List<News> newses = this.newsRepository.findAllByOrderByDateOfPubblication();
		int size=newses.size();
		
		model.addAttribute("newses",newses);
		model.addAttribute("size",size);

		return "index";
	}
	
	
	@RequestMapping(value = "/admin/indexAdmin", method = RequestMethod.GET) 
	public String showindexAdmin (Model model)
	{
		return "admin/index";
	}
	
	
	
	@RequestMapping(value = "/login", method = RequestMethod.GET) 
	public String showLoginForm (Model model) {
		return "public/loginForm";
	}
	
	
	
	
	@RequestMapping(value = "/logout", method = RequestMethod.GET) 
	public String logout(Model model) 
	{
		return "index";
	}
	
	
	@RequestMapping(value = "/default", method = RequestMethod.GET)
    public String defaultAfterLogin(Model model) {
        

		
    	UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
    	
    	
    	if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
            return "/admin/index";
        }
        return "index";
    }
	
    
    @GetMapping("/admin/index")
	public String features(Model model) {
    	
		return "admin/index";
    }
    
    
    @RequestMapping(value = "/signup", method = RequestMethod.GET) 
	public String showRegisterForm (Model model)
	{
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		
		return "public/registerUser";
		
	}
    
    @RequestMapping(value = { "/signup" }, method = RequestMethod.POST)
    public String registerUser(@ModelAttribute("user") User user,
                 BindingResult userBindingResult,
                 @ModelAttribute("credentials") Credentials credentials,
                 BindingResult credentialsBindingResult)
      {

        // validate user and credentials fields
    	
    	
    	
        this.userValidator.validate(user, userBindingResult);
        this.credentialsValidator.validate(credentials, credentialsBindingResult);
        
        
        // if neither of them had invalid contents, store the User and the Credentials into the DB
        if(!userBindingResult.hasErrors() && ! credentialsBindingResult.hasErrors()) {
            // set the user and store the credentials;
            // this also stores the User, thanks to Cascade.ALL policy
        	
        	//user.setImage(image.getBytes()); 
        	
            credentials.setUser(user);
            userService.saveUser(user);
            credentialsService.saveCredentials(credentials);
            return "/public/loginForm";
        }
        
        return "public/registerUser";
    }
}