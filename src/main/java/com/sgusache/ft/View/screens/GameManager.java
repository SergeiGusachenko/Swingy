package com.sgusache.ft.View.screens;

import com.sgusache.ft.Controller.AdventureLogic;
import com.sgusache.ft.Model.Player;
import de.gurkenlabs.litiengine.entities.*;
import com.sun.media.jfxmedia.events.PlayerStateEvent;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.Prop;
import de.gurkenlabs.litiengine.entities.Spawnpoint;
import de.gurkenlabs.litiengine.environment.CreatureMapObjectLoader;
import de.gurkenlabs.litiengine.environment.Environment;
import de.gurkenlabs.litiengine.environment.PropMapObjectLoader;
import de.gurkenlabs.litiengine.environment.tilemap.MapProperty;
import de.gurkenlabs.litiengine.graphics.Camera;
import de.gurkenlabs.litiengine.graphics.PositionLockCamera;
import de.gurkenlabs.litiengine.gui.GuiProperties;
import de.gurkenlabs.litiengine.gui.SpeechBubble;
import de.gurkenlabs.litiengine.gui.SpeechBubbleAppearance;
import de.gurkenlabs.litiengine.resources.Resources;

import java.awt.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class GameManager {
    public enum GameState {
        INGAME,
        MENU,
        INGAME_MENU,
        DEAD;
    }
    public static final Font GUI_FONT = Resources.fonts().get("/Users/sergeygusachenko/IdeaProjects/swingy/src/main/resources/fonts/menu.ttf").deriveFont(40f);
    //public static final Font GUI_FONT = Resources.fonts().get("menu.ttf").deriveFont(32f);
    public static final Font MENU_FONT = Resources.fonts().get("/Users/sergeygusachenko/IdeaProjects/swingy/src/main/resources/fonts/menu.ttf").deriveFont(40f);
    public static String START_LEVEL = "map";

    public static final SpeechBubbleAppearance SPEECH_BUBBLE_APPEARANCE = new SpeechBubbleAppearance(new Color(16, 20, 19), new Color(255, 255, 255, 150), new Color(16, 20, 19), 5);

    private static final Map<String, Runnable> startups = new ConcurrentHashMap<>();
     static {
        SPEECH_BUBBLE_APPEARANCE.setBackgroundColor2(new Color(255, 255, 255, 220));

        startups.put("map", () -> {

            Game.graphics().setBaseRenderScale(2.501f);
            Camera camera = new PositionLockCamera(Player.instance());
            camera.setClampToMap(true);
            Game.world().setCamera(camera);
        });

        startups.put("level1", () -> {
            Camera camera = new PositionLockCamera(Player.instance());
            camera.setClampToMap(true);
            Game.world().setCamera(camera);
        });

        startups.put("gameover", () -> {
            Game.graphics().setBaseRenderScale(1.2f);
            Game.world().loadEnvironment("gameover");
            Player.instance().die();
        });
    }
    private static GameState state = GameState.MENU;

    private GameManager() {
    }

    public static void init() {
        GuiProperties.setDefaultFont(GUI_FONT);
        Camera camera = new PositionLockCamera(Player.instance());
        camera.setClampToMap(true);
        Game.world().setCamera(camera);

        Game.world().addLoadedListener(e -> {
            if (e.getMap().getName().equals("end")) {
                return;
            }

            for (Prop prop : Game.world().environment().getProps()) {
                prop.setIndestructible(true);
            }

            Game.loop().perform(500, () -> Game.window().getRenderComponent().fadeIn(1000));
            Game.graphics().setBaseRenderScale(3.001f);
            if (startups.containsKey(e.getMap().getName())) {
                startups.get(e.getMap().getName()).run();
            }

            if (e.getMap().getName().equals("end")) {
                return;
            }

            setState(GameState.INGAME);
            Player.instance().getHitPoints().setToMaxValue();
            Player.instance().setIndestructible(false);
            Player.instance().setCollision(true);
            // spawn the player instance on the spawn point with the name "enter"
            Spawnpoint enter = e.getSpawnpoint("enter");
            if (enter != null) {
                enter.spawn(Player.instance());
            }
        });
    }

    public static String getCity(String levelName) {
        return Game.world().getEnvironment(levelName).getMap().getStringValue(MapProperty.MAP_TITLE);
    }

    public static String getCurrentCity() {
        return getCity(Game.world().environment().getMap().getName());
    }

    public static GameState getState() {
        return state;
    }

    public static void setState(GameState state) {
        GameManager.state = state;

    }
}
