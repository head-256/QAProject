package com.dubhad.qaproject.dao.impl;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.dao.TagDao;
import com.dubhad.qaproject.entity.Tag;
import lombok.extern.log4j.Log4j2;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("Duplicates")
@Log4j2
public class OracleTagDao extends AbstractOracleDao<Long, Tag> implements TagDao {
    private static final String COL_ID = "id";
    private static final String COL_TEXT = "text";


    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Tag> findEntityById(Long id) throws ProjectException {
        Tag tag;
        CallableStatement st = null;
        try {
            st = getConnection().prepareCall("{? = call GET_TAG_TEXT_BY_ID(?)}");
            st.registerOutParameter(1, OracleTypes.VARCHAR);
            st.setLong(2, id);
            st.execute();
            tag = new Tag();
            tag.setId(id);
            tag.setText(st.getString(1));
        } catch (SQLException e) {
            throw new ProjectException(e);
        } finally {
            close(st);
        }
        return Optional.of(tag);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Long id) throws ProjectException {
        CallableStatement st = null;
        int affectedRows;
        try {
            st = getConnection().prepareCall("{call DELETE_TAG(?)}");
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
    public boolean create(Tag entity) throws ProjectException {
        CallableStatement st = null;
        int affectedRows;
        try {
            st = getConnection().prepareCall("{call INSERT_TAG(?, ?)}");
            st.setString(1, entity.getText());
            st.registerOutParameter(2, OracleTypes.NUMBER);
            affectedRows = st.executeUpdate();
            entity.setId(st.getLong(2));
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
    public boolean update(Tag entity) throws ProjectException {
        CallableStatement st = null;
        int affectedRows;
        try {
            st = getConnection().prepareCall("{call UPDATE_TAG(?, ?)}");
            st.setString(1, entity.getText());
            st.setLong(2, entity.getId());
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
    public boolean initTagIdByText(Tag entity) throws ProjectException {
        CallableStatement st = null;
        boolean created = false;
        try {
            st = getConnection().prepareCall("{? = call GET_TAG_ID_BY_TEXT(?)}");
            st.registerOutParameter(1, OracleTypes.NUMBER);
            st.setString(2, entity.getText());
            st.execute();
            long id = st.getLong(1);
            if (id != 0) {
                entity.setId(id);
            } else {
                st = getConnection().prepareCall("{call INSERT_TAG(?, ?)}");
                st.setString(1, entity.getText());
                st.registerOutParameter(2, OracleTypes.NUMBER);
                st.executeUpdate();
                entity.setId(st.getLong(2));
                created = true;
            }
        } catch (SQLException e) {
            throw new ProjectException(e);
        } finally {
            close(st);
        }
        return created;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Tag> findTagsByQuestionId(long questionId) throws ProjectException {
        List<Tag> tags = new ArrayList<>();
        CallableStatement st = null;
        try {
            st = getConnection().prepareCall("{? = call FIND_TAGS_BY_QUESTION_ID(?)}");
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.setLong(2, questionId);
            st.execute();
            ResultSet resultSet = ((OracleCallableStatement)st).getCursor(1);
            while (resultSet.next()) {
                Tag tag = new Tag();
                tag.setId(resultSet.getLong(COL_ID));
                tag.setText(resultSet.getString(COL_TEXT));
                tags.add(tag);
            }
        } catch (SQLException e) {
            throw new ProjectException(e);
        } finally {
            close(st);
        }
        return tags;
    }
}
