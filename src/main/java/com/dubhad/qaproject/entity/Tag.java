package com.dubhad.qaproject.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Tag extends Entity {
    private long id;
    private String text;

}
