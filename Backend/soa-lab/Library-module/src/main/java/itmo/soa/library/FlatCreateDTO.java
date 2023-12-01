package itmo.soa.library;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlatCreateDTO {
    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @XmlElementWrapper(name = "coordinates")
    private Coordinates coordinates;

    @NotNull
    private LocalDate creationDate;

    @Min(1)
    @Max(527)
    private int area;

    @Min(1)
    private long numberOfRooms;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Furnish furnish;

    @Enumerated(EnumType.STRING)
    @NotNull
    private View view;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Transport transport;

    @NotNull
    private House house;

    @Min(1)
    private Long price;

    @NotNull
    private Boolean hasBalcony;
}
