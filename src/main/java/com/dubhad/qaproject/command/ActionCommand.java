package com.dubhad.qaproject.command;

import com.dubhad.qaproject.servlet.RequestWrapper;

/**
 * Interface, that specifies method for all commands, that could be called from controller. All commands must implement
 * it
 */
public interface ActionCommand {
    /**
     * Method, called by controller.
     * @param request http request to fetch parameters from
     * @return router objects, that specifies next pages and crossing method
     */
    Router execute(RequestWrapper request);
}
