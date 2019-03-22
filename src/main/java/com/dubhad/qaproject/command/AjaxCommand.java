package com.dubhad.qaproject.command;

import com.dubhad.qaproject.servlet.RequestWrapper;

import java.util.Map;

/**
 * Interface, that specifies method for all ajax commands, that could be called from ajax controller. All commands must
 * implement it
 */
public interface AjaxCommand {
    /**
     * Method, called by controller.
     * @param request http request to fetch parameters from
     * @return Map of response strings, that should be passed to front-end
     */
    Map<String, String> execute(RequestWrapper request);
}
