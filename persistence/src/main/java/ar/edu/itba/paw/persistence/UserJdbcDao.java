package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.User;
import ar.edu.itba.paw.interfaces.UserDao;
import org.springframework.stereotype.Repository;


//TODO: Falta la dependencia de spring
@Repository
public class UserJdbcDao implements UserDao {
    @Override
    public User findById(long id) {
        return null;
    }
}