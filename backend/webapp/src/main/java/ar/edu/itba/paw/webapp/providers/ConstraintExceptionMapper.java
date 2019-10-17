package ar.edu.itba.paw.webapp.providers;

import ar.edu.itba.paw.webapp.dto.ValidationErrorDTO;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ConstraintExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(final ConstraintViolationException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new ValidationErrorDTO(exception.getConstraintViolations()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
