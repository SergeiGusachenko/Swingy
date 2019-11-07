package com.sgusache.ft.View.screens;

import de.gurkenlabs.litiengine.graphics.TextRenderer;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;

import java.awt.*;

public class GameOver extends GameScreen {
    public GameOver() {
        super("gameover");
    }

    @Override
    public void render(final Graphics2D g) {
        super.render(g);
        g.setColor(Color.RED);
        TextRenderer.render(g, "Game Over", 100, 100);
    }
}
