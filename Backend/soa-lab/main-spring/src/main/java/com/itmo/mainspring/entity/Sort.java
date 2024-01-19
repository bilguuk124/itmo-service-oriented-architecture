package com.itmo.mainspring.entity;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Sort {
    private boolean desc;
    private String fieldName;
    private String nestedName;
}
