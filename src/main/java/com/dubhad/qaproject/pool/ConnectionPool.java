package com.dubhad.qaproject.pool;

import lombok.extern.log4j.Log4j2;
import oracle.jdbc.OracleTypes;

import java.sql.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Singleton class, connection pool to database.
 * Requires running server and existing properties file to fetch parameters from
 */
@Log4j2
public final class ConnectionPool {
    private static ConnectionPool instance;
    private static AtomicBoolean instanceExists = new AtomicBoolean();
    private static ReentrantLock lock = new ReentrantLock();

    private BlockingQueue<ProxyConnection> connectionQueue = new LinkedBlockingDeque<>();;
    private List<ProxyConnection> givenAwayConnections = new LinkedList<>();

    private int poolSize;
    private int minPoolSize;

    private ConnectionPool() {
        poolSize = 16;
        minPoolSize = 8;
        try {
            DriverManager.registerDriver((Driver) Class.forName(DbSettingsManager.DRIVER_NAME).newInstance());
        } catch (SQLException e) {
            log.fatal("Unable to register driver", e);
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            log.error("Illegal state");
        } catch (InstantiationException e) {
            log.error("Instantiation failed");
        } catch (ClassNotFoundException e) {
            log.error("Driver class not found");
        }
        for (int i = 0; i < poolSize; ++i) {
            try {
                Connection conn = DriverManager.getConnection(
                        DbSettingsManager.URL,
                        DbSettingsManager.USER,
                        DbSettingsManager.PASSWORD);
                ProxyConnection proxy = new ProxyConnection(conn);
                connectionQueue.add(proxy);
            } catch (SQLException e) {
                log.error("Can't create connection", e);
            }
        }
        if(connectionQueue.size() < minPoolSize){
            log.fatal("Pool size less then critical");
            throw new RuntimeException("Pool size less then critical");
        }
    }

    /**
     * Returns instance of connection pool. Creates one on the first call
     * @return initialized connection pool
     */
    public static ConnectionPool getInstance(){
        if (!instanceExists.get()) {
            try{
                lock.lock();
                if(instance == null){
                    instance = new ConnectionPool();
                    instanceExists.set(true);
                }
            }
            finally {
                lock.unlock();
            }
        }
        return instance;
    }

    /**
     * Returns available connection wrapper. If currently none of them is available, waits, until connection is
     * returned to the pool
     * @return fetched connection wrapper
     */
    public ProxyConnection getConnection(){
        ProxyConnection connection = null;
        try {
            connection = connectionQueue.take();
            givenAwayConnections.add(connection);
            /*CallableStatement cs = connection.prepareCall("{ call DBMS_SESSION.SET_IDENTIFIER('1111') }");
            cs.execute();
            cs.close();*/
        } catch (InterruptedException e) {
            log.error("Interrupted exception while waiting for connection", e);
        }
        return connection;
    }

    /**
     * Returns connection to available queue. Commits made changes, if autoCommit is set to false.
     * @param connection connection to return
     */
    public boolean returnConnection(ProxyConnection connection){
        boolean result = false;
        if(connection != null){
            try {
                if (!connection.getAutoCommit()) {
                    connection.commit();
                }
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                log.error("SQL exception while returning connection", e);
            }
            givenAwayConnections.remove(connection);
            connectionQueue.add(connection);
            result = true;
        }
        else{
            log.error("Trying to return null connection");
        }
        return result;
    }

    /**
     * Closes pool by deregistering drivers and closing all connections.
     * @see ProxyConnection#realClose()
     */
    public void closePool(){
        for(int i = 0; i < poolSize; ++i){
            try {
                ProxyConnection connection = connectionQueue.take();
                connection.realClose();
            } catch (InterruptedException e) {
                log.error("Thread interrupted", e);
                Thread.currentThread().interrupt();
            } catch (SQLException e) {
                log.error("Exception while closing connection", e);
            }
        }
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while(drivers.hasMoreElements()){
            Driver driver = drivers.nextElement();
            try{
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                log.error("Exception while deregister driver", e);
            }
        }
    }
}