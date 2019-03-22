package com.dubhad.qaproject.dao;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.entity.Answer;

import java.util.List;

/**
 * Interface, that provides methods for AnswerDaos. All concrete AnswerDaos must implement it
 */
public interface AnswerDao extends AbstractDao<Long, Answer> {
    /**
     * Find all answers to specified question
     * @param id PK of question to find answers for
     * @return List of all found answers
     * @throws ProjectException in the case of unexpected error on lower level
     */
    List<Answer> findAnswersByQuestionId(long id) throws ProjectException;

    /**
     * Finds not deleted answers to specified question
     * @param id PK of question to find answers for
     * @return List of all found answers
     * @throws ProjectException in the case of unexpected error on lower level
     */
    List<Answer> findNotDeletedAnswersByQuestionId(long id) throws ProjectException;

    /**
     * Counts answers to the specified question
     * @param id PK of question
     * @return number of found answers
     * @throws ProjectException in the case of unexpected error on lower level
     */
    long getAnswersCountByQuestionId(long id) throws ProjectException;

    /**
     * Counts not deleted answers to the specified question
     * @param id PK of question
     * @return number of found answers
     * @throws ProjectException in the case of unexpected error on lower level
     */
    long getNotDeletedAnswersCountByQuestionId(long id) throws ProjectException;
}
