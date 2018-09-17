package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.Professor;

public interface ProfessorService {

    Professor findById(final Long id);

    Professor findByUsername(final String username);

    Professor create(final Long userId, final String description);

    Professor createWithUser(final Long id, final String username, final String name, final String lastname,
                             final String password, final String email, final String description);
}
