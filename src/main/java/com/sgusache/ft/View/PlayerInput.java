package com.sgusache.ft.View;

import com.sgusache.ft.Controller.Jump;
import com.sgusache.ft.Model.Player;
import de.gurkenlabs.litiengine.input.Input;

import java.awt.event.KeyEvent;

public final class PlayerInput {
    private PlayerInput() {
    }

    public static void init() {
        // make the game exit upon pressing ESCAPE (by default there is no such key binding and the window needs to be shutdown otherwise, e.g. ALT-F4 on Windows)
        Input.keyboard().onKeyPressed(KeyEvent.VK_ESCAPE, e -> System.exit(0));
    }
}