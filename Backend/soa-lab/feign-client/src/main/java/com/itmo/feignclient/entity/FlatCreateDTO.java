package com.itmo.feignclient.entity;

import com.itmo.feignclient.entity.Coordinates;
import com.itmo.feignclient.entity.House;
import jakarta.persistence.Embedded;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
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
