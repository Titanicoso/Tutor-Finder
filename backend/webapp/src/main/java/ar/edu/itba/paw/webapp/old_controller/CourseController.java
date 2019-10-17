package ar.edu.itba.paw.webapp.old_controller;

import ar.edu.itba.paw.exceptions.*;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.form.ClassReservationForm;
import ar.edu.itba.paw.webapp.form.CommentForm;
import ar.edu.itba.paw.webapp.form.CourseForm;
import ar.edu.itba.paw.webapp.form.MessageForm;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;


@Controller
public class CourseController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    @Qualifier("courseServiceImpl")
    private CourseService courseService;

    @Autowired
    @Qualifier("subjectServiceImpl")
    private SubjectService subjectService;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ClassReservationService classReservationService;

    @RequestMapping("/Course")
    public ModelAndView course(
            @ModelAttribute("messageForm") final MessageForm messageForm,
            @ModelAttribute("commentForm") final CommentForm commentForm,
            @RequestParam(value="professor", required=true) final long professorId,
            @RequestParam(value="subject", required=true) final long subjectId,
            @ModelAttribute("currentUser") final User loggedUser,
            @RequestParam(value = "page", defaultValue = "1") final int page){

        final ModelAndView mav = new ModelAndView("course");
        final Course course = courseService.findCourseByIds(professorId, subjectId);
        if(course == null) {
            return redirectToErrorPage("nonExistentCourse");
        }
        mav.addObject("course", course);
        LOGGER.debug("Creating view for Course with professor id {} and subject id {}", professorId, subjectId);

        final Schedule schedule = scheduleService.getScheduleForProfessor(professorId);

        final PagedResults<Comment> comments = courseService.getComments(course, page);

        if(comments == null) {
            redirectToErrorPage("pageOutOfBounds");
        }

        final boolean canComment = classReservationService.hasAcceptedReservation(loggedUser, course);

        mav.addObject("comments", comments);

        mav.addObject("schedule", schedule);
        mav.addObject("page", page);
        mav.addObject("canComment", canComment);
        messageForm.setProfessorId(professorId);
        messageForm.setSubjectId(subjectId);
        commentForm.setCommentProfessorId(professorId);
        commentForm.setCommentSubjectId(subjectId);
        return mav;
    }

    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    public ModelAndView contact(
            @ModelAttribute("commentForm") final CommentForm comment,
            @Valid @ModelAttribute("messageForm") final MessageForm form,
            final BindingResult errors,
            @ModelAttribute("currentUser") final User loggedUser) throws UserNotInConversationException, NonexistentConversationException {

        if(errors.hasErrors()) {
            return course(form,comment, form.getProfessorId(), form.getSubjectId(), loggedUser, 1);
        }

        final Conversation conversation;
        try {
            conversation = conversationService.sendMessage(loggedUser.getId(), form.getProfessorId(),
                    form.getSubjectId(), form.getBody());
        } catch (SameUserException e) {
            errors.rejectValue("body", "SameUserMessageError");
            form.setBody(null);
            return course(form,comment, form.getProfessorId(), form.getSubjectId(), loggedUser,1);
        }
        if(conversation != null) {
            errors.rejectValue("extraMessage", "MessageSent");
            form.setBody(null);
            return course(form,comment, form.getProfessorId(), form.getSubjectId(), loggedUser,1);
        } else {
            errors.rejectValue("body", "SendMessageError");
        }
        return course(form,comment, form.getProfessorId(), form.getSubjectId(), loggedUser,1);
    }

    @RequestMapping(value = "/postComment", method = RequestMethod.POST)
    public ModelAndView comment(
            @ModelAttribute("messageForm") final MessageForm message,
            @Valid @ModelAttribute("commentForm") final CommentForm form,
            final BindingResult errors,
            @ModelAttribute("currentUser") final User loggedUser) {

        if(errors.hasErrors()) {
            return course(message, form, form.getCommentProfessorId(), form.getCommentSubjectId(), loggedUser,1);
        }

        final boolean sent;
        try {
            sent = courseService.comment(loggedUser.getId(), form.getCommentProfessorId(),
                        form.getCommentSubjectId(), form.getCommentBody(), form.getRating());
        } catch (SameUserException e) {
            errors.rejectValue("rating", "sameUserComment");
            return course(message, form, form.getCommentProfessorId(), form.getCommentSubjectId(), loggedUser,1);
        } catch (NonAcceptedReservationException e) {
            errors.rejectValue("rating", "nonAcceptedReservation");
            return course(message, form, form.getCommentProfessorId(), form.getCommentSubjectId(), loggedUser,1);
        }

        if(sent) {
            return redirectWithNoExposedModalAttributes("/Course/?professor=" +
                    form.getCommentProfessorId() + "&subject=" + form.getCommentSubjectId());
        } else {
            errors.rejectValue("commentBody", "SendMessageError");
        }
        return course(message, form, form.getCommentProfessorId(), form.getCommentSubjectId(), loggedUser,1);
    }


    @RequestMapping("/createCourse")
    public ModelAndView createCourse(@ModelAttribute("CourseForm") final CourseForm form,
                                     @ModelAttribute("currentUser") final User user) {
        final ModelAndView mav = new ModelAndView("createCourse");
        mav.addObject("subjects", subjectService.getAvailableSubjects(user.getId()));
        return mav;
    }

    @RequestMapping(value = "/createCourse", method = RequestMethod.POST)
    public ModelAndView create(@Valid @ModelAttribute("CourseForm") final CourseForm form,
                               final BindingResult errors,
                               @ModelAttribute("currentUser") final User user) {
        if(errors.hasErrors()) {
            return createCourse(form, user);
        }

        final Course course;
        try {
            course = courseService.create(user.getId(), form.getSubjectId(), form.getDescription(), form.getPrice());
        } catch (CourseAlreadyExistsException e) {
            return redirectToErrorPage("courseAlreadyExists");
        } catch (NonexistentProfessorException e) {
            return redirectToErrorPage("nonExistentUser");
        } catch (NonexistentSubjectException e) {
            errors.rejectValue("subjectId", "subjectDoesNotExist");
            return createCourse(form, user);
        }

        if(course == null) {
            return createCourse(form, user);
        }

        LOGGER.debug("Posting request for course creation for professor with id {} in subject with id {}", user.getId(), form.getSubjectId());
        return redirectWithNoExposedModalAttributes("/Course/?professor=" + course.getProfessor().getId()
                + "&subject=" + course.getSubject().getId());
    }

    @RequestMapping(value = "/reserveClass", method = RequestMethod.GET)
    public ModelAndView reserveClass(@ModelAttribute("currentUser") final User user,
                                     @ModelAttribute("classReservationForm") final ClassReservationForm form,
                                     @RequestParam("professor") final long professorId,
                                     @RequestParam("subject") final long subjectId) {
        final ModelAndView mav = new ModelAndView("reserveClass");
        final Schedule schedule = scheduleService.getScheduleForProfessor(professorId);
        mav.addObject("schedule", schedule);
        return mav;
    }

    @RequestMapping(value = "/reserveClass", method = RequestMethod.POST)
    public ModelAndView reserveClass(@ModelAttribute("currentUser") final User user,
                                     @Valid @ModelAttribute("classReservationForm")
                                     final ClassReservationForm form,
                                     final BindingResult errors,
                                     @RequestParam("professor") final long professorId,
                                     @RequestParam("subject") final long subjectId) {

        if(errors.hasErrors() || !form.validForm()) {
            if(!form.validForm()) {
                errors.rejectValue("endHour", "profile.add_schedule.timeError");
            }
            return reserveClass(user, form, professorId, subjectId);
        }

        final LocalDate day = new LocalDate(form.getDay());

        final LocalDateTime startTime = new LocalDateTime(day.getYear(), day.getMonthOfYear(),
                day.getDayOfMonth(), form.getStartHour(), 0);

        final LocalDateTime endTime = new LocalDateTime(day.getYear(), day.getMonthOfYear(),
                day.getDayOfMonth(), form.getEndHour(), 0);

        if(!startTime.isAfter(LocalDateTime.now())) {
            errors.rejectValue("day", "futureDateError");
            return reserveClass(user, form, professorId, subjectId);
        }

        final ClassReservation reservation;
        try {
            reservation = classReservationService.reserve(startTime, endTime,
                    professorId, subjectId, user.getId());
        } catch (SameUserException e) {
            return redirectToErrorPage("sameUserReservation");
        } catch (NonexistentCourseException e) {
            return redirectToErrorPage("nonExistentCourse");
        } catch (NonExistentUserException e) {
            return redirectToErrorPage("nonExistentUser");
        } catch (ReservationTimeOutOfRange reservationTimeOutOfRange) {
            errors.rejectValue("day", "notAvailableTime");
            return reserveClass(user, form, professorId, subjectId);
        }

        if(reservation == null) {
            return reserveClass(user, form, professorId, subjectId);
        }

        return redirectWithNoExposedModalAttributes("/reservations");
    }


    @RequestMapping("/denyClassRequest")
    public ModelAndView denyClassRequest(@ModelAttribute("currentUser") final User currentUser,
                                         @RequestParam("classReservation") final long classReservationId) {

        final ClassReservation classReservation;
        try {
            classReservation = classReservationService.deny(classReservationId, currentUser.getId(), null);
        } catch (UserAuthenticationException e) {
            return redirectToErrorPage("403");
        }

        if(classReservation == null) {
            return redirectToErrorPage("nonExistentClassReservation");
        }

        return redirectWithNoExposedModalAttributes("/classRequests");
    }

    @RequestMapping("/approveClassRequest")
    public ModelAndView approveClassRequest(@ModelAttribute("currentUser") final User currentUser,
                                            @RequestParam("classReservation") final long classReservationId) {

        final ClassReservation classReservation;
        try {
            classReservation = classReservationService.confirm(classReservationId, currentUser.getId(), null);
        } catch (UserAuthenticationException e) {
            return redirectToErrorPage("403");
        }

        if(classReservation == null) {
            return redirectToErrorPage("nonExistentClassReservation");
        }

        return redirectWithNoExposedModalAttributes("/classRequests");
    }


    @InitBinder
    private void dateBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        CustomDateEditor editor = new CustomDateEditor(dateFormat, true);
        binder.registerCustomEditor(Date.class, editor);
    }

    @RequestMapping("/modifyCourse")
    public ModelAndView modifyCourse(@ModelAttribute("modifyForm") final CourseForm form,
                                     @ModelAttribute("currentUser") final User user,
                                     @RequestParam("subject") final long subjectId) {
        final ModelAndView mav = new ModelAndView("modifyCourse");

        final Course course = courseService.findCourseByIds(user.getId(), subjectId);

        if(course == null) {
            return redirectToErrorPage("nonExistentCourse");
        }

        form.setDescription(course.getDescription());
        form.setPrice(course.getPrice());
        form.setSubjectId(course.getSubject().getId());
        return mav;
    }

    @RequestMapping(value = "/modifyCourse", method = RequestMethod.POST)
    public ModelAndView modify(@Valid @ModelAttribute("modifyForm") final CourseForm form,
                               final BindingResult errors,
                               @ModelAttribute("currentUser") final User user) {
        if(errors.hasErrors()) {
            return modifyCourse(form, user, form.getSubjectId());
        }

        final Course course;
        try {
            course = courseService.modify(user.getId(), form.getSubjectId(), form.getDescription(), form.getPrice());
        } catch (NonexistentCourseException e) {
            return redirectToErrorPage("nonExistentCourse");
        }

        if(course == null) {
            return modifyCourse(form, user, form.getSubjectId());
        }

        LOGGER.debug("Posting request for course modification for professor with id {} in subject with id {}", user.getId(), form.getSubjectId());
        return redirectWithNoExposedModalAttributes("/Course/?professor=" + course.getProfessor().getId()
                + "&subject=" + course.getSubject().getId());
    }

}
