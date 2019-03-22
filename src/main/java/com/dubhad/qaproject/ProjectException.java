package com.dubhad.qaproject;

/**
 * The only user-defined exception in app.
 * Motivation for this decision - nothing is done with exceptions on middle levels, so there is no need to create
 * additional objects to wrap low-level exceptions
 */
public class ProjectException extends Exception{
    public ProjectException(String message, Throwable cause) {
            super(message, cause);
        }

    public ProjectException(Throwable cause) {
        super(cause);
    }

    public ProjectException(String message) {
        super(message);
    }
}