package ar.edu.itba.paw.interfaces.persistence;

import ar.edu.itba.paw.exceptions.CourseAlreadyExistsException;
import ar.edu.itba.paw.models.*;

import java.util.List;
import java.util.Optional;

public interface CourseDao {
    Optional<Course> findByIds(final long professor_id, final long subject_id);

    List<Course> findByProfessorId(final long professor_id, final int limit, final int offset);

    long totalByProfessorId(Long professor_id);

    List<Course> filterByAreaId(final long areaId, final int limit, final int offset);

    long totalByAreaId(Long areaId);

    List<Course>  filter(final List<Integer> days, final Integer startHour, final Integer endHour, final Double minPrice, final Double maxPrice, final String searchText, final int limit, final int offset);

    long totalByFilter(List<Integer> days, Integer startHour, Integer endHour,
                       Double minPrice, Double maxPrice, String searchText);

    Course create(final Professor professor, final Subject subject, final String description, final Double price) throws CourseAlreadyExistsException;

    Comment create(final User creator, final String text, final Course course, final int rating);

    List<Comment> getComments(final Course course, final int limit, final int offset);

    long totalComments(Course course);

    boolean delete(Course course);

    Course modify(Course course, String description, Double price);
}
