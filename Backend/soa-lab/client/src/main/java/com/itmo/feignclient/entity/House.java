package com.itmo.feignclient.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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
