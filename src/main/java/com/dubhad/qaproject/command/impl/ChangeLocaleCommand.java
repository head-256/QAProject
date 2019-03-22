package com.dubhad.qaproject.command.impl;

import com.dubhad.qaproject.command.ActionCommand;
import com.dubhad.qaproject.command.CommandEnum;
import com.dubhad.qaproject.command.Router;
import com.dubhad.qaproject.servlet.RequestWrapper;
import com.dubhad.qaproject.util.LocaleUtil;
import com.dubhad.qaproject.command.RequestParamAttr;

import javax.servlet.jsp.jstl.core.Config;
import java.util.Locale;

/**
 * Changes locale of current session to specified
 * Required user status: any, role: any
 * @see CommandEnum
 */
public class ChangeLocaleCommand implements ActionCommand {
    /**
     * {@inheritDoc}
     * Required request parameter: locale
     */
    @Override
    public Router execute(RequestWrapper request) {
        Router router = new Router();
        String localeString = request.getParameter(RequestParamAttr.LOCALE);
        Locale locale = LocaleUtil.getLocale(localeString);
        Config.set(request.getSession(), Config.FMT_LOCALE, locale);
        router.setReturnBack(true);
        return router;
    }
}
