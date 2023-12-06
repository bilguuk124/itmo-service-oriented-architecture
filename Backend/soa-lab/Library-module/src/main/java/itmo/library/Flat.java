package itmo.library;


import itmo.library.adapters.LocalDateXmlAdapter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "flat")
@XmlRootElement(name = "flat")
@XmlType(propOrder = {"name","coordinates","creationDate","area","numberOfRooms","furnish", "view","transport","house","price","hasBalcony"})
@XmlAccessorType(XmlAccessType.FIELD)
public class Flat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlAttribute
    private Integer id;

    @NotNull
    @NotBlank
    @XmlElement
    private String name;

    @Embedded
    @XmlElement
    @NotNull
    private Coordinates coordinates;

    @NotNull
    @XmlElement
    @XmlJavaTypeAdapter(LocalDateXmlAdapter.class)
    private LocalDate creationDate;

    @Min(1)
    @XmlElement
    @Max(527)
    private int area;

    @XmlElement
    @Min(1)
    private long numberOfRooms;

    @XmlElement
    @Enumerated(EnumType.STRING)
    @NotNull
    private Furnish furnish;

    @XmlElement
    @Enumerated(EnumType.STRING)
    @NotNull
    private View view;

    @Enumerated(EnumType.STRING)
    @NotNull
    @XmlElement
    private Transport transport;

    @ManyToOne
    @JoinColumn(name = "house_name")
    @XmlElement
    @NotNull
    private House house;

    @XmlElement
    @Min(1)
    private Long price;

    @XmlElement
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
