package com.dubhad.qaproject.dao.impl;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.dao.AbstractDao;
import com.dubhad.qaproject.entity.Entity;
import com.dubhad.qaproject.pool.ProxyConnection;
import com.dubhad.qaproject.servlet.Controller;
import lombok.extern.log4j.Log4j2;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Class, that provides basic methods for oracle daos. All of them must extend it
 * @param <PK> Primary key type
 * @param <T> Type of entity
 */
@Log4j2
public abstract class AbstractOracleDao<PK, T extends Entity> implements AbstractDao<PK, T> {
    private ProxyConnection connection;

    /**
     * Tries to close passed statement, if its not null
     * @param statement statement to close
     */
    protected void close(Statement statement){
        if(statement != null){
            try{
                statement.close();
            }
            catch (SQLException e){
                log.error("Unable to close statement: ", e);
            }
        }
    }

    /**
     * Returns connection of dao, or throws exception if its null
     * @return connection
     * @throws ProjectException if connection is null
     */
    ProxyConnection getConnection() throws ProjectException {
        if(connection == null){
            log.error("Connection not set for DAO");
            throw new ProjectException("Connection not set for DAO");
        }
        return connection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setConnection(ProxyConnection connection) {
        CallableStatement cs;
        String sessionId = Controller.SESSION_ID;
        log.error("SET CONNECTION, ID: " + sessionId);
        try {
            cs = connection.prepareCall("{ call SESSION_CTX_PKG.SET_SESSION_ID(?) }");
            cs.setString(1, sessionId);
            cs.execute();
            cs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.connection = connection;
    }
}
