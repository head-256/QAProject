package com.dubhad.qaproject.dao.impl;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.dao.QuestionDao;
import com.dubhad.qaproject.entity.Question;
import com.dubhad.qaproject.entity.QuestionStatus;
import com.dubhad.qaproject.entity.Tag;
import lombok.extern.log4j.Log4j2;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;
import org.intellij.lang.annotations.Language;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("Duplicates")
@Log4j2
public class OracleQuestionDao extends AbstractOracleDao<Long, Question> implements QuestionDao {
    private static final String COL_ID = "id";
    private static final String COL_CREATION = "creation_date";
    private static final String COL_LAST_EDIT = "last_edit_date";
    private static final String COL_TITLE = "title";
    private static final String COL_TEXT = "text";
    private static final String COL_STATUS = "status";
    private static final String COL_USER_ID = "user_id";


    private static final String SQL_COUNT_BY_NAME_TAGS_OFFSET_START =
            "SELECT COUNT(*) FROM questions WHERE ";
    private static final String SQL_COUNT_BY_NAME_TAGS_OFFSET_END =
            "questions.title LIKE ? ";
    private static final String SQL_NOT_DELETED_COUNT_BY_NAME_TAGS_OFFSET_END =
            "questions.title LIKE ? AND status != 'deleted' ";

    private static final String SQL_FIND_LATEST_BY_NAME_TAGS_OFFSET_START =
            "SELECT id, creation_date, title, text, last_edit_date, status, user_id " +
                    "FROM questions WHERE ";
    private static final String SQL_FIND_LATEST_BY_NAME_TAGS_OFFSET_MIDDLE =
            "EXISTS (SELECT 1 FROM tags_view WHERE tags_view.question_id=questions.id AND UPPER(tags_view.tag_text)=" +
                    "UPPER(?)) AND ";
    private static final String SQL_FIND_LATEST_BY_NAME_TAGS_OFFSET_END =
            "questions.title LIKE ? ORDER BY last_edit_date DESC, id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";

    private static final String SQL_FIND_NOT_DELETED_LATEST_BY_NAME_TAGS_OFFSET_END =
            "questions.title LIKE ? AND status != 'deleted' ORDER BY last_edit_date DESC, id " +
                    "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ";

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Question> findEntityById(Long id) throws ProjectException {
        Question question = null;
        CallableStatement st = null;
        try {
            st = getConnection().prepareCall("{? = call FIND_QUESTION_BY_ID(?)}");
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.setLong(2, id);
            st.execute();
            ResultSet resultSet = ((OracleCallableStatement)st).getCursor(1);
            if(resultSet.next()) {
                question = new Question();
                question.setId(id);
                question.setUserId(resultSet.getLong(COL_USER_ID));
                question.setTitle(resultSet.getString(COL_TITLE));
                question.setText(resultSet.getString(COL_TEXT));
                question.setStatus(QuestionStatus.valueOf(resultSet.getString(COL_STATUS).toUpperCase()));
                question.setCreationDate(Instant.ofEpochMilli(resultSet.getLong(COL_CREATION)));
                question.setLastEditDate(Instant.ofEpochMilli(resultSet.getLong(COL_LAST_EDIT)));
            }
        } catch (SQLException e) {
            throw new ProjectException(e);
        } finally {
            close(st);
        }
        return Optional.ofNullable(question);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Long id) throws ProjectException {
        CallableStatement st = null;
        int affectedRows;
        try {
            st = getConnection().prepareCall("{call DELETE_QUESTION(?)}");
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
    public boolean create(Question entity) throws ProjectException {
        CallableStatement st = null;
        int affectedRows;
        try {
            st = getConnection().prepareCall("{call INSERT_QUESTION(?, ?, ?, ?, ?, ?, ?)}");
            st.setString(1, entity.getTitle());
            st.setString(2, entity.getText());
            st.setLong(3, entity.getCreationDate().toEpochMilli());
            st.setLong(4, entity.getLastEditDate().toEpochMilli());
            st.setString(5, entity.getStatus().name().toLowerCase());
            st.setLong(6, entity.getUserId());
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
    public boolean update(Question entity) throws ProjectException {
        CallableStatement st = null;
        int affectedRows;
        try {
            st = getConnection().prepareCall("{call UPDATE_QUESTION(?, ?, ?, ?, ?, ?, ?)}");
            st.setString(1, entity.getTitle());
            st.setString(2, entity.getText());
            st.setLong(3, entity.getCreationDate().toEpochMilli());
            st.setLong(4, entity.getLastEditDate().toEpochMilli());
            st.setString(5, entity.getStatus().name().toLowerCase());
            st.setLong(6, entity.getUserId());
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
    public List<Question> findQuestionsByUserId(long id) throws ProjectException {
        return findByUserId(id, "{? = call FIND_QUESTIONS_BY_USER_ID(?)}");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Question> findNotDeletedQuestionsByUserId(long id) throws ProjectException {
        return findByUserId(id, "{? = call FIND_NOT_DELETED_QUESTIONS_BY_USER_ID(?)}");
    }

    private List<Question> findByUserId(long id, @Language("Oracle") String function) throws ProjectException {
        List<Question> questions = new ArrayList<>();
        CallableStatement st = null;
        try {
            st = getConnection().prepareCall(function);
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.setLong(2, id);
            st.execute();
            ResultSet resultSet = ((OracleCallableStatement)st).getCursor(1);
            while (resultSet.next()) {
                Question question = new Question();
                question.setId(resultSet.getLong(COL_ID));
                question.setUserId(id);
                question.setTitle(resultSet.getString(COL_TITLE));
                question.setText(resultSet.getString(COL_TEXT));
                question.setStatus(QuestionStatus.valueOf(resultSet.getString(COL_STATUS).toUpperCase()));
                question.setCreationDate(Instant.ofEpochMilli(resultSet.getLong(COL_CREATION)));
                question.setLastEditDate(Instant.ofEpochMilli(resultSet.getLong(COL_LAST_EDIT)));
                questions.add(question);
            }
        } catch (SQLException e) {
            throw new ProjectException(e);
        } finally {
            close(st);
        }
        return questions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Question> findLatestQuestions(long offset, int count) throws ProjectException {
        return findLatestWithOffset(offset, count, "{? = call FIND_LATEST_QUESTIONS_WITH_OFFSET(?, ?)}");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Question> findLatestNotDeletedQuestions(long offset, int count) throws ProjectException {
        return findLatestWithOffset(offset, count, "{? = call FIND_LATEST_NOT_DELETED_QUESTIONS_WITH_OFFSET(?, ?)}");
    }

    private List<Question> findLatestWithOffset(long offset, int count, @Language("Oracle") String function) throws ProjectException {
        List<Question> questions = new ArrayList<>();
        CallableStatement st = null;
        try {
            st = getConnection().prepareCall(function);
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.setLong(2, offset);
            st.setInt(3, count);
            st.execute();
            ResultSet resultSet = ((OracleCallableStatement)st).getCursor(1);
            while (resultSet.next()) {
                Question question = new Question();
                question.setId(resultSet.getLong(COL_ID));
                question.setUserId(resultSet.getLong(COL_USER_ID));
                question.setTitle(resultSet.getString(COL_TITLE));
                question.setText(resultSet.getString(COL_TEXT));
                question.setStatus(QuestionStatus.valueOf(resultSet.getString(COL_STATUS).toUpperCase()));
                question.setCreationDate(Instant.ofEpochMilli(resultSet.getLong(COL_CREATION)));
                question.setLastEditDate(Instant.ofEpochMilli(resultSet.getLong(COL_LAST_EDIT)));
                questions.add(question);
            }
            st = getConnection().prepareCall("{? = call GET_SESSION_CTX()}");
            st.registerOutParameter(1, OracleTypes.VARCHAR);
            st.execute();
            log.error("CONTEXT FROM QUESTION: " + st.getString(1));
        } catch (SQLException e) {
            throw new ProjectException(e);
        } finally {
            close(st);
        }
        return questions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Question> findLatestQuestions(long offset, int count, String name, Collection<Tag> tags)
            throws ProjectException {
        return findLatestByNameTagsOffset(offset, count, name, tags, SQL_FIND_LATEST_BY_NAME_TAGS_OFFSET_END);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Question> findLatestNotDeletedQuestions(long offset, int count, String name, Collection<Tag> tags)
            throws ProjectException {
        return findLatestByNameTagsOffset(offset, count, name, tags, SQL_FIND_NOT_DELETED_LATEST_BY_NAME_TAGS_OFFSET_END);
    }

    private List<Question> findLatestByNameTagsOffset(long offset, int count, String name, Collection<Tag> tags,
                                                      String endQuery) throws ProjectException {
        List<Question> questions = new ArrayList<>();
        PreparedStatement st = null;
        try {
            StringBuilder stringBuilder = new StringBuilder(SQL_FIND_LATEST_BY_NAME_TAGS_OFFSET_START);
            for(Tag ignored : tags){
                stringBuilder.append(SQL_FIND_LATEST_BY_NAME_TAGS_OFFSET_MIDDLE);
            }
            stringBuilder.append(endQuery);
            log.debug(stringBuilder.toString());
            st = getConnection().prepareStatement(stringBuilder.toString());
            int i = 0;
            for(Tag tag: tags){
                ++i;
                st.setString(i, tag.getText());
            }
            if(name != null){
                st.setString(i+1, "%" + name + "%");
            }
            else{
                st.setString(i+1, "%");
            }
            st.setLong(i+2, offset);
            st.setInt(i+3, count);
            ResultSet resultSet = st.executeQuery();
            while (resultSet.next()) {
                Question question = new Question();
                question.setId(resultSet.getLong(COL_ID));
                question.setUserId(resultSet.getLong(COL_USER_ID));
                question.setTitle(resultSet.getString(COL_TITLE));
                question.setText(resultSet.getString(COL_TEXT));
                question.setStatus(QuestionStatus.valueOf(resultSet.getString(COL_STATUS).toUpperCase()));
                question.setCreationDate(Instant.ofEpochMilli(resultSet.getLong(COL_CREATION)));
                question.setLastEditDate(Instant.ofEpochMilli(resultSet.getLong(COL_LAST_EDIT)));
                questions.add(question);
            }
        } catch (SQLException e) {
            throw new ProjectException(e);
        } finally {
            close(st);
        }
        return questions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getQuestionsCount(String name, Collection<Tag> tags)
            throws ProjectException {
        return getCountByNameTagsOffset(name, tags, SQL_COUNT_BY_NAME_TAGS_OFFSET_END);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getNotDeletedQuestionsCount(String name, Collection<Tag> tags)
            throws ProjectException {
        return getCountByNameTagsOffset(name, tags, SQL_NOT_DELETED_COUNT_BY_NAME_TAGS_OFFSET_END);
    }

    private long getCountByNameTagsOffset(String name, Collection<Tag> tags, String endQuery)
            throws ProjectException {
        long result;
        PreparedStatement st = null;
        try {
            StringBuilder stringBuilder = new StringBuilder(SQL_COUNT_BY_NAME_TAGS_OFFSET_START);
            for(Tag ignored : tags){
                stringBuilder.append(SQL_FIND_LATEST_BY_NAME_TAGS_OFFSET_MIDDLE);
            }
            stringBuilder.append(endQuery);
            log.debug(stringBuilder.toString());
            st = getConnection().prepareStatement(stringBuilder.toString());
            int i = 0;
            for(Tag tag: tags){
                ++i;
                st.setString(i, tag.getText());
            }
            if(name != null){
                st.setString(i+1, "%" + name + "%");
            }
            else{
                st.setString(i+1, "%");
            }
            ResultSet resultSet = st.executeQuery();
            if(resultSet.next()) {
                result = resultSet.getLong(1);
            }else{
                throw new ProjectException("Result set is empty");
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
    public boolean createQuestionTagConnection(Question entity, long tagId) throws ProjectException {
        CallableStatement st = null;
        int affectedRows;
        try {
            st = getConnection().prepareCall("{call INSERT_QUESTION_TAG_RELATION(?, ?)}");
            st.setLong(1, entity.getId());
            st.setLong(2, tagId);
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
    public int deleteTagConnections(Question entity) throws ProjectException {
        CallableStatement st = null;
        int affectedRows;
        try {
            st = getConnection().prepareCall("{call DELETE_QUESTION_TAG_RELATIONS(?)}");
            st.setLong(1, entity.getId());
            affectedRows = st.executeUpdate();
        } catch (SQLException e) {
            throw new ProjectException(e);
        } finally {
            close(st);
        }
        return affectedRows;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getCount() throws ProjectException {
        CallableStatement st = null;
        long result;
        try {
            st = getConnection().prepareCall("{? = call GET_QUESTION_COUNT()}");
            st.registerOutParameter(1, OracleTypes.NUMBER);
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
