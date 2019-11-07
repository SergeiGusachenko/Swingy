package com.sgusache.ft.View.screens;

import com.sgusache.ft.Controller.AdventureLogic;
import com.sgusache.ft.Model.Player;
import com.sgusache.ft.View.PlayerInput;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.graphics.ImageRenderer;
import de.gurkenlabs.litiengine.graphics.TextRenderer;
import de.gurkenlabs.litiengine.gui.SpeechBubbleAppearance;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;
import de.gurkenlabs.litiengine.gui.screens.Screen;
import de.gurkenlabs.litiengine.resources.Resources;
import de.gurkenlabs.litiengine.util.Imaging;
import de.gurkenlabs.litiengine.util.MathUtilities;

import java.awt.*;
import java.awt.image.BufferedImage;


public class MenuScreen extends Screen implements IUpdateable {

    public long lastPlayed;
    private KeyboardMenu mainMenu;
    private KeyboardMenu playMenu;

    private boolean renderInstructions;
    public MenuScreen() {
        super("Menu");
    }
    private void exit() {
        System.exit(0);
    }
    @Override
    protected void initializeComponents() {
        final double centerX = Game.window().getResolution().getWidth() / 2.0;
        final double centerY = Game.window().getResolution().getHeight() * 1 / 2;
        final double buttonWidth = 450;

        this.mainMenu = new KeyboardMenu(centerX - buttonWidth / 2, centerY * 1.3, buttonWidth, centerY / 2, "Play", "Exit");
        this.playMenu = new KeyboardMenu(centerX - buttonWidth / 2, centerY * 1.3, buttonWidth, centerY / 2, "Adven", "Knight");
       this.getComponents().add(this.playMenu);
        this.getComponents().add(this.mainMenu);

        this.mainMenu.onChange(c -> {
            this.renderInstructions = c == 1;
        });
        this.playMenu.onChange(c -> {
            this.renderInstructions = c == 1;
        });
        this.playMenu.setVisible(false);
        this.mainMenu.onConfirm(c -> {
            switch (c.intValue()) {
                case 0:
                    this.mainMenu.suspend();
                    this.playMenu.onConfirm(d ->{
                        switch (c.intValue())
                        {
                            case 0:
                                Player.instance().setName("adven");
                                this.startGame();
                                break;
                            case 1:
                                Player.instance().setName("knight");
                                this.startGame();
                                break;
                        }
                    });
                    //this.startGame();
                    break;
                case 1:
                    this.exit();
                    break;
                default:
                    break;
            }
        });
    }



    @Override
    public void prepare() {
        this.mainMenu.setEnabled(true);
        super.prepare();
        Game.loop().attach(this);
        Game.window().getRenderComponent().setBackground(Color.BLACK);
        Game.graphics().setBaseRenderScale(3f * Game.window().getResolutionScale());
        this.mainMenu.incFocus();
        Game.world().loadEnvironment("map");
        Game.world().camera().setFocus(Game.world().environment().getCenter());
    }

    @Override
    public void render(final Graphics2D g) {
        Game.world().environment().render(g);
        final double centerX = Game.window().getResolution().getWidth() / 2.0;
        final double logoY = Game.window().getResolution().getHeight() * 1 / 12;
        // ImageRenderer.render(g, LOGO_TEXT, logoX, logoY);
        g.setFont(GameManager.GUI_FONT);
        g.setColor(Color.WHITE);
        super.render(g);
    }

    private void startGame() {
        this.mainMenu.setEnabled(false);
        this.playMenu.setEnabled(false);
        this.playMenu.setVisible(false);
        this.mainMenu.setVisible(false);
        GameManager.init();
        //Game.window().getRenderComponent().fadeOut(2500);
            Game.world().loadEnvironment(GameManager.START_LEVEL);
            GameManager.setState(GameManager.GameState.INGAME);
    }

    @Override
    public void suspend() {
        super.suspend();
        Game.loop().detach(this);
    }

    @Override
    public void update() {
        if (this.lastPlayed == 0) {
            this.lastPlayed = Game.loop().getTicks();
        }
    }

}
