package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Professor;
import ar.edu.itba.paw.models.Schedule;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@XmlRootElement
public class ScheduleDTO {

    private ProfessorDTO professor;

    private List<RangeListDTO> days;

    private URI url;

    public ScheduleDTO() {
    }

    public ScheduleDTO(final Professor professor, final Schedule schedule, final UriInfo uriInfo) {
        final URI baseUri = uriInfo.getBaseUri();

        this.url = baseUri.resolve("professors/" + professor.getUsername() + "/schedule");
        this.professor = new ProfessorDTO(professor, uriInfo);

        this.days = schedule.getDays().entrySet().stream()
                .map(e -> new RangeListDTO(e.getKey(), e.getValue().stream()
                        .map(t -> new TimeRangeDTO(t.getStartHour(), t.getEndHour()))
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    public ProfessorDTO getProfessor() {
        return professor;
    }

    public void setProfessor(ProfessorDTO professor) {
        this.professor = professor;
    }

    public URI getUrl() {
        return url;
    }

    public void setUrl(URI url) {
        this.url = url;
    }

    public List<RangeListDTO> getDays() {
        return days;
    }

    public void setDays(List<RangeListDTO> days) {
        this.days = days;
    }
}
