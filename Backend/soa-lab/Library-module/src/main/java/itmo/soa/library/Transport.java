package itmo.soa.library;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlEnum
public enum Transport {
    FEW,
    NONE,
    LITTLE,
    NORMAL,
    ENOUGH
}
