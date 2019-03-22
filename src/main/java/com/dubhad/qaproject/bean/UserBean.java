package com.dubhad.qaproject.bean;

import com.dubhad.qaproject.entity.UserStatus;
import lombok.Data;

import java.util.List;

@Data
public class UserBean {
    private long id;
    private String username;
    private String email;
    private int privilegeLevel;
    private UserStatus status;
    private String avatarPath;

    private long positiveRating;
    private long negativeRating;

    private List<QuestionBean> questions;
    private List<AnswerBean> answers;
}
