package com.dubhad.qaproject.command;

import com.dubhad.qaproject.servlet.RequestWrapper;
import lombok.extern.log4j.Log4j2;

/**
 * Factory of command implementations
 */
@Log4j2
public class ActionFactory {
    /**
     * Defines command by request parameter
     * @param request request to fetch command name from
     * @return Actual command implementation
     * @see CommandEnum
     */
    public ActionCommand defineCommand(RequestWrapper request) {
        ActionCommand current;
        String command = request.getParameter(RequestParamAttr.COMMAND);
        log.info(command);
        try {
            CommandEnum currentEnum = CommandEnum.valueOf(command.toUpperCase());
            log.info(currentEnum);
            current = currentEnum.getActionCommand();
        } catch (IllegalArgumentException e) {
            log.fatal("Unexpected command");
            throw new RuntimeException(e);
        }
        return current;
    }
}
