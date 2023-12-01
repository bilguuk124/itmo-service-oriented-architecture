package itmo.soa.library;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlEnum
public enum Furnish {
    NONE,
    FINE,
    BAD,
    LITTLE
}
