package com.itmo.feignclient.entity;

import com.itmo.feignclient.entity.adapters.LocalDateTimeXmlAdapter;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Setter
@Getter
public class ErrorBody {
    @XmlElement
    private int errorCode;

    @XmlElement
    private String message;

    @XmlElement
    private String details;

    @XmlElement
    @XmlJavaTypeAdapter(LocalDateTimeXmlAdapter.class)
    private LocalDateTime timestamp;
}
