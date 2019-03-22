package com.dubhad.qaproject.command.impl;

import com.dubhad.qaproject.command.ActionCommand;
import com.dubhad.qaproject.command.CommandEnum;
import com.dubhad.qaproject.command.Router;
import com.dubhad.qaproject.resource.PathEnum;
import com.dubhad.qaproject.servlet.RequestWrapper;

/**
 * Shows form for creating question
 * Required user status: available, role: any
 * @see CommandEnum
 */
public class ShowQuestionCreationFromCommand implements ActionCommand {
    /**
     * {@inheritDoc}
     */
    @Override
    public Router execute(RequestWrapper request) {
        Router router = new Router();
        router.setPage(PathEnum.CREATE_QUESTION);
        return router;
    }
}
