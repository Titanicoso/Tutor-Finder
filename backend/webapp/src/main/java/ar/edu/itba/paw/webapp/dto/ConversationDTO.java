package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Conversation;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;

//TODO: Check latestMessage type
@XmlRootElement
public class ConversationDTO {

    private long id;
    private UserDTO user;
    private ProfessorDTO professor;
    private SubjectDTO subject;

    @XmlElement(name = "messages_url")
    private URI messagesUrl;

    @XmlElement(name = "latest_message")
    private String latestMessage;

    public ConversationDTO() {
    }

    public ConversationDTO(final Conversation conversation, final UriInfo uriInfo) {
        this.id = conversation.getId();
        this.subject = new SubjectDTO(conversation.getSubject(), uriInfo.getBaseUri());
        this.latestMessage = conversation.getLatestMessage().toString();
        this.messagesUrl = uriInfo.getBaseUri().resolve("conversations/" + id +"/messages");
        this.user = new UserDTO(conversation.getUser(), uriInfo.getBaseUri(), false);
        this.professor = new ProfessorDTO(conversation.getProfessor(), uriInfo);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public URI getMessagesUrl() {
        return messagesUrl;
    }

    public void setMessagesUrl(URI messagesUrl) {
        this.messagesUrl = messagesUrl;
    }

    public String getLatestMessage() {
        return latestMessage;
    }

    public void setLatestMessage(String latestMessage) {
        this.latestMessage = latestMessage;
    }

    public SubjectDTO getSubject() {
        return subject;
    }

    public void setSubject(SubjectDTO subject) {
        this.subject = subject;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public ProfessorDTO getProfessor() {
        return professor;
    }

    public void setProfessor(ProfessorDTO professor) {
        this.professor = professor;
    }
}
