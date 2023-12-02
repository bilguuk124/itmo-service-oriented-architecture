package itmo.soa.library;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "house")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "house")
public class House {
    @Id
    @NotEmpty
    private String name;

    @Min(1)
    @Max(634)
    private Integer year;

    @Min(1)
    private Integer numberOfFloors;

    public void update(House house){
        this.name = house.getName();
        this.year = house.getYear();
        this.numberOfFloors = house.getNumberOfFloors();
    }

}
