package itmo.library;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum Transport {
    FEW,
    NONE,
    LITTLE,
    NORMAL,
    ENOUGH
}
