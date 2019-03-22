package com.dubhad.qaproject.dao.impl;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.dao.UserDao;
import com.dubhad.qaproject.entity.User;
import com.dubhad.qaproject.entity.UserRole;
import com.dubhad.qaproject.entity.UserStatus;
import lombok.extern.log4j.Log4j2;
import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("Duplicates")
@Log4j2
public class OracleUserDao extends AbstractOracleDao<Long, User> implements UserDao {
    private static final String COL_ID = "id";
    private static final String COL_USERNAME = "username";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD_SALT = "password_salt";
    private static final String COL_PASSWORD = "password";
    private static final String COL_ROLE= "role";
    private static final String COL_STATUS = "status";
    private static final String COL_AVATAR_PATH = "avatar_ext";

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> findEntityById(Long id) throws ProjectException {
        User user = null;
        CallableStatement st = null;
        try {
            st = getConnection().prepareCall("{? = call FIND_USER_BY_ID(?)}");
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.setLong(2, id);
            st.execute();
            ResultSet resultSet = ((OracleCallableStatement)st).getCursor(1);
            if(resultSet.next()) {
                user = new User();
                user.setId(id);
                user.setEmail(resultSet.getString(COL_EMAIL));
                user.setUsername(resultSet.getString(COL_USERNAME));
                user.setPassword(resultSet.getString(COL_PASSWORD));
                user.setPasswordSalt(resultSet.getString(COL_PASSWORD_SALT));
                user.setAvatarPath(resultSet.getString(COL_AVATAR_PATH));
                user.setRole(UserRole.valueOf(resultSet.getString(COL_ROLE).toUpperCase()));
                user.setStatus(UserStatus.valueOf(resultSet.getString(COL_STATUS).toUpperCase()));
            }
        } catch (SQLException e) {
            throw new ProjectException(e);
        } finally {
            close(st);
        }
        return Optional.ofNullable(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean delete(Long id) throws ProjectException {
        CallableStatement st = null;
        int affectedRows;
        try {
            st = getConnection().prepareCall("{call DELETE_USER(?)}");
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
    public boolean create(User entity) throws ProjectException {
        CallableStatement st = null;
        int affectedRows;
        try {
            st = getConnection().prepareCall("{call INSERT_USER(?, ?, ?, ?, ?, ?, ?, ?)}");
            st.setString(1, entity.getUsername());
            st.setString(2, entity.getEmail());
            st.setString(3, entity.getPasswordSalt());
            st.setString(4, entity.getPassword());
            st.setString(5, entity.getRole().name().toLowerCase());
            st.setString(6, entity.getStatus().name().toLowerCase());
            st.setString(7, entity.getAvatarPath());
            st.registerOutParameter(8, OracleTypes.NUMBER);
            affectedRows = st.executeUpdate();
            entity.setId(st.getLong(8));
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
    public boolean update(User entity) throws ProjectException {
        CallableStatement st = null;
        int affectedRows;
        try {
            st = getConnection().prepareCall("{call UPDATE_USER(?, ?, ?, ?, ?, ?, ?, ?)}");
            st.setString(1, entity.getUsername());
            st.setString(2, entity.getEmail());
            st.setString(3, entity.getPasswordSalt());
            st.setString(4, entity.getPassword());
            st.setString(5, entity.getRole().name().toLowerCase());
            st.setString(6, entity.getStatus().name().toLowerCase());
            st.setString(7, entity.getAvatarPath());
            st.setLong(8, entity.getId());
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
    public Optional<User> findEntityByUsername(String username) throws ProjectException {
        User user = null;
        CallableStatement st = null;
        try {
            st = getConnection().prepareCall("{? = call FIND_USER_BY_USERNAME(?)}");
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.setString(2, username);
            st.execute();
            ResultSet resultSet = ((OracleCallableStatement)st).getCursor(1);
            if(resultSet.next()) {
                user = new User();
                user.setId(resultSet.getLong(COL_ID));
                user.setEmail(resultSet.getString(COL_EMAIL));
                user.setUsername(username);
                user.setPassword(resultSet.getString(COL_PASSWORD));
                user.setPasswordSalt(resultSet.getString(COL_PASSWORD_SALT));
                user.setAvatarPath(resultSet.getString(COL_AVATAR_PATH));
                user.setRole(UserRole.valueOf(resultSet.getString(COL_ROLE).toUpperCase()));
                user.setStatus(UserStatus.valueOf(resultSet.getString(COL_STATUS).toUpperCase()));
            }
        } catch (SQLException e) {
            throw new ProjectException(e);
        } finally {
            close(st);
        }
        return Optional.ofNullable(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<User> findEntityByEmail(String email) throws ProjectException {
        User user = null;
        CallableStatement st = null;
        try {
            st = getConnection().prepareCall("{? = call FIND_USER_BY_EMAIL(?)}");
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.setString(2, email);
            st.execute();
            ResultSet resultSet = ((OracleCallableStatement)st).getCursor(1);
            if(resultSet.next()) {
                user = new User();
                user.setId(resultSet.getLong(COL_ID));
                user.setEmail(email);
                user.setUsername(resultSet.getString(COL_USERNAME));
                user.setPassword(resultSet.getString(COL_PASSWORD));
                user.setPasswordSalt(resultSet.getString(COL_PASSWORD_SALT));
                user.setAvatarPath(resultSet.getString(COL_AVATAR_PATH));
                user.setRole(UserRole.valueOf(resultSet.getString(COL_ROLE).toUpperCase()));
                user.setStatus(UserStatus.valueOf(resultSet.getString(COL_STATUS).toUpperCase()));
            }
        } catch (SQLException e) {
            throw new ProjectException(e);
        } finally {
            close(st);
        }
        return Optional.ofNullable(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<User> findUsers(long offset, int limit) throws ProjectException {
        List<User> users = new ArrayList<>();
        CallableStatement st = null;
        try {
            st = getConnection().prepareCall("{? = call FIND_USERS(?, ?)}");
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.setLong(2, offset);
            st.setInt(3, limit);
            st.execute();
            ResultSet resultSet = ((OracleCallableStatement)st).getCursor(1);
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong(COL_ID));
                user.setEmail(resultSet.getString(COL_EMAIL));
                user.setUsername(resultSet.getString(COL_USERNAME));
                user.setPassword(resultSet.getString(COL_PASSWORD));
                user.setPasswordSalt(resultSet.getString(COL_PASSWORD_SALT));
                user.setAvatarPath(resultSet.getString(COL_AVATAR_PATH));
                user.setRole(UserRole.valueOf(resultSet.getString(COL_ROLE).toUpperCase()));
                user.setStatus(UserStatus.valueOf(resultSet.getString(COL_STATUS).toUpperCase()));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new ProjectException(e);
        } finally {
            close(st);
        }
        return users;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getCount() throws ProjectException {
        CallableStatement st = null;
        long result;
        try {
            st = getConnection().prepareCall("{? = call GET_USER_COUNT()}");
            st.registerOutParameter(1, OracleTypes.NUMBER);
            st.execute();
            result = st.getInt(1);
        } catch (SQLException e) {
            throw new ProjectException(e);
        } finally {
            close(st);
        }
        return result;
    }

    @Override
    public void saveUserToContext(User entity) throws ProjectException {
        CallableStatement st = null;
        try {
            st = getConnection().prepareCall("{call SAVE_USER_TO_CONTEXT(?, ?, ?, ?, ?, ?)}");
            log.error("ID 1: " + entity.getId());
            st.setLong(1, entity.getId());
            st.setString(2, entity.getUsername());
            st.setString(3, entity.getEmail());
            st.setString(4, entity.getRole().name().toLowerCase());
            st.setString(5, entity.getStatus().name().toLowerCase());
            st.setString(6, entity.getAvatarPath());
            st.execute();
        } catch (SQLException e) {
            throw new ProjectException(e);
        } finally {
            close(st);
        }
    }

    @Override
    public User getUserFromContext() throws ProjectException {
        CallableStatement st = null;
        User user = null;
        try {
            log.error("CALL getUserFromContext");
            st = getConnection().prepareCall("{? = call GET_USER_FROM_CONTEXT()}");
            st.registerOutParameter(1, OracleTypes.STRUCT, "USER_");
            st.execute();
            Struct struct = (Struct) st.getObject(1);
            if (struct != null) {
                Object[] attrs = struct.getAttributes();
                long user_id = (((BigDecimal) attrs[0]).toBigInteger()).longValue();
                log.error("ID 2: " + user_id);
                user = new User();
                user.setId(user_id);
                user.setUsername((String)attrs[1]);
                user.setEmail((String)attrs[2]);
                user.setRole(UserRole.valueOf(((String)attrs[3]).toUpperCase()));
                user.setStatus(UserStatus.valueOf(((String)attrs[4]).toUpperCase()));
                user.setAvatarPath((String)attrs[5]);
            }
        } catch (SQLException e) {
            throw new ProjectException(e);
        } finally {
            close(st);
        }
        return user;
    }
}