package itmo.library;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class Coordinates implements Serializable {

    @Max(548)
    @NotNull
    @XmlElement(name = "coordinate_x")
    private Double x;

    @XmlElement(name = "coordinate_y")
    @NotNull
    private Integer y;
}
