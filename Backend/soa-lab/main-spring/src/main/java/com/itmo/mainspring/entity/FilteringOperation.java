package com.itmo.mainspring.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

@Getter
@RequiredArgsConstructor
public enum FilteringOperation {
    EQ("eq"),
    NEQ("neq"),
    GT("gt"),
    LT("lt"),
    GTE("gte"),
    LTE("lte"),
    UNDEFINED("undefined");

    private final String value;

    @Override
    public String toString(){
        return value;
    }

    public static FilteringOperation fromValue(String value){
        return Arrays.stream(FilteringOperation.values())
                .filter(e -> Objects.equals(e.getValue(), value))
                .findFirst()
                .orElse(UNDEFINED);
    }
}
