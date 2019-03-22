package com.dubhad.qaproject.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
public class Answer extends Entity {
    private long id;
    private Instant creationDate;
    private Instant lastEditDate;
    private String text;
    private boolean isDeleted;
    private long authorId;
    private long questionId;
}
