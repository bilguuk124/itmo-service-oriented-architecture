package itmo.library;

import jakarta.xml.bind.annotation.XmlEnum;

@XmlEnum
public enum View {
    STREET,
    YARD,
    BAD,
    NORMAL,
    TERRIBLE
}
