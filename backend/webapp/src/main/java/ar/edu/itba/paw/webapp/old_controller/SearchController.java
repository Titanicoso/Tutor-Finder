package ar.edu.itba.paw.webapp.old_controller;

import ar.edu.itba.paw.interfaces.service.AreaService;
import ar.edu.itba.paw.interfaces.service.CourseService;
import ar.edu.itba.paw.interfaces.service.ProfessorService;
import ar.edu.itba.paw.models.Area;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.PagedResults;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.webapp.form.SearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.HashMap;

@Controller
public class SearchController extends BaseController{

    @Autowired
    @Qualifier("courseServiceImpl")
    private CourseService cs;

    @Autowired
    @Qualifier("professorServiceImpl")
    private ProfessorService ps;

    @Autowired
    @Qualifier("areaServiceImpl")
    private AreaService as;

    @RequestMapping(value = "/searchResults", method = RequestMethod.GET)
    public ModelAndView search(@RequestParam(value = "page", defaultValue = "1") final int page,
                               @ModelAttribute("searchForm") final SearchForm form) {

        final ModelAndView mav = new ModelAndView("searchResults");
        mav.addObject("page", page);

        switch (form.getType()) {
            case "professor":
                final PagedResults<Professor> professorPagedResults = ps.filterByFullName(form.getSearch(), page);

                if(professorPagedResults == null) {
                    redirectToErrorPage("pageOutOfBounds");
                }

                mav.addObject("pagedResults", professorPagedResults);
                break;
            case "course":
                final PagedResults<Course> coursePagedResults = cs.filterCourses(
                        form.getDays(), form.getStartHour(), form.getEndHour(),
                        form.getMinPrice(), form.getMaxPrice(), form.getSearch(), page);

                if(coursePagedResults == null) {
                    redirectToErrorPage("pageOutOfBounds");
                }
                mav.addObject("pagedResults", coursePagedResults);
                break;
            case "area":
                final PagedResults<Area> areaPagedResults = as.filterAreasByName(form.getSearch(), page);
                if(areaPagedResults == null) {
                    redirectToErrorPage("pageOutOfBounds");
                }
                mav.addObject("pagedResults", areaPagedResults);
                break;
            default:
                return redirectToErrorPage("typeInvalid");
        }
        mav.addObject("search", form.getSearch());
        mav.addObject("type", form.getType());
        return mav;
    }

    @RequestMapping(value = "/searchResults", method = RequestMethod.POST)
    public ModelAndView search(@RequestParam(value = "page", defaultValue = "1")final int page,
                               @Valid @ModelAttribute("searchForm") final SearchForm form,
                               final BindingResult errors,
                               final RedirectAttributes redirectAttributes) {

        if(errors.hasErrors() || !form.validPriceRange() || !form.validTimeRange()) {
            if(!form.validPriceRange()){
                errors.rejectValue("maxPrice", "invalidPriceRange");
            }
            if(!form.validTimeRange()){
                errors.rejectValue("endHour", "profile.add_schedule.timeError");
            }
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.searchForm", errors);
            redirectAttributes.addFlashAttribute("searchForm", form);
        } else {
            if(form.getType().equals("course")){
                redirectAttributes
                        .addAttribute("startHour", form.getStartHour())
                        .addAttribute("endHour", form.getEndHour())
                        .addAttribute("minPrice", form.getMinPrice())
                        .addAttribute("maxPrice", form.getMaxPrice());

                ((HashMap<String, Object>) redirectAttributes).putIfAbsent("days",
                        form.getDays());
            }
            redirectAttributes
                    .addAttribute("search", form.getSearch())
                    .addAttribute("page", page)
                    .addAttribute("type", form.getType());
        }
        RedirectView redirect = new RedirectView("/searchResults", true);
        return new ModelAndView(redirect);
    }
}
