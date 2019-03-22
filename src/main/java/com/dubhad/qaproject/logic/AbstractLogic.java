package com.dubhad.qaproject.logic;

import com.dubhad.qaproject.dao.TransactionHelper;

/**
 * Class, that provides basic methods for all Logic classes. All of them have to extend this class
 */
public abstract class AbstractLogic implements AutoCloseable {
    private TransactionHelper helper = new TransactionHelper();

    /**
     * Ends transaction of transaction helper, linked with this object
     * @see TransactionHelper#endTransaction()
     */
    @Override
    public void close(){
        helper.endTransaction();
    }

    protected TransactionHelper getHelper(){
        return helper;
    }
}
