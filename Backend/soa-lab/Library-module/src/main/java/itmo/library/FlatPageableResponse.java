package itmo.library;

import jakarta.xml.bind.annotation.*;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "PageableResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class FlatPageableResponse {
    @XmlElementWrapper(name = "data")
    @XmlElement(name = "flat")
    private List<Flat> data;
    @XmlElement(name = "numberOfEntries")
    private Long numberOfEntries;
}
