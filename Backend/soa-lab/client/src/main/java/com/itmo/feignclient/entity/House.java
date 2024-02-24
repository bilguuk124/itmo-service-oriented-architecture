package com.itmo.feignclient.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.*;

@Entity
@Table(name = "house")
@Setter
@Getter
@AllArgsConstructor
@Builder
@ToString
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
    @Column(name = "_year")
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
