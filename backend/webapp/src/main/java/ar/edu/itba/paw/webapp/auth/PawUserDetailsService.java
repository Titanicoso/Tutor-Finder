package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.service.ProfessorService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;

@Component
public class PawUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService us;

    @Autowired
    private ProfessorService ps;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final Professor professor = ps.findByUsername(username);

        if(professor == null) {
            final User user = us.findByUsername(username);

            if(user == null){
                throw  new UsernameNotFoundException("Cannot find user with username: " + username);
            }

            final Collection<? extends GrantedAuthority> userAuthorities = Arrays.asList(
                    new SimpleGrantedAuthority("ROLE_USER"));

            return new org.springframework.security.core.userdetails.User(username,
                    user.getPassword(), userAuthorities);
        }

        final Collection<? extends GrantedAuthority> userAuthorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_PROFESSOR"));

        return new org.springframework.security.core.userdetails.User(username,
                professor.getPassword(), userAuthorities);
    }
}
