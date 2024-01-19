package com.itmo.mainspring.entity;

import com.itmo.feignclient.entity.House;
import javax.xml.bind.annotation.*;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "PageableResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class HousePageableResponse{
    @XmlElementWrapper(name = "data")
    @XmlElement(name = "house")
    private List<House> data;
    @XmlElement(name = "numberOfEntries")
    private Long numberOfEntries;
}
