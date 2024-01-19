package com.itmo.feignclient.entity;

import com.itmo.feignclient.entity.Coordinates;
import com.itmo.feignclient.entity.House;
import javax.persistence.Embedded;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@XmlRootElement(name = "newFlatRequest")
@XmlAccessorType(XmlAccessType.FIELD)
public class FlatCreateDTO {
    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @Embedded
    private Coordinates coordinates;

    @Min(1)
    @Max(527)
    private Integer area;

    @Min(1)
    private Integer numberOfRooms;

    @NotNull
    private String furnish;

    @NotNull
    private String view;

    @NotNull
    private String transport;

    @NotNull
    private House house;

    @Min(1)
    private Long price;

    @NotNull
    private String hasBalcony;

}
