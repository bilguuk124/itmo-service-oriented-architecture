package com.itmo.feignclient.entity;

import javax.xml.bind.annotation.XmlEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@XmlEnum
@RequiredArgsConstructor
public enum Furnish {
    NONE("none"),
    LITTLE("little"),
    BAD("bad"),
    FINE("fine");

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
