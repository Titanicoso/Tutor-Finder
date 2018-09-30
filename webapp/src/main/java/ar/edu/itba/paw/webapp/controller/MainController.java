package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@Controller
public class MainController {

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService us;

    @RequestMapping("/")
    public ModelAndView index(
            @ModelAttribute(value = "SUCCESS_MESSAGE") final String success_message,
            @ModelAttribute(value = "ERROR_MESSAGE") final String error_message
    ){
        ModelAndView mav = new ModelAndView("index");
        return mav;
    }

    @RequestMapping("favicon.ico")
    public String favicon() {
        return "forward:/resources/images/favicon.ico";
    }


    @ModelAttribute("currentUser")
    public User loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null)
            return null;

        final String username = auth.getName();
        if(!username.equals("anonymousUser"))
            return us.findByUsername(username);
        return null;
    }

    @ModelAttribute("currentUserIsProfessor")
    public boolean isProfessor() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null)
            return false;

        for (GrantedAuthority authority: auth.getAuthorities()) {
            if(authority.getAuthority().equals("ROLE_PROFESSOR"))
                return true;
        }
        return false;
    }
}
