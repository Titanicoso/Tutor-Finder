package ar.edu.itba.paw.models;

import org.hibernate.annotations.Formula;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "conversations")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "conversations_conversation_id_seq")
    @SequenceGenerator(sequenceName = "conversations_conversation_id_seq",
            name = "conversations_conversation_id_seq",  allocationSize = 1)
    @Column(name = "conversation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="user_id", foreignKey = @ForeignKey(name = "conversations_user_id_fkey"))
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="professor_id", foreignKey = @ForeignKey(name = "conversations_professor_id_fkey"))
    private Professor professor;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name="subject_id", foreignKey = @ForeignKey(name = "conversations_subject_id_fkey"))
    private Subject subject;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "conversation")
    @OrderBy("created ASC")
    private List<Message> messages;

    @Formula("(SELECT max(m.created) FROM messages m WHERE m.conversation_id = conversation_id GROUP BY conversation_id)")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime latestMessage;

    Conversation() {}

    public Conversation(final User user, final Professor professor, final Subject subject, LocalDateTime latestMessage) {
        this.user = user;
        this.professor = professor;
        this.subject = subject;
        this.latestMessage = latestMessage;
    }

    public Conversation(final Long id, final User user, final Professor professor, final Subject subject, LocalDateTime latestMessage) {
        this.id = id;
        this.user = user;
        this.professor = professor;
        this.subject = subject;
        this.latestMessage = latestMessage;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Professor getProfessor() {
        return professor;
    }

    public Subject getSubject() {
        return subject;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public LocalDateTime getLatestMessage() {
        return latestMessage;
    }

    public void addMessages(List<Message> messages) {
        this.messages.addAll(messages);
    }

    public boolean belongs(Long id) {return (user.getId().equals(id) || professor.getId().equals(id)); }

    public String getHours() {
        return String.format("%02d", latestMessage.getHourOfDay());
    }

    public String getMinutes() {
        return String.format("%02d", latestMessage.getMinuteOfHour());
    }

    public String getDay() {
        return String.format("%02d", latestMessage.getDayOfMonth());
    }

    public String getMonth() {
        return String.format("%02d", latestMessage.getMonthOfYear());
    }

    public int getYear() {
        return latestMessage.getYear();
    }
}
