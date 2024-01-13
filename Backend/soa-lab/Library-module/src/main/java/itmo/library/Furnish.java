package itmo.library;

import jakarta.xml.bind.annotation.XmlEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@Getter
@XmlEnum
@RequiredArgsConstructor
public enum Furnish {
    NONE("none"),
    FINE("fine"),
    BAD("bad"),
    LITTLE("little");

    private final String value;

    public static Furnish fromValue(String value){
        return Arrays.stream(Furnish.values())
                .filter(e -> e.getValue().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public static boolean isValid(String value){
        return Arrays.stream(Furnish.values())
                .anyMatch(e -> e.getValue().equalsIgnoreCase(value));
    }
}
