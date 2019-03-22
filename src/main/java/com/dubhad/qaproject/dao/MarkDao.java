package com.dubhad.qaproject.dao;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.entity.Mark;

import java.util.Optional;

/**
 * Interface, that provides methods for MarkDaos. All concrete MarkDaos must implement it
 */
public interface MarkDao extends AbstractDao<Long, Mark> {
    /**
     * Finds mark by id of user, who put it, and answer id
     * @param userId id of user, who put mark
     * @param answerId id of rated answer
     * @return Optional object, that contains Mark, if it was found, empty otherwise
     * @throws ProjectException in the case of unexpected error on lower level
     */
    Optional<Mark> findEntityByUserAndAnswerId(long userId, long answerId) throws ProjectException;

    /**
     * Puts the specified mark and returns mark after that operation
     * @param mark: Mark to be put
     * @return current mark: -1 negative, 0 - none, 1 - positive
     * @throws ProjectException in the case of unexpected error on lower level
     */
    int putMark(Mark mark) throws ProjectException;

    /**
     * Gets positive rating of specified user - number of positive marks, put to his answers
     * @param id id of user to count rating for
     * @return positive rating
     * @throws ProjectException in the case of unexpected error on lower level
     */
    long getPositiveUserRating(long id) throws ProjectException;

    /**
     * Gets negative rating of specified user - number of negative marks, put to his answers
     * @param id id of user to count rating for
     * @return negative rating
     * @throws ProjectException in the case of unexpected error on lower level
     */
    long getNegativeUserRating(long id) throws ProjectException;

    /**
     * Gets positive rating of specified answer - number of positive marks, put to it
     * @param id id of answer to count rating for
     * @return positive rating
     * @throws ProjectException in the case of unexpected error on lower level
     */
    long getPositiveAnswerRating(long id) throws ProjectException;

    /**
     * Gets negative rating of specified answer - number of negative marks, put to it
     * @param id id of answer to count rating for
     * @return positive rating
     * @throws ProjectException in the case of unexpected error on lower level
     */
    long getNegativeAnswerRating(long id) throws ProjectException;
}
