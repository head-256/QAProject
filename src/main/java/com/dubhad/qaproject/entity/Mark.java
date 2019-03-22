package com.dubhad.qaproject.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class Mark extends Entity {
    private boolean value;
    private long userId;
    private long answerId;
}
