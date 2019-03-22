package com.dubhad.qaproject.dao.impl;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.dao.AnswerDao;
import com.dubhad.qaproject.entity.Answer;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("Duplicates")
public class OracleAnswerDao extends AbstractOracleDao<Long, Answer> implements AnswerDao {
    private static final String COL_ID = "id";
    private static final String COL_CREATION = "creation_date";
    private static final String COL_LAST_EDIT = "last_edit_date";
    private static final String COL_TEXT = "text";
    private static final String COL_IS_DELETED = "is_deleted";
    private static final String COL_AUTHOR_ID = "author_id";
    private static final String COL_QUESTION_ID = "question_id";


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Answer> findEntityById(Long id) throws ProjectException {
        Answer answer = null;
        CallableStatement st = null;
        try {
            st = getConnection().prepareCall("{? = call FIND_ANSWER_BY_ID(?)}");
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.setLong(2, id);
            st.execute();
            ResultSet resultSet = ((OracleCallableStatement)st).getCursor(1);
            if(resultSet.next()) {
                answer = new Answer();
                answer.setId(id);
                answer.setText(resultSet.getString(COL_TEXT));
                answer.setAuthorId(resultSet.getLong(COL_AUTHOR_ID));
                answer.setQuestionId(resultSet.getLong(COL_QUESTION_ID));
                answer.setCreationDate(Instant.ofEpochMilli(resultSet.getLong(COL_CREATION)));
                answer.setLastEditDate(Instant.ofEpochMilli(resultSet.getLong(COL_LAST_EDIT)));
                answer.setDeleted(resultSet.getInt(COL_IS_DELETED) == 1);
            }
        } catch (SQLException e) {
            throw new ProjectException(e);
        } finally {
            close(st);
        }
        return Optional.ofNullable(answer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Long id) throws ProjectException {
        CallableStatement st = null;
        int affectedRows;
        try {
            st = getConnection().prepareCall("{call DELETE_ANSWER(?)}");
            st.setLong(1, id);
            affectedRows = st.executeUpdate();
        } catch (SQLException e) {
            throw new ProjectException(e);
        } finally {
            close(st);
        }
        return affectedRows == 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean create(Answer entity) throws ProjectException {
        CallableStatement st = null;
        int affectedRows;
        try {
            st = getConnection().prepareCall("{call INSERT_ANSWER(?, ?, ?, ?, ?, ?, ?)}");
            st.setLong(1, entity.getCreationDate().toEpochMilli());
            st.setLong(2, entity.getLastEditDate().toEpochMilli());
            st.setString(3, entity.getText());
            st.setLong(4, entity.getAuthorId());
            st.setLong(5, entity.getQuestionId());
            st.setInt(6, entity.isDeleted() ? 1 : 0);
            st.registerOutParameter(7, OracleTypes.NUMBER);
            affectedRows = st.executeUpdate();
            entity.setId(st.getLong(7));
        } catch (SQLException e) {
            throw new ProjectException(e);
        } finally {
            close(st);
        }
        return affectedRows == 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean update(Answer entity) throws ProjectException {
        CallableStatement st = null;
        int affectedRows;
        try {
            st = getConnection().prepareCall("{call UPDATE_ANSWER(?, ?, ?, ?, ?, ?, ?)}");
            st.setLong(1, entity.getCreationDate().toEpochMilli());
            st.setLong(2, entity.getLastEditDate().toEpochMilli());
            st.setString(3, entity.getText());
            st.setLong(4, entity.getAuthorId());
            st.setLong(5, entity.getQuestionId());
            st.setInt(6, entity.isDeleted() ? 1 : 0);
            st.setLong(7, entity.getId());
            affectedRows = st.executeUpdate();
        } catch (SQLException e) {
            throw new ProjectException(e);
        } finally {
            close(st);
        }
        return affectedRows == 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Answer> findAnswersByQuestionId(long id) throws ProjectException {
        List<Answer> answers = new ArrayList<>();
        CallableStatement st = null;
        try {
            st = getConnection().prepareCall("{? = call FIND_ANSWERS_BY_QUESTION_ID(?)}");
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.setLong(2, id);
            st.execute();
            ResultSet resultSet = ((OracleCallableStatement)st).getCursor(1);
            while (resultSet.next()) {
                Answer answer = new Answer();
                answer.setId(resultSet.getLong(COL_ID));
                answer.setText(resultSet.getString(COL_TEXT));
                answer.setAuthorId(resultSet.getLong(COL_AUTHOR_ID));
                answer.setQuestionId(id);
                answer.setCreationDate(Instant.ofEpochMilli(resultSet.getLong(COL_CREATION)));
                answer.setLastEditDate(Instant.ofEpochMilli(resultSet.getLong(COL_LAST_EDIT)));
                answer.setDeleted(resultSet.getInt(COL_IS_DELETED) == 1);
                answers.add(answer);
            }
        } catch (SQLException e) {
            throw new ProjectException(e);
        } finally {
            close(st);
        }
        return answers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Answer> findNotDeletedAnswersByQuestionId(long id) throws ProjectException {
        List<Answer> answers = new ArrayList<>();
        CallableStatement st = null;
        try {
            st = getConnection().prepareCall("{? = call FIND_NOT_DELETED_ANSWERS_BY_QUESTION_ID(?)}");
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.setLong(2, id);
            st.execute();
            ResultSet resultSet = ((OracleCallableStatement)st).getCursor(1);
            while (resultSet.next()) {
                Answer answer = new Answer();
                answer.setId(resultSet.getLong(COL_ID));
                answer.setText(resultSet.getString(COL_TEXT));
                answer.setAuthorId(resultSet.getLong(COL_AUTHOR_ID));
                answer.setQuestionId(id);
                answer.setCreationDate(Instant.ofEpochMilli(resultSet.getLong(COL_CREATION)));
                answer.setLastEditDate(Instant.ofEpochMilli(resultSet.getLong(COL_LAST_EDIT)));
                answer.setDeleted(resultSet.getInt(COL_IS_DELETED) == 1);
                answers.add(answer);
            }
        } catch (SQLException e) {
            throw new ProjectException(e);
        } finally {
            close(st);
        }
        return answers;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getAnswersCountByQuestionId(long id) throws ProjectException {
        CallableStatement st = null;
        long result;
        try {
            st = getConnection().prepareCall("{? = call GET_ANSWER_COUNT_BY_QUESTION_ID(?)}");
            st.registerOutParameter(1, OracleTypes.NUMBER);
            st.setLong(2, id);
            st.execute();
            result = st.getLong(1);
        } catch (SQLException e) {
            throw new ProjectException(e);
        } finally {
            close(st);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getNotDeletedAnswersCountByQuestionId(long id) throws ProjectException {
        CallableStatement st = null;
        long result;
        try {
            st = getConnection().prepareCall("{? = call GET_NOT_DELETED_ANSWER_COUNT_BY_QUESTION_ID(?)}");
            st.registerOutParameter(1, OracleTypes.NUMBER);
            st.setLong(2, id);
            st.execute();
            result = st.getLong(1);
        } catch (SQLException e) {
            throw new ProjectException(e);
        } finally {
            close(st);
        }
        return result;
    }
}
