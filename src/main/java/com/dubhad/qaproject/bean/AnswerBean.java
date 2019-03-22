package com.dubhad.qaproject.bean;

import lombok.Data;

import java.util.Date;

@Data
public class AnswerBean {
    private long id;
    private Date lastEditDate;
    private String text;
    private long positiveRating;
    private long negativeRating;
    private boolean deleted;
    private int currentUserMark; // -1 if negative, 1 if positive, 0 if doesn't exist
    private UserBean user;
    private long questionId;
}
