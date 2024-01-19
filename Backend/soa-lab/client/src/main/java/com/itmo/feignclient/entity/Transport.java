package com.itmo.feignclient.entity;

import javax.xml.bind.annotation.XmlEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@XmlEnum
@Getter
@RequiredArgsConstructor
public enum Transport {
    NORMAL("normal"),
    FEW("few"),
    ENOUGH("enough"),
    LITTLE("little"),
    NONE("none");

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
