package com.dubhad.qaproject.command.impl;

import com.dubhad.qaproject.command.ActionCommand;
import com.dubhad.qaproject.command.CommandEnum;
import com.dubhad.qaproject.command.Router;
import com.dubhad.qaproject.resource.PathEnum;
import com.dubhad.qaproject.servlet.RequestWrapper;

/**
 * Shows signup command
 * Required user status: any, role: any
 * @see CommandEnum
 */
public class ShowSignupCommand implements ActionCommand {
    /**
     * {@inheritDoc}
     */
    @Override
    public Router execute(RequestWrapper request) {
        Router router = new Router();
        router.setPage(PathEnum.SIGNUP);
        return router;
    }
}
