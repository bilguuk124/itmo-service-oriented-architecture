package itmo.library;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.*;
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
    private Coordinates coordinates;

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
