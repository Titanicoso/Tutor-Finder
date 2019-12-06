package ar.edu.itba.paw.persistence.jdbc;

import ar.edu.itba.paw.exceptions.CourseAlreadyExistsException;
import ar.edu.itba.paw.interfaces.persistence.CourseDao;
import ar.edu.itba.paw.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.*;

public class CourseJdbcDao implements CourseDao {

    private final static Logger LOGGER = LoggerFactory.getLogger(CourseJdbcDao.class);

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    private static final String COURSES_SELECT_FROM = "SELECT courses.user_id, courses.subject_id," +
            "courses.description, price, professors.description, users.username," +
            "users.name, users.lastname, users.password, users.email, subjects.description," +
            "subjects.name, areas.name, areas.description, areas.area_id, professors.profile_picture," +
            "areas.image FROM courses, professors, users, subjects, areas ";
    
    private final static RowMapper<Course> ROW_MAPPER = (rs, rowNum) -> new Course(
            new Professor(
                    rs.getString(6),
                    rs.getString(7),
                    rs.getString(8),
                    rs.getString(9),
                    rs.getString(10),
                    rs.getString(5),
                    rs.getBytes(16)
                    ),
            new Subject(
                    rs.getLong(2),
                    rs.getString(11),
                    rs.getString(12),
                    new Area(
                            rs.getString(14),
                            rs.getString(13),
                            rs.getBytes(17)
                            )
                    ),
            rs.getString(3),
            rs.getDouble(4)
    );

    @Autowired
    public CourseJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("courses");
    }

    @Override
    public Optional<Course> findByIds(final long professor_id, final long subject_id) {
        LOGGER.trace("Querying for course with professor_id {} and subject_id {}", professor_id, subject_id);
        final List<Course> list = jdbcTemplate.query(
                COURSES_SELECT_FROM + "WHERE courses.user_id = ? AND courses.subject_id = ? " +
                        "AND professors.user_id = users.user_id AND areas.area_id = subjects.area_id " +
                        "AND users.user_id = courses.user_id AND courses.subject_id = subjects.subject_id"
                , ROW_MAPPER, professor_id, subject_id);
        return list.stream().findFirst();
    }

    @Override
    public List<Course> findByProfessorId(final long professor_id, final int limit, final int offset) {
        LOGGER.trace("Querying for courses belonging to a professor with id {}", professor_id);
        final List<Course> courses = jdbcTemplate.query(
                COURSES_SELECT_FROM + "WHERE courses.user_id = ? AND professors.user_id = users.user_id" +
                        " AND areas.area_id = subjects.area_id AND users.user_id = courses.user_id " +
                        "AND courses.subject_id = subjects.subject_id ORDER BY subjects.subject_id " +
                        " LIMIT ? OFFSET ?", ROW_MAPPER, professor_id, limit, offset
        );
        return courses;
    }

    @Override
    public long totalByProfessorId(Long professor_id) {
        return 0;
    }

    @Override
    public List<Course> filterByAreaId(final long areaId, final int limit, final int offset) {
        LOGGER.trace("Querying for courses from area with id {}", areaId);
        final List<Course> courses = jdbcTemplate.query(
                COURSES_SELECT_FROM + "WHERE subjects.area_id = ? AND professors.user_id = users.user_id" +
                        " AND areas.area_id = subjects.area_id AND users.user_id = courses.user_id" +
                        " AND courses.subject_id = subjects.subject_id " +
                        " ORDER BY courses.user_id, courses.subject_id LIMIT ? OFFSET ?"
                , ROW_MAPPER, areaId, limit, offset);
        return courses;
    }

    @Override
    public long totalByAreaId(Long areaId) {
        return 0;
    }

    @Override
    public List<Course> filter(List<Integer> days, Integer startHour, Integer endHour, Double minPrice, Double maxPrice, String searchText, int limit, int offset) {
        return null;
    }

    @Override
    public long totalByFilter(List<Integer> days, Integer startHour, Integer endHour, Double minPrice, Double maxPrice, String searchText) {
        return 0;
    }


    /*public List<Course> filter(Filter filter, int limit, int offset) {
        List<Object> params = filter.getQueryParams();
        params.add(limit);
        params.add(offset);
        final List<Course> courses = jdbcTemplate.query(
                filter.getQuery() + "ORDER BY courses.user_id, courses.subject_id LIMIT ? OFFSET ?",
                ROW_MAPPER, params.toArray()
        );

        return  courses;
    }*/

    @Override
    public Course create(final Professor professor, final Subject subject, final String description,
                         final Double price) throws CourseAlreadyExistsException {
        final Map<String, Object> args = new HashMap<>();
        args.put("user_id", professor.getId());
        args.put("subject_id", subject.getId());
        args.put("description", description);
        args.put("price", price);
        LOGGER.trace("Inserting course with user_id {} and subject_id {}", professor.getId(), subject.getId());
        try {
            jdbcInsert.execute(args);
        } catch (DuplicateKeyException e) {
            LOGGER.error("Course with user_id {} and subject_id {} already exists", professor.getId(), subject.getId());
            throw new CourseAlreadyExistsException();
        }
        return new Course(professor, subject, description, price);
    }

    @Override
    public Comment create(User creator, String text, Course course, int rating) {
        return null;
    }

    @Override
    public List<Comment> getComments(Course course, int limit, int offset) {
        return null;
    }

    @Override
    public long totalComments(Course course) {
        return 0;
    }

    @Override
    public boolean delete(Course course) {
        return false;
    }

    @Override
    public Course modify(Course course, String description, Double price) {
        return null;
    }
}