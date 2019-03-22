package com.dubhad.qaproject.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@EqualsAndHashCode(callSuper = false)
@Data
public class Question extends Entity {
    private long id;
    private long userId;
    private Instant creationDate;
    private Instant lastEditDate;
    private String title;
    private String text;
    private QuestionStatus status;
}
