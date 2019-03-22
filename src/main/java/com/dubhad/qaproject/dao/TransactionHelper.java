package com.dubhad.qaproject.dao;

import com.dubhad.qaproject.pool.ConnectionPool;
import com.dubhad.qaproject.pool.ProxyConnection;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.sql.SQLException;

/**
 * Class, provides support in dao management
 */
@Log4j2
public class TransactionHelper {
    /**
     * Connection, associated with object. It's taken from connection pool in constructor
     */
    private ProxyConnection connection;

    /**
     * Default factory, that is supposed to create daos
     */
    @Getter
    private static DaoFactory factory = new OracleDaoFactory();

    /**
     * Initializes connection filed by taking connection from pool
     */
    public TransactionHelper(){
        connection = ConnectionPool.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            log.error("Unable to set autocommit false");
        }
    }

    /**
     * Prepares dao to work by setting connection to it
     * @param dao dao to prepare
     */
    public void prepareDao(AbstractDao dao){
        dao.setConnection(connection);
    }

    /**
     * Prepares daos to work by setting connection to each of them
     * @param daos dao to prepare
     */
    public void prepareDaos(AbstractDao... daos){
        for(AbstractDao dao: daos){
            dao.setConnection(connection);
        }
    }

    /**
     * Tries to commit changes, made by daos.
     * @return true, if committed successfully, false otherwise
     */
    public boolean commit(){
        boolean result = false;
        try {
            connection.commit();
            result = true;
        } catch (SQLException e) {
            log.error("unable to commit");
        }
        return result;
    }

    /**
     * Tries to rollback changes, made by daos.
     * @return true, if rollback successfully, false otherwise
     */
    public boolean rollback(){
        boolean result = false;
        try {
            connection.rollback();
            result = true;
        } catch (SQLException e) {
            log.error("unable to commit");
        }
        return result;
    }

    /**
     * Finishes work with daos by closing connection
     */
    public void endTransaction(){
        connection.close();
    }
}
