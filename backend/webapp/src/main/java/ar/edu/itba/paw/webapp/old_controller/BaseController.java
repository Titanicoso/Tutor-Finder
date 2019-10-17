package ar.edu.itba.paw.webapp.old_controller;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class BaseController {

    public ModelAndView redirectToErrorPage(final String errorCode) {
        final ModelAndView error = new ModelAndView("error");
        error.addObject("errorMessageCode",errorCode);
        return error;
    }

    public ModelAndView redirectWithNoExposedModalAttributes(final String url) {
        final RedirectView view = new RedirectView(url, true);
        view.setExposeModelAttributes(false);
        return new ModelAndView(view);
    }
}
