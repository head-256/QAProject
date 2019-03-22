package com.dubhad.qaproject.command;

import com.dubhad.qaproject.servlet.RequestWrapper;
import lombok.extern.log4j.Log4j2;

/**
 * Factory of ajax command implementations
 */
@Log4j2
public class AjaxFactory {
    /**
     * Defines command by request parameter
     * @param request request to fetch command name from
     * @return Actual command implementation
     * @see AjaxCommandEnum
     */
    public AjaxCommand defineCommand(RequestWrapper request) {
        AjaxCommand current;
        String command = request.getParameter(RequestParamAttr.COMMAND);
        try {
            AjaxCommandEnum currentEnum = AjaxCommandEnum.valueOf(command.toUpperCase());
            log.debug(currentEnum);
            current = currentEnum.getAjaxCommand();
        } catch (IllegalArgumentException e) {
            log.error("Unexpected command");
            throw new RuntimeException(e);
        }
        return current;
    }
}
