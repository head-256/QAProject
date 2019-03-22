package com.dubhad.qaproject.dao.impl;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.dao.MarkDao;
import com.dubhad.qaproject.entity.Mark;
import lombok.extern.log4j.Log4j2;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.util.Optional;

@SuppressWarnings("Duplicates")
@Log4j2
public class OracleMarkDao extends AbstractOracleDao<Long, Mark> implements MarkDao {

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Mark> findEntityById(Long id) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Long id) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean create(Mark entity) throws ProjectException {
        CallableStatement st = null;
        int affectedRows;
        try {
            st = getConnection().prepareCall("{call INSERT_MARK(?, ?, ?)}");
            st.setLong(1, entity.getUserId());
            st.setLong(2, entity.getAnswerId());
            st.setInt(3, entity.isValue() ? 1 : 0);
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
    public boolean update(Mark entity) throws ProjectException {
        CallableStatement st = null;
        int affectedRows;
        try {
            st = getConnection().prepareCall("{call UPDATE_MARK(?, ?, ?)}");
            st.setInt(1, entity.isValue() ? 1 : 0);
            st.setLong(2, entity.getUserId());
            st.setLong(3, entity.getAnswerId());
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
    public Optional<Mark> findEntityByUserAndAnswerId(long userId, long answerId) throws ProjectException {
        Mark mark = null;
        CallableStatement st = null;
        try {
            st = getConnection().prepareCall("{? = call GET_MARK_VALUE_BY_ID(?, ?)}");
            st.registerOutParameter(1, OracleTypes.NUMBER);
            st.setLong(2, userId);
            st.setLong(3, answerId);
            st.execute();
            int res = st.getInt(1);
            if (res != -1) {
                boolean value = (res == 1);
                mark = new Mark();
                mark.setValue(value);
                mark.setAnswerId(answerId);
                mark.setUserId(userId);
            }
        } catch (SQLException e) {
            throw new ProjectException(e);
        } finally {
            close(st);
        }
        return Optional.ofNullable(mark);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int putMark(Mark mark) throws ProjectException {
        CallableStatement st = null;
        int result;
        try {
            st = getConnection().prepareCall("{? = call GET_MARK_VALUE_BY_ID(?, ?)}");
            st.registerOutParameter(1, OracleTypes.NUMBER);
            st.setLong(2, mark.getUserId());
            st.setLong(3, mark.getAnswerId());
            st.execute();
            int res = st.getInt(1);
            if(res != -1) {
                boolean value = (res == 1);
                if(value != mark.isValue()){
                    st = getConnection().prepareCall("{call DELETE_MARK(?, ?)}");
                    st.setLong(1, mark.getUserId());
                    st.setLong(2, mark.getAnswerId());
                    int affectedRows = st.executeUpdate();
                    if(affectedRows != 1){
                        throw new ProjectException("Unexpected affected rows num");
                    }
                    result = 0;
                }
                else {
                    result = mark.isValue() ? 1 : -1;
                }
            }
            else{
                st = getConnection().prepareCall("{call INSERT_MARK(?, ?, ?)}");
                st.setLong(1, mark.getUserId());
                st.setLong(2, mark.getAnswerId());
                st.setInt(3, mark.isValue() ? 1 : 0);
                int affectedRows = st.executeUpdate();
                if(affectedRows != 1){
                    throw new ProjectException("Unexpected affected rows num");
                }
                result = mark.isValue() ? 1 : -1;
            }
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
    public long getPositiveUserRating(long id) throws ProjectException {
        return getUserRating(id, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getNegativeUserRating(long id) throws ProjectException {
        return getUserRating(id, 0);
    }

    private long getUserRating(long id, int value) throws ProjectException {
        CallableStatement st = null;
        long result;
        try {
            st = getConnection().prepareCall("{? = call GET_USER_RATING(?, ?)}");
            st.registerOutParameter(1, OracleTypes.NUMBER);
            st.setLong(2, id);
            st.setInt(3, value);
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
    public long getPositiveAnswerRating(long id) throws ProjectException {
        return getAnswerRating(id, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getNegativeAnswerRating(long id) throws ProjectException {
        return getAnswerRating(id, 0);
    }

    private long getAnswerRating(long id, int value) throws ProjectException {
        CallableStatement st = null;
        long result;
        try {
            st = getConnection().prepareCall("{? = call GET_ANSWER_RATING(?, ?)}");
            st.registerOutParameter(1, OracleTypes.NUMBER);
            st.setLong(2, id);
            st.setInt(3, value);
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
