package ar.edu.itba.paw.webapp.old_controller;

import ar.edu.itba.paw.exceptions.NonexistentConversationException;
import ar.edu.itba.paw.exceptions.UserNotInConversationException;
import ar.edu.itba.paw.interfaces.service.ConversationService;
import ar.edu.itba.paw.models.Conversation;
import ar.edu.itba.paw.models.PagedResults;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.form.MessageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;


@Controller
public class ConversationController extends BaseController{

    @Autowired
    private ConversationService conversationService;

    @RequestMapping("/Conversations")
    public ModelAndView conversations(@ModelAttribute("currentUser") final User loggedUser,
                                      @RequestParam(value="page", defaultValue="1") final int page) {
        final ModelAndView mav = new ModelAndView("conversations");
        final PagedResults<Conversation> conversations = conversationService.findByUserId(loggedUser.getId(), page);

        if(conversations == null) {
            redirectToErrorPage("pageOutOfBounds");
        }

        mav.addObject("conversations", conversations);
        mav.addObject("page", page);
        return mav;
    }

    @RequestMapping("/Conversation")
    public ModelAndView conversation(@RequestParam(value="id", required=true) final long id,
                                     @ModelAttribute("messageForm") final MessageForm form,
                                     @ModelAttribute("currentUser") final User loggedUser)
            throws UserNotInConversationException {

        final ModelAndView mav = new ModelAndView("conversation");
        final Conversation conversation = conversationService.findById(id, loggedUser.getId());

        if(conversation == null) {
            redirectToErrorPage("nonExistentConversation");
        }
        final Conversation ret = conversationService.initializeMessages(conversation);

        mav.addObject("conversation", ret);
        form.setConversationId(id);
        return mav;
    }

    @RequestMapping(value = "/Conversation", method = RequestMethod.POST)
    public ModelAndView sendMessage(@Valid @ModelAttribute("messageForm") final MessageForm form,
                                     final BindingResult errors,
                                     @ModelAttribute("currentUser") final User loggedUser)
            throws UserNotInConversationException, NonexistentConversationException {

        if(errors.hasErrors()) {
            return conversation(form.getConversationId(), form, loggedUser);
        }

        final boolean sent = conversationService.sendMessage(loggedUser.getId(), form.getConversationId(), form.getBody());
        if(sent) {
            return redirectWithNoExposedModalAttributes("/Conversation?id=" + form.getConversationId());
        }
        errors.rejectValue("body", "SendMessageError");
        return conversation(form.getConversationId(), form, loggedUser);
    }

}
