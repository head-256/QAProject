package com.dubhad.qaproject.dao;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.bean.UserBean;
import com.dubhad.qaproject.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Interface, that provides methods for UserDaos. All concrete UserDaos must implement it
 */
public interface UserDao extends AbstractDao<Long, User> {
    /**
     * Finds entity by specified username
     * @param username username to search
     * @return Optional object, that contains User if one was found, empty otherwise
     * @throws ProjectException in the case of unexpected error on lower level
     */
    Optional<User> findEntityByUsername(String username) throws ProjectException;

    /**
     * Finds entity by specified email
     * @param email email to search
     * @return Optional object, that contains User if one was found, empty otherwise
     * @throws ProjectException in the case of unexpected error on lower level
     */
    Optional<User> findEntityByEmail(String email) throws ProjectException;

    /**
     * Finds users by offset and limit
     * @param offset offset of users slice
     * @param limit size of users slice
     * @return list of found users
     * @throws ProjectException in the case of unexpected error on lower level
     */
    List<User> findUsers(long offset, int limit) throws ProjectException;

    /**
     * Gets count of all users
     * @return number of all users
     * @throws ProjectException in the case of unexpected error on lower level
     */
    long getCount() throws ProjectException;

    void saveUserToContext(User entity) throws ProjectException;

    User getUserFromContext() throws ProjectException;
}
