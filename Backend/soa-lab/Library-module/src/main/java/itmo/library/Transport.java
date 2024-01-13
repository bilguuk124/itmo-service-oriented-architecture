package itmo.library;

import jakarta.xml.bind.annotation.XmlEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@XmlEnum
@Getter
@RequiredArgsConstructor
public enum Transport {
    FEW("few"),
    NONE("none"),
    LITTLE("little"),
    NORMAL("normal"),
    ENOUGH("enough");

    private final String value;

    public static Transport fromValue(String value){
        return Arrays.stream(Transport.values())
                .filter(e -> e.getValue().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
    public static boolean isValid(String value){
        return Arrays.stream(Transport.values())
                .anyMatch(e -> e.getValue().equalsIgnoreCase(value));
    }
}
