package com.dubhad.qaproject.command;

import com.dubhad.qaproject.resource.PathEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * Utility class, for defining concrete page and jump type in commands
 */
@Getter
public class Router {
    @Setter
    private PathEnum page;
    @Setter
    private boolean returnBack;
    private Action action = Action.FORWARD;

    /**
     * Changes jump type from default (forward) to redirect
     */
    public void changeAction(){
        this.action = Action.REDIRECT;
    }

    public enum Action{
        REDIRECT,
        FORWARD
    }
}
