package itmo.library;

import jakarta.xml.bind.annotation.XmlEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@XmlEnum
@Getter
@RequiredArgsConstructor
public enum View{
    YARD("yard"),
    NORMAL("normal"),
    STREET("street"),
    BAD("bad"),
    TERRIBLE("terrible");

    private final String value;
    public static View fromValue(String value){
        return Arrays.stream(View.values())
                .filter(e -> e.getValue().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
    public static boolean isValid(String value){
        return Arrays.stream(View.values())
                .anyMatch(e -> e.getValue().equalsIgnoreCase(value));
    }
}
