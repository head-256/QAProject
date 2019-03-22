package com.dubhad.qaproject.dao;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.entity.Question;
import com.dubhad.qaproject.entity.Tag;

import java.util.Collection;
import java.util.List;

/**
 * Interface, that provides methods for QuestionDaos. All concrete QuestionDaos must implement it
 */
public interface QuestionDao extends AbstractDao<Long, Question> {
    /**
     * Finds questions of specified user
     * @param id id of user, whose questions needs to be found
     * @return List of all found questions
     * @throws ProjectException in the case of unexpected error on lower level
     */
    List<Question> findQuestionsByUserId(long id) throws ProjectException;

    /**
     * Finds questions with latest edit date with offset and limit
     * @param offset offset of questions slice
     * @param count size if questions slice
     * @return List of found questions
     * @throws ProjectException in the case of unexpected error on lower level
     */
    List<Question> findLatestQuestions(long offset, int count) throws ProjectException;

    /**
     * Finds questions with latest edit date, which matches name pattern and have specified tags, with offset and count
     * @param offset offset of questions slice
     * @param count size of questions slice
     * @param name substring, that has to be found inside name
     * @param tags required tags for questions
     * @return list of all questions, that match requirements
     * @throws ProjectException in the case of unexpected error on lower level
     */
    List<Question> findLatestQuestions(long offset, int count, String name, Collection<Tag> tags) throws ProjectException;

    /**
     * Finds count of questions, which matches name pattern and have specified tags
     * @param name substring, that has to be found inside name
     * @param tags required tags for questions
     * @return count of questions, that match requirements
     * @throws ProjectException in the case of unexpected error on lower level
     */
    long getQuestionsCount(String name, Collection<Tag> tags) throws ProjectException;

    /**
     * Finds not deleted questions of specified user
     * @param id id of user, whose questions needs to be found
     * @return List of all found questions
     * @throws ProjectException in the case of unexpected error on lower level
     */
    List<Question> findNotDeletedQuestionsByUserId(long id) throws ProjectException;

    /**
     * Finds not deleted questions with latest edit date with offset and limit
     * @param offset offset of questions slice
     * @param count size if questions slice
     * @return List of found questions
     * @throws ProjectException in the case of unexpected error on lower level
     */
    List<Question> findLatestNotDeletedQuestions(long offset, int count) throws ProjectException;

    /**
     * Finds not deleted questions with latest edit date, which matches name pattern and have specified tags, with offset and count
     * @param offset offset of questions slice
     * @param count size of questions slice
     * @param name substring, that has to be found inside name
     * @param tags required tags for questions
     * @return list of all questions, that match requirements
     * @throws ProjectException in the case of unexpected error on lower level
     */
    List<Question> findLatestNotDeletedQuestions(long offset, int count, String name, Collection<Tag> tags)
            throws ProjectException;

    /**
     * Finds count of not deleted questions, which matches name pattern and have specified tags
     * @param name substring, that has to be found inside name
     * @param tags required tags for questions
     * @return count of questions, that match requirements
     * @throws ProjectException in the case of unexpected error on lower level
     */
    long getNotDeletedQuestionsCount(String name, Collection<Tag> tags) throws ProjectException;

    /**
     * Creates question-tag connection in m2m table
     * @param entity Question entity
     * @param tagId id of tag
     * @return true, if record added successfully, false otherwise
     * @throws ProjectException in the case of unexpected error on lower level
     */
    boolean createQuestionTagConnection(Question entity, long tagId) throws ProjectException;

    /**
     * Deletes all question-tag connections in m2m table
     * @param entity Question entity
     * @return number of deleted records
     * @throws ProjectException in the case of unexpected error on lower level
     */
    int deleteTagConnections(Question entity) throws ProjectException;

    /**
     * Gets count of all questions
     * @return number of all questions
     * @throws ProjectException in the case of unexpected error on lower level
     */
    long getCount() throws ProjectException;
}
