package com.dubhad.qaproject.dao;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.entity.Entity;
import com.dubhad.qaproject.pool.ProxyConnection;

import java.util.Optional;

/**
 * Interface, that provides basic methods for dao classes. All of them must implement it. In the case if any method is
 * not supported, UnsupportedOperationException expected
 * @param <PK> Primary key type of entity
 * @param <T> Entity class
 */
public interface AbstractDao<PK, T extends Entity> {
    /**
     * Finds an entity with specified primary key
     * @param id PK of entity
     * @return Optional object, that contains entity if it was found, empty otherwise
     * @throws ProjectException in the case of unexpected error on lower level
     */
    Optional<T> findEntityById(PK id) throws ProjectException;

    /**
     * Deletes an entity with specified primary key
     * @param id PK of entity
     * @return true, if entity deleted, false, if it wasn't found
     * @throws ProjectException in the case of unexpected error on lower level
     */
    boolean delete(PK id) throws ProjectException;

    /**
     * Creates entity in database
     * @param entity entity to be created
     * @return true, if record was inserted successfully, false otherwise
     * @throws ProjectException in the case of unexpected error on lower level
     */
    boolean create(T entity) throws ProjectException;

    /**
     * Updates entity in database
     * @param entity entity to take all fields, including PK, from
     * @return true, if record was updated successfully, false otherwise
     * @throws ProjectException in the case of unexpected error on lower level
     */
    boolean update(T entity) throws ProjectException;

    /**
     * Sets connection for all operations of this object
     * @param connection connection to be set
     */
    void setConnection(ProxyConnection connection);
}
