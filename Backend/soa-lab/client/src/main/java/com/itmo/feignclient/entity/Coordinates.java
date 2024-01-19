package com.itmo.feignclient.entity;

import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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
