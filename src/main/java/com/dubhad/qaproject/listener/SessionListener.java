package com.dubhad.qaproject.listener;

import com.dubhad.qaproject.util.LocaleUtil;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.jsp.jstl.core.Config;
import java.util.Locale;

/**
 * Session listener, that adds default locale. Locale is specified in configuration file
 */
public class SessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        Locale locale = LocaleUtil.getLocale();
        Config.set(event.getSession(), Config.FMT_LOCALE, locale);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

    }
}