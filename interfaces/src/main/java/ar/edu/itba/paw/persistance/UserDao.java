package ar.edu.itba.paw.persistance;

import ar.edu.itba.paw.model.User;

public interface UserDao {

    User findById(long id);
}
