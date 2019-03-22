package com.dubhad.qaproject.bean;

import com.dubhad.qaproject.entity.QuestionStatus;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class QuestionBean {
    private long id;
    private UserBean user;
    private Date lastEditDate;
    private String title;
    private String text;
    private QuestionStatus status;
    private List<TagBean> tags = new ArrayList<>();
    private String tagsAggregated;
    private List<AnswerBean> answers = new ArrayList<>();
    private long answersCount;
}
