package ar.edu.itba.paw.webapp.dto.form;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RegisterForm {

    @NotNull(message = "{NotNull.name}")
    @Size(min = 1, max = 128, message = "{Size.registerForm.name}")
    @Pattern(regexp = "[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+", message = "{Pattern.registerForm.name}")
    private String name;

    @NotNull(message = "{NotNull.lastname}")
    @Size(min = 1, max = 128, message = "{Size.registerForm.lastname}")
    @Pattern(regexp = "[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s]+", message = "{Pattern.registerForm.lastname}")
    private String lastname;

    @NotNull(message = "{NotNull.email}")
    @Size(min = 1, max = 512, message = "{Size.registerForm.email}")
    @Email
    private String email;

    @NotNull(message = "{NotNull.username}")
    @Size(min = 1, max = 128, message = "{Size.registerForm.username}")
    private String username;

    @NotNull(message = "{NotNull.password}")
    @Size(min = 8, max = 64, message = "{Size.registerForm.password}")
    private String password;

    public void setName(String name) {
        this.name = name;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
