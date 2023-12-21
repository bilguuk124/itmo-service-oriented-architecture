package itmo.library;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "house")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "house")
@XmlAccessorType(XmlAccessType.FIELD)
public class House{
    @Id
    @NotEmpty
    @XmlElement(name = "name")
    private String name;

    @Min(1)
    @Max(634)
    @Column(name = "yeer")
    private Integer year;

    @Min(1)
    @XmlElement(name = "numberOfFloors")
    private Integer numberOfFloors;

    public House (House house){
        this.name = house.getName();
        this.numberOfFloors = house.getNumberOfFloors();
        this.year = house.getYear();
    }

    public void update(Integer year, Integer numberOfFloors){
        this.year = year;
        this.numberOfFloors = numberOfFloors;
    }
}
