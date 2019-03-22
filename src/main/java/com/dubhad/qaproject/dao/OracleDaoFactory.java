package com.dubhad.qaproject.dao;

import com.dubhad.qaproject.dao.impl.*;

/**
 * Concrete implementation of DaoFactory interface, returns Daos for Oracle db
 */
class OracleDaoFactory extends DaoFactory {
    /**
     * Get concrete implementation of UserDao interface
     * @return concrete implementation of UserDao interface
     * @see OracleUserDao
     */
    @Override
    public UserDao getUserDao() {
        return new OracleUserDao();
    }

    /**
     * Get concrete implementation of QuestionDao interface
     * @return concrete implementation of QuestionDao interface
     * @see OracleQuestionDao
     */
    @Override
    public QuestionDao getQuestionDao() {
        return new OracleQuestionDao();
    }

    /**
     * Get concrete implementation of AnswerDao interface
     * @return concrete implementation of AnswerDao interface
     * @see OracleAnswerDao
     */
    @Override
    public AnswerDao getAnswerDao() {
        return new OracleAnswerDao();
    }

    /**
     * Get concrete implementation of TagDao interface
     * @return concrete implementation of TagDao interface
     * @see OracleTagDao
     */
    @Override
    public TagDao getTagDao() {
        return new OracleTagDao();
    }

    /**
     * Get concrete implementation of MarkDao interface
     * @return concrete implementation of MarkDao interface
     * @see OracleMarkDao
     */
    @Override
    public MarkDao getMarkDao() {
        return new OracleMarkDao();
    }
}
