package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.dto.ValidationErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.Locale;
import java.util.Map;

@Component
public class BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private UserService us;

    @Autowired
    private MessageSource messageSource;

    public User loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null)
            return null;

        final String username = auth.getName();
        if(!username.equals("anonymousUser")) {
            final User user = us.findByUsername(username);
            if(user != null) {
                LOGGER.debug("Currently logged user has id {}", user.getId());
            }
            return user;
        }
        return null;
    }

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

    public Response badRequest(final String error) {
        final ValidationErrorDTO errors = new ValidationErrorDTO(error);
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errors).build();
    }

    public ValidationErrorDTO getErrors(final String error) {
        return new ValidationErrorDTO(messageSource.getMessage(error,
                null, Locale.getDefault()));
    }

    public ValidationErrorDTO addError(final ValidationErrorDTO errors, final String error, final String field) {
        errors.addError(field, messageSource.getMessage(error,null, Locale.getDefault()));
        return errors;
    }
}
