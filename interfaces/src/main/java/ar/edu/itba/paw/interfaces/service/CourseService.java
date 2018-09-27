package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.exceptions.CourseAlreadyExistsException;
import ar.edu.itba.paw.exceptions.PageOutOfBoundsException;
import ar.edu.itba.paw.models.Course;
import ar.edu.itba.paw.models.PagedResults;
import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Subject;

import java.util.List;

public interface CourseService {
    Course findCourseByIds(final long professor_id, final long subject_id);

    PagedResults<Course> findCourseByProfessorId(final long professor_id, final int page) throws PageOutOfBoundsException;

    PagedResults<Course> filterCoursesByName(String name, final int page) throws PageOutOfBoundsException;

    PagedResults<Course> filterByAreaId(final long areaId, final int page) throws PageOutOfBoundsException;

    Course create(Professor professor, Subject subject, String description, Double price) throws CourseAlreadyExistsException;
}
