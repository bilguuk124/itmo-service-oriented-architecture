package itmo.soa.library;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "flat")
@XmlRootElement(name = "flat")
public class Flat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @NotBlank
    private String name;

    @Embedded
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

    @ManyToOne
    @JoinColumn(name = "house_id")
    @NotNull
    private House house;

    @Min(1)
    private Long price;

    @NotNull
    private Boolean hasBalcony;

    public void update(FlatCreateDTO dto){
        if (dto.getCoordinates() != null && !dto.getCoordinates().equals(coordinates)){
            setCoordinates(dto.getCoordinates());
        }
        if (dto.getName() != null && !dto.getName().equals(name)){
            setName(dto.getName());
        }
        if (dto.getArea() >= 1 && dto.getArea() <= 527 && area != dto.getArea()){
            setArea(dto.getArea());
        }
        if (dto.getNumberOfRooms() >= 1 && numberOfRooms != dto.getNumberOfRooms()){
            setNumberOfRooms(dto.getNumberOfRooms());
        }
        if (dto.getFurnish() != null && !dto.getFurnish().equals(furnish)){
            setFurnish(dto.getFurnish());
        }
        if (dto.getTransport() != null && !dto.getTransport().equals(transport)){
            setTransport(dto.getTransport());
        }
        if (dto.getView() != null && !dto.getView().equals(view)){
            setView(dto.getView());
        }
        if (dto.getPrice() != null && !dto.getPrice().equals(price)){
            setPrice(dto.getPrice());
        }
        if (dto.getHasBalcony() != null && !dto.getHasBalcony().equals(hasBalcony)){
            setHasBalcony(dto.getHasBalcony());
        }
        if (dto.getHouse() != null && !dto.getHouse().equals(house)){
            setHouse(dto.getHouse());
        }
    }

    public Flat(String name, Coordinates coordinates, int area, long numberOfRooms, Furnish furnish, View view, Transport transport, House house, Long price, boolean hasBalcony) {
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = LocalDate.now();
        this.area = area;
        this.numberOfRooms = numberOfRooms;
        this.furnish = furnish;
        this.view = view;
        this.transport = transport;
        this.house = house;
        this.price = price;
        this.hasBalcony = hasBalcony;
    }
}
