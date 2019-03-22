package com.dubhad.qaproject.command.impl;

import com.dubhad.qaproject.command.ActionCommand;
import com.dubhad.qaproject.command.CommandEnum;
import com.dubhad.qaproject.command.Router;
import com.dubhad.qaproject.resource.PathEnum;
import com.dubhad.qaproject.servlet.RequestWrapper;

/**
 * Shows login form
 * Required user status: any, role: any
 * @see CommandEnum
 */
public class ShowLoginCommand implements ActionCommand {
    /**
     * {@inheritDoc}
     */
    @Override
    public Router execute(RequestWrapper request) {
        Router router = new Router();
        router.setPage(PathEnum.LOGIN);
        return router;
    }
}
