package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Message;
import ar.edu.itba.paw.webapp.utils.LocalDateTimeXmlAdapter;
import org.joda.time.LocalDateTime;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.net.URI;

@XmlRootElement
public class MessageDTO {

    private long id;
    private UserDTO sender;
    private String text;

    @XmlJavaTypeAdapter(type = LocalDateTime.class, value = LocalDateTimeXmlAdapter.class)
    private LocalDateTime created;

    @XmlElement(name = "conversation_url")
    private URI conversationUrl;
    private URI url;

    public MessageDTO() {
    }

    public MessageDTO(final Message message, final URI baseUri) {
        this.id = message.getId();
        this.created = message.getCreated();
        this.text = message.getText();
        this.sender = new UserDTO(message.getSender(), baseUri, false);

        this.conversationUrl = baseUri.resolve("conversations/" + message.getConversation().getId());
        this.url = baseUri.resolve(this.conversationUrl + "/messages");
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public URI getConversationUrl() {
        return conversationUrl;
    }

    public void setConversationUrl(URI conversationUrl) {
        this.conversationUrl = conversationUrl;
    }

    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }

    public UserDTO getSender() {
        return sender;
    }

    public void setSender(UserDTO sender) {
        this.sender = sender;
    }
}
