package com.itmo.mainspring.entity;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Filter {
    private String fieldName;
    private String nestedName;
    private FilteringOperation filteringOperation;
    private String fieldValue;
}
