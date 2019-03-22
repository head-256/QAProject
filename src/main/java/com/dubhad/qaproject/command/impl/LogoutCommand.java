package com.dubhad.qaproject.command.impl;

import com.dubhad.qaproject.command.ActionCommand;
import com.dubhad.qaproject.command.CommandEnum;
import com.dubhad.qaproject.command.Router;
import com.dubhad.qaproject.resource.PathEnum;
import com.dubhad.qaproject.servlet.RequestWrapper;

import javax.servlet.http.HttpSession;

/**
 * Logs out
 * Required user status: any, role: any
 * @see CommandEnum
 */
public class LogoutCommand implements ActionCommand {
    /**
     * {@inheritDoc}
     */
    @Override
    public Router execute(RequestWrapper request) {
        Router router = new Router();
        router.changeAction();
        router.setPage(PathEnum.INDEX);
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        return router;
    }
}
