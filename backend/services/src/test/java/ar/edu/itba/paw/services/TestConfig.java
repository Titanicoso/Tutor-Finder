package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.persistence.*;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.thymeleaf.TemplateEngine;

@ComponentScan({ "ar.edu.itba.paw.services"})
@Configuration
public class TestConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        return Mockito.mock(JavaMailSender.class);
    }

    @Bean
    public TemplateEngine getTemplateEngine() {
        return Mockito.mock(TemplateEngine.class);
    }

    @Bean
    public AreaDao areaDao() {
        return Mockito.mock(AreaDao.class);
    }

    @Bean
    public ConversationDao conversationDao() {
        return Mockito.mock(ConversationDao.class);
    }

    @Bean
    public CourseDao courseDao() {
        return Mockito.mock(CourseDao.class);
    }

    @Bean
    public PasswordResetTokenDao passwordResetTokenDao() {
        return Mockito.mock(PasswordResetTokenDao.class);
    }

    @Bean
    public ProfessorDao professorDao() {
        return Mockito.mock(ProfessorDao.class);
    }

    @Bean
    public ScheduleDao scheduleDao() {
        return Mockito.mock(ScheduleDao.class);
    }

    @Bean
    public SubjectDao subjectDao() {
        return Mockito.mock(SubjectDao.class);
    }

    @Bean
    public UserDao userDao() {
        return Mockito.mock(UserDao.class);
    }

    @Bean
    public ClassReservationDao classReservationDao() {
        return Mockito.mock(ClassReservationDao.class);
    }

    @Bean
    public CourseFileDao courseFileDao() {
        return Mockito.mock(CourseFileDao.class);
    }
}
