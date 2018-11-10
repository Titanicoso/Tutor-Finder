package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.form.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
public class UserController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService us;

    @Autowired
    @Qualifier("professorServiceImpl")
    private ProfessorService ps;

    @Autowired
    @Qualifier("courseServiceImpl")
    private CourseService cs;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ScheduleService ss;

    @Autowired
    private PasswordResetService passwordResetService;

    @RequestMapping("/register")
    public ModelAndView register(@ModelAttribute("registerForm") final RegisterForm form) {
        return new ModelAndView("register");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView create(@Valid @ModelAttribute("registerForm") final RegisterForm form,
                               final BindingResult errors, HttpServletRequest request) {
        if(errors.hasErrors() || !form.checkRepeatPassword()) {
            if(!form.checkRepeatPassword()) {
                errors.rejectValue("repeatPassword", "RepeatPassword");
            }
            return register(form);
        }
        final User u;

        try {
            u = us.create(form.getUsername(), form.getPassword(), form.getEmail(), form.getName(), form.getLastname());
        } catch (EmailAlreadyInUseException e) {
            errors.rejectValue("email", "RepeatedEmail");
            return register(form);
        } catch (UsernameAlreadyInUseException e) {
            errors.rejectValue("username", "RepeatedUsername");
            return register(form);
        } catch (UsernameAndEmailAlreadyInUseException e){
            errors.rejectValue("email", "RepeatedEmail");
            errors.rejectValue("username", "RepeatedUsername");
            return register(form);
        }

        if(u == null) {
            return register(form);
        }

        LOGGER.debug("Authenticating user with id {}", u.getId());
        authenticateRegistered(request, u.getUsername(), u.getPassword());

        return redirectWithNoExposedModalAttributes("/");
    }

    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @RequestMapping("/Professor/{id}")
    public ModelAndView professorProfile(@PathVariable(value = "id") long id,
                                         @ModelAttribute("currentUser") final User loggedUser,
                                         @ModelAttribute("currentUserIsProfessor") final boolean isProfessor,
                                         @RequestParam(value = "page", defaultValue = "1") final int page) {
        if(loggedUser != null && loggedUser.getId() == id && isProfessor) {
            return redirectWithNoExposedModalAttributes("/Profile");
        }

        final Professor professor = ps.findById(id);
        if(professor == null) {
            return redirectToErrorPage("nonExistentProfessor");
        }

        final ModelAndView mav = new ModelAndView("profile");
        final Schedule schedule = ss.getScheduleForProfessor(professor.getId());

        final PagedResults<Course> courses = cs.findCourseByProfessorId(id, page);

        if(courses == null) {
            redirectToErrorPage("pageOutOfBounds");
        }

        mav.addObject("courses", courses);
        mav.addObject("page", page);
        mav.addObject("schedule", schedule);
        mav.addObject("professor", professor);
        return mav;
    }

    @RequestMapping("/Profile")
    public ModelAndView profile(
            @ModelAttribute("currentUser") final User loggedUser,
            @ModelAttribute("addScheduleForm") final ScheduleForm addScheduleForm,
            @ModelAttribute("deleteScheduleForm") final ScheduleForm deleteScheduleForm,
            @RequestParam(value = "page", defaultValue = "1") final int page
    ) throws NonexistentProfessorException {
        final Professor professor = ps.findById(loggedUser.getId());
        if(professor == null) {
            throw new NonexistentProfessorException();
        }

        final ModelAndView mav = new ModelAndView("profileForProfessor");

        final Schedule schedule = ss.getScheduleForProfessor(professor.getId());

        final PagedResults<Course> courses = cs.findCourseByProfessorId(professor.getId(), page);

        if(courses == null) {
            redirectToErrorPage("pageOutOfBounds");
        }

        mav.addObject("courses", courses);
        mav.addObject("professor", professor);
        mav.addObject("schedule", schedule);
        mav.addObject("page", page);
        return mav;
    }
    
    private void authenticateRegistered(HttpServletRequest request, String username, String password) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
        authToken.setDetails(new WebAuthenticationDetails(request));

        Authentication authentication = authenticationManager.authenticate(authToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    @RequestMapping(value = "/registerAsProfessor", method = RequestMethod.POST)
    public ModelAndView createProfessor(@ModelAttribute("currentUser") final User loggedUser,
                                        @Valid @ModelAttribute("registerAsProfessorForm") final RegisterProfessorForm form,
                                        final BindingResult errors, final HttpServletRequest request) throws ProfessorWithoutUserException {
        if(errors.hasErrors()) {
            return registerProfessor(form);
        }

        final Professor p;
        try {
            p = ps.create(loggedUser.getId(), form.getDescription(), form.getPicture().getBytes());
        } catch (IOException e) {
            return redirectToErrorPage("fileUploadError");
        }
        if(p == null) {
            return registerProfessor(form);
        }

        authenticateRegistered(request, p.getUsername(), p.getPassword());

        return redirectWithNoExposedModalAttributes("/");
    }

    @RequestMapping("/registerAsProfessor")
    public ModelAndView registerProfessor(@ModelAttribute("registerAsProfessorForm") final RegisterProfessorForm form) {
        return new ModelAndView("registerAsProfessorForm");
    }

    @RequestMapping(value = "/CreateTimeSlot", method = RequestMethod.POST)
    public ModelAndView createTimeslot(
            @ModelAttribute("currentUser") final User loggedUser,
            @Valid @ModelAttribute("ScheduleForm") final ScheduleForm form,
            final BindingResult errors
    ) throws NonexistentProfessorException {
        if(errors.hasErrors() || !form.validForm()) {
            if(!form.validForm()) {
                errors.rejectValue("endHour", "profile.add_schedule.timeError");
            }
            return profile(loggedUser, form, new ScheduleForm(), 1);
        }

        final List<Timeslot> timeslots;

        try {
            timeslots = ss.reserveTimeSlot(loggedUser.getId(), form.getDay(), form.getStartHour(), form.getEndHour());
        } catch (NonexistentProfessorException e) {
            return redirectToErrorPage("nonExistentUser");
        } catch (TimeslotAllocatedException e) {
            errors.rejectValue("endHour", "TimeslotAllocatedError");
            return profile(loggedUser, form, new ScheduleForm(),1);
        }

        if(timeslots == null) {
            return profile(loggedUser, form, new ScheduleForm(), 1);
        }

        return redirectWithNoExposedModalAttributes("/Profile");
    }


    @RequestMapping(value = "/RemoveTimeSlot", method = RequestMethod.POST)
    public ModelAndView RemoveTimeslot(
            @ModelAttribute("currentUser") final User loggedUser,
            @Valid @ModelAttribute("ScheduleForm") final ScheduleForm form,
            final BindingResult errors
    ) throws NonexistentProfessorException {
        if(errors.hasErrors() || !form.validForm()) {
            if(!form.validForm()) {
                errors.rejectValue("endHour", "profile.add_schedule.timeError");
            }
            return profile(loggedUser, new ScheduleForm(), form, 1);
        }
        try {
            ss.removeTimeSlot(loggedUser.getId(), form.getDay(), form.getStartHour(), form.getEndHour());
        } catch (NonexistentProfessorException e) {
            return redirectToErrorPage("nonExistentUser");
        }

        return redirectWithNoExposedModalAttributes("/Profile");
    }

    @RequestMapping(value = "/forgotPassword")
    public ModelAndView forgotPassword(@ModelAttribute("resetPasswordForm") final ResetPasswordRequestForm form) {
        return new ModelAndView("forgotPassword");
    }

    @RequestMapping(value = "/forgotPassword", method = RequestMethod.POST)
    public ModelAndView forgotPassword(@Valid @ModelAttribute("resetPasswordForm") final ResetPasswordRequestForm form,
                                       final BindingResult errors) {
        if(errors.hasErrors()) {
            return forgotPassword(form);
        }

        final boolean created;
        try {
            created = passwordResetService.createToken(form.getEmail());
        } catch (TokenCrationException e) {
            errors.rejectValue("email", "mailSendError");
            return forgotPassword(form);
        }

        if(!created) {
            errors.rejectValue("email", "forgotPasswordError");
            return forgotPassword(form);
        }
        errors.rejectValue("successMessage", "forgotPasswordSuccess");
        form.setEmail("");
        return forgotPassword(form);
    }

    @RequestMapping("/resetPassword")
    public ModelAndView resetPassword(@ModelAttribute("resetPasswordForm") final ResetPasswordForm form,
                                      @RequestParam(value="token", required=true) final String token) {

        if(token.isEmpty()) {
            return redirectToErrorPage("invalidToken");
        }

        final PasswordResetToken passwordResetToken = passwordResetService.findByToken(token);

        if(passwordResetToken == null) {
            return redirectToErrorPage("invalidToken");
        }

        return new ModelAndView("resetPassword");
    }


    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public ModelAndView resetPassword(@ModelAttribute("resetPasswordForm") final ResetPasswordForm form,
                                      final BindingResult errors,
                                      @RequestParam(value="token", required=true) final String token,
                                      HttpServletRequest request) {
        if(errors.hasErrors() || !form.checkRepeatPassword()) {
            if(!form.checkRepeatPassword()) {
                errors.rejectValue("repeatPassword", "RepeatPassword");
            }
            return resetPassword(form, token);
        }

        final User changedUser;
        try {
            changedUser = passwordResetService.changePassword(token, form.getPassword());
        } catch (InvalidTokenException e) {
            return redirectToErrorPage("invalidToken");
        }

        if(changedUser == null) {
            return redirectToErrorPage("changePasswordError");
        }

        authenticateRegistered(request, changedUser.getUsername(), form.getPassword());
        return redirectWithNoExposedModalAttributes("/");
    }


}
