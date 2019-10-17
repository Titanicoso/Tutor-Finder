package ar.edu.itba.paw.webapp.providers;

import org.glassfish.jersey.server.validation.ValidationConfig;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.springframework.context.MessageSource;
import org.springframework.validation.beanvalidation.MessageSourceResourceBundleLocator;

import javax.inject.Inject;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
public class ValidationConfigContextResolver implements ContextResolver<ValidationConfig> {

    private final MessageSource messageSource;

    @Inject
    public ValidationConfigContextResolver(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public ValidationConfig getContext(Class<?> aClass) {
        final ValidationConfig config = new ValidationConfig();
        config.messageInterpolator(
                new ResourceBundleMessageInterpolator(
                        new MessageSourceResourceBundleLocator(messageSource)
                )
        );
        return config;
    }

}
