package com.itmo.mainspring.entity;

import com.itmo.feignclient.entity.Flat;
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
@ToString
public class FlatPageableResponse {
    @XmlElementWrapper(name = "data")
    @XmlElement(name = "flat")
    private List<Flat> data;
    @XmlElement(name = "numberOfEntries")
    private Long numberOfEntries;
}
