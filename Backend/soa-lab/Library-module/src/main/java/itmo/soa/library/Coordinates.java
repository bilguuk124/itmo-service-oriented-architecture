package itmo.soa.library;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class Coordinates implements Serializable {

    @Max(548)
    @NotNull
    private double x;
    @NotNull
    private Integer y;
}
