package com.dubhad.qaproject.dao;

/**
 * Interface, that declares methods for all DaoFactories
 */
public abstract class DaoFactory {
    /**
     * Get concrete implementation of UserDao interface
     * @return concrete implementation of UserDao interface
     * @see UserDao
     */
    public abstract UserDao getUserDao();

    /**
     * Get concrete implementation of QuestionDao interface
     * @return concrete implementation of QuestionDao interface
     * @see QuestionDao
     */
    public abstract QuestionDao getQuestionDao();

    /**
     * Get concrete implementation of AnswerDao interface
     * @return concrete implementation of AnswerDao interface
     * @see AnswerDao
     */
    public abstract AnswerDao getAnswerDao();

    /**
     * Get concrete implementation of TagDao interface
     * @return concrete implementation of TagDao interface
     * @see TagDao
     */
    public abstract TagDao getTagDao();

    /**
     * Get concrete implementation of MarkDao interface
     * @return concrete implementation of MarkDao interface
     * @see MarkDao
     */
    public abstract MarkDao getMarkDao();
}
