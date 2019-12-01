package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.service.EmailService;
import ar.edu.itba.paw.models.Conversation;
import ar.edu.itba.paw.models.Message;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@PropertySource(value = "classpath:${spring.profiles.active}-email.properties", ignoreResourceNotFound = true)
public class EmailServiceImpl implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);
    private static final String PNG = "image/png";

    @Value("classpath:WelcomeMail.html")
    private Resource registrationMail;

    @Value("classpath:ConversationMail.html")
    private Resource contactMail;

    @Value("classpath:RestorePassword.html")
    private Resource restorePassword;

    @Value("classpath:logo.png")
    private Resource logo;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private TemplateEngine resourceTemplateEngine;

    @Autowired
    private Environment env;

    @Override
    public void sendEmail(String to, String subject, String text) {
        LOGGER.debug("Sending email to {} with subject {}", to, subject);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    @Override
    public void sendMailToProfessor(Professor professor, String subject, String text) {
        sendEmail(professor.getEmail(), subject, text);
    }


    @Override
    public boolean sendRestorePasswordEmail(final User user, final String token) {
        LOGGER.debug("Creating Password Restore Email for user with id {} and token {}", user.getId(), token);
        final Context ctx = new Context();
        ctx.setVariable("mail", user.getEmail());
        ctx.setVariable("url", getContextUri() + "forgotPassword?token=" + token);
        final String subject = messageSource.getMessage("mail.restorepass.subject", null, LocaleContextHolder.getLocale());

        final String resource = htmlString(restorePassword);

        if(resource == null) {
            LOGGER.debug("Error creating Password Restore Email for user with id {}", user.getId());
            return false;
        }

        final String html = resourceTemplateEngine.process(resource ,ctx);
        final MimeMessage mimeMessage = emailSender.createMimeMessage();
        final MimeMessageHelper message = prepareMail(mimeMessage, subject, user.getEmail(), html);

        if(message == null) {
            LOGGER.debug("Error creating Password Restore Email for user with id {}", user.getId());
            return false;
        }

        LOGGER.debug("Sending Password Restore Email for user with id {}", user.getId());
        emailSender.send(mimeMessage);
        return true;
    }


    @Override
    @Async
    public void sendRegistrationEmail(final User user) {
        LOGGER.debug("Creating Registration Email for user with id {}", user.getId());
        final Context ctx = new Context();
        ctx.setVariable("name", user.getName());
        ctx.setVariable("username", user.getUsername());
        ctx.setVariable("url", getContextUri());
        Locale locale = LocaleContextHolder.getLocale();
        final String subject = messageSource.getMessage("mail.registration.subject", null, locale);

        final String resource = htmlString(registrationMail);

        if(resource == null) {
            LOGGER.debug("Error creating Registration Email for user with id {}", user.getId());
            return;
        }

        final String html = resourceTemplateEngine.process(resource ,ctx);
        final MimeMessage mimeMessage = emailSender.createMimeMessage();
        final MimeMessageHelper message = prepareMail(mimeMessage, subject, user.getEmail(), html);

        if(message == null) {
            LOGGER.debug("Error creating Registration Email for user with id {}", user.getId());
            return;
        }

        LOGGER.debug("Sending Registration Email for user with id {}", user.getId());
        emailSender.send(mimeMessage);
    }

    @Override
    @Async
    public void sendContactEmail(final User from, final User to, final Conversation conversation,
                                 final Message sentMessage) {
        LOGGER.debug("Creating Contact Email for user with id {} from user with id {} for conversation with id {}", to.getId(),
                from.getId(), conversation.getId());
        final Context ctx = new Context();
        ctx.setVariable("name", from.getName());
        ctx.setVariable("lastname", from.getLastname());
        ctx.setVariable("subject", conversation.getSubject().getName());
        ctx.setVariable("message", sentMessage.getText());
        ctx.setVariable("url", getContextUri() + "conversation/" + conversation.getId());
        final String subject = messageSource.getMessage("mail.newmessage.subject", null, LocaleContextHolder.getLocale());

        final String resource = htmlString(contactMail);

        if(resource == null) {
            LOGGER.debug("Error creating Contact Email for user with id {} from conversation with id {}", to.getId(), conversation.getId());
            return;
        }

        final String html = resourceTemplateEngine.process(resource ,ctx);
        final MimeMessage mimeMessage = emailSender.createMimeMessage();
        final MimeMessageHelper message = prepareMail(mimeMessage, subject, to.getEmail(), html);

        if(message == null) {
            LOGGER.debug("Error creating Contact Email for user with id {} from conversation with id {}", to.getId(), conversation.getId());
            return;
        }

        LOGGER.debug("Sending Contact Email for user with id {} from conversation with id {}", to.getId(), conversation.getId());
        emailSender.send(mimeMessage);
    }

    private MimeMessageHelper prepareMail(MimeMessage message, final String subject, final String to, final String html) {
        final MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            helper.addInline("logo", logo, PNG);
        } catch (MessagingException e) {
            return null;
        }
        return helper;
    }

    private String htmlString(final Resource resource){
        final InputStream resourceIo;
        final String html;
        try {
            resourceIo = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(resourceIo));
            html = reader.lines()
                    .collect(Collectors.joining("\n"));
        } catch (IOException e) {
            return null;
        }
        return html;
    }

    private String getContextUri() {
        return env.getProperty("ui.url");
    }
}
