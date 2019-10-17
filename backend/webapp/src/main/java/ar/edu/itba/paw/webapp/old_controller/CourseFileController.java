package ar.edu.itba.paw.webapp.old_controller;

import ar.edu.itba.paw.exceptions.DownloadFileException;
import ar.edu.itba.paw.exceptions.UserAuthenticationException;
import ar.edu.itba.paw.interfaces.service.CourseFileService;
import ar.edu.itba.paw.interfaces.service.CourseService;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.CourseFile;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.form.UploadClassFileForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
public class CourseFileController extends BaseController {

    @Autowired
    CourseFileService cfs;

    @Autowired
    CourseService cs;

    @RequestMapping(value = "/courseFiles", method = RequestMethod.GET)
    public ModelAndView getCourseFiles(@RequestParam("professor") final long professorId,
                               @RequestParam("subject") final long subjectId,
                               @ModelAttribute("currentUser") final User currentUser,
                               @ModelAttribute("uploadClassFileForm") final UploadClassFileForm form) {
        final Course course = cs.findCourseByIds(professorId, subjectId);
        if(course == null) {
            return  redirectToErrorPage("nonExistentCourse");
        }
        final List<CourseFile> courseFiles;
        try {
            courseFiles = cfs.findForCourse(course, currentUser);
        } catch (UserAuthenticationException e) {
            return redirectToErrorPage("403");
        }
        ModelAndView mav = new ModelAndView("courseFiles");
        mav.addObject("courseFiles", courseFiles);
        return mav;
    }


    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public void downloadFile(@RequestParam("courseFile")final long courseFileId,
                                     @ModelAttribute("currentUser") final User currentUser,
                                     HttpServletResponse response) throws DownloadFileException {

        final CourseFile courseFile;
        try {
            courseFile = cfs.findByIdForUser(courseFileId, currentUser);
        } catch (UserAuthenticationException e) {
            return;
        }

        //No file present
        if(courseFile == null) {
            return;
        }

        response.setContentType(courseFile.getType());
        response.setContentLength(courseFile.getContent().length);
        response.setHeader("Content-Disposition","attachment; filename=\"" + courseFile.getName() +"\"");

        try {
            FileCopyUtils.copy(courseFile.getContent(), response.getOutputStream());
        } catch (IOException e) {
            throw new DownloadFileException();
        }

    }


    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ModelAndView uploadFile(@Valid @ModelAttribute("uploadClassFileForm") final UploadClassFileForm form,
                                   BindingResult result,
                                   @RequestParam("professor") final long professorId,
                                   @RequestParam("subject") final long subjectId,
                                   @ModelAttribute("currentUser") final User currentUser) {

        CourseFile courseFile = null;
        if(result.hasErrors()) {
            return getCourseFiles(professorId, subjectId, currentUser, form);
        }

        try {
            courseFile = cfs.save(professorId, subjectId, currentUser, form.getFile().getOriginalFilename(),
                    form.getDescription(), form.getFile().getContentType(), form.getFile().getBytes());
        } catch (IOException e) {
            return redirectToErrorPage("oops");
        } catch (UserAuthenticationException e) {
            return redirectToErrorPage("403");
        }

        if(courseFile == null) {
            return getCourseFiles(professorId, subjectId, currentUser, form);
        }

        return redirectWithNoExposedModalAttributes("/courseFiles?professor=" + professorId
                +"&subject=" + subjectId);
    }

    @RequestMapping("/deleteFile")
    public ModelAndView deleteFile(@ModelAttribute("currentUser") final User currentUser,
                                   @RequestParam("professor") final long professorId,
                                   @RequestParam("subject") final long subjectId,
                                   @RequestParam("courseFile") final long fileId) {

        try {
            cfs.deleteById(fileId, currentUser);
        } catch (UserAuthenticationException e) {
            return redirectToErrorPage("403");
        }
        return redirectWithNoExposedModalAttributes("/courseFiles?professor=" + professorId
                +"&subject=" + subjectId);
    }
}
