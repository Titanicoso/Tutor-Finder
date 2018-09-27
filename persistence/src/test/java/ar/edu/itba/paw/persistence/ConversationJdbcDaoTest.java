package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Sql("classpath:schema.sql")
public class ConversationJdbcDaoTest {

    private static final Long SUBJECT_ID = 1L;
    private static final Long PROFESSOR_ID = 2L;
    private static final Long USER_ID = 3L;
    private static final Long SENDER_2 = 2L;
    private static final Long SENDER_1 = 3L;
    private static final String BODY_2 = "Hola";
    private static final String BODY_1 = "Hola2";
    private static final Long CONVERSATION_ID = 1L;
    private static final Long INVALID_ID = 666L;
    private static final int CONVERSATION_NUMBER = 2;
    private static final Integer LIMIT = 10;
    private static final Integer OFFSET = 0;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ConversationJdbcDao conversationDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp(){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    public void testConversationCreateValid() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "conversations");
        Professor mockProfessor = mock(Professor.class);
        User mockUser = mock(User.class);
        Subject mockSubject = mock(Subject.class);
        when(mockProfessor.getId()).thenReturn(PROFESSOR_ID);
        when(mockUser.getId()).thenReturn(USER_ID);
        when(mockSubject.getId()).thenReturn(SUBJECT_ID);

        final Conversation conversation = conversationDao.create(mockUser, mockProfessor, mockSubject);

        assertNotNull(conversation);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "conversations",
                "user_id = " + USER_ID + " AND professor_id = " + PROFESSOR_ID +
                        " AND subject_id = " + SUBJECT_ID));

    }

    @Test
    public void testMessageCreateValid() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "messages");
        Conversation mockConversation = mock(Conversation.class);
        User mockUser = mock(User.class);
        when(mockConversation.getId()).thenReturn(CONVERSATION_ID);
        when(mockUser.getId()).thenReturn(USER_ID);

        final Message message = conversationDao.create(mockUser, BODY_1, mockConversation);

        assertNotNull(message);
        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "messages"));
    }


    @Test
    public void testFindByIdValid() {

        final Conversation conversation = conversationDao.findById(CONVERSATION_ID);

        assertNotNull(conversation);
        assertEquals(CONVERSATION_ID, conversation.getId());
        assertEquals(SUBJECT_ID, conversation.getSubject().getId());
        assertEquals(PROFESSOR_ID, conversation.getProfessor().getId());
        assertEquals(USER_ID, conversation.getUser().getId());

        final Message message1 = conversation.getMessages().get(0);
        final Message message2 = conversation.getMessages().get(1);

        assertEquals(BODY_1, message1.getText());
        assertEquals(SENDER_1, message1.getSender().getId());
        assertEquals(BODY_2, message2.getText());
        assertEquals(SENDER_2, message2.getSender().getId());
    }

    @Test
    public void testFindByIdInvalid() {
        final Conversation conversation = conversationDao.findById(INVALID_ID);
        assertNull(conversation);
    }

    @Test
    public void testFindByUserIdValid() {

        final List<Conversation> conversations = conversationDao.findByUserId(PROFESSOR_ID, LIMIT, OFFSET);

        assertNotNull(conversations);
        assertEquals(CONVERSATION_NUMBER, conversations.size());

        assertEquals(CONVERSATION_ID, conversations.get(0).getId());
    }

    @Test
    public void testFindByUserIdInvalid() {
        final List<Conversation> conversations = conversationDao.findByUserId(INVALID_ID, LIMIT, OFFSET);
        assertNotNull(conversations);
        assertEquals(0, conversations.size());
    }

    @Test
    public void testFindByIdsValid() {

        final Conversation conversation = conversationDao.findByIds(USER_ID, PROFESSOR_ID, SUBJECT_ID);

        assertNotNull(conversation);
        assertEquals(CONVERSATION_ID, conversation.getId());
        assertEquals(SUBJECT_ID, conversation.getSubject().getId());
        assertEquals(PROFESSOR_ID, conversation.getProfessor().getId());
        assertEquals(USER_ID, conversation.getUser().getId());
    }

    @Test
    public void testFindByIdsInvalid() {
        final Conversation conversation = conversationDao.findByIds(INVALID_ID, INVALID_ID, INVALID_ID);
        assertNull(conversation);
    }

    @After
    public void tearDown(){
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "areas");
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
    }
}
