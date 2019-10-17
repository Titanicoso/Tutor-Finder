package ar.edu.itba.paw.webapp.old_controller;

import ar.edu.itba.paw.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;


@ControllerAdvice
@Controller
public class ErrorController extends BaseController{

    private static final Logger LOGGER = LoggerFactory.getLogger(AreaController.class);

    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(Exception e) {
        LOGGER.debug("Handling exception {}", e.getCause());
        return redirectToErrorPage("somethingWentWrong");
    }

    @ExceptionHandler({UserNotInConversationException.class, NonexistentConversationException.class})
    public ModelAndView handleConversationExceptions(Exception e) {
        final String messageCode;
        if(e instanceof UserNotInConversationException) {
            messageCode = "userNotInConversation";
        } else {
            messageCode = "nonExistentConversation";
        }
        return redirectToErrorPage(messageCode);
    }

    @ExceptionHandler({NonexistentProfessorException.class, ProfessorWithoutUserException.class})
    public ModelAndView handleNonexistentUser() {
        return redirectToErrorPage("nonExistentUser");
    }

    @RequestMapping("/403")
    public ModelAndView forbidden() {
        return redirectToErrorPage("403");
    }

    @RequestMapping("/404")
    public ModelAndView resourceNotFound() {
        return redirectToErrorPage("404");
    }

    @RequestMapping("/500")
    public ModelAndView internalError() {
        return redirectToErrorPage("500");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView handleFileSizeException() {
        final ModelAndView mav = new ModelAndView("registerAsProfessorForm");
        return mav;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ModelAndView handleMissingParameterException() {
        return redirectToErrorPage("missingParameter");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ModelAndView handleargumentTypeException() {
        return redirectToErrorPage("invalidArgument");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ModelAndView handleMethodNotSupportedException() {
        return redirectToErrorPage("methodNotSupported");
    }


    @ExceptionHandler(DownloadFileException.class)
    public ModelAndView handleDownloadFileException() {
        return redirectToErrorPage("downloadFile");
    }
}
