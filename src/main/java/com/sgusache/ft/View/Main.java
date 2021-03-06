package com.sgusache.ft.View;
import com.sgusache.ft.Controller.AdventureLogic;
import com.sgusache.ft.Model.Player;
import com.sgusache.ft.View.screens.GameManager;
import com.sgusache.ft.View.screens.IngameScreen;
import com.sgusache.ft.View.screens.MenuScreen;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.Camera;
import de.gurkenlabs.litiengine.graphics.PositionLockCamera;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.resources.Resources;

import java.awt.*;

public class Main {
//    public static void main(String[] args) {
//        // set meta information about the game
//        Game.info().setName("Adven");
//        Game.info().setSubTitle("");
//        Game.info().setVersion("v0.0.2");
//        Game.info().setDescription("An example 2D platformer with shooter elements made in the LITIengine");
//        // init the game infrastructure
//        Game.init(args);
//        Game.graphics().setBaseRenderScale(3.001f);
//        Game.window();
//       // Game.screens().add(new MenuScreen());
//        // load data from the utiLITI game file
//        Resources.load("game.litidata");
//        PlayerInput.init();
//        AdventureLogic.init();
//        //Game.screens().add(new MenuScreen());
//        Game.screens().add(new IngameScreen());
//        Game.screens().display("map");
//        Game.world().loadEnvironment("map");
//        Game.start();
//
//    }
//

    public static void main(String[] args) {
        // set meta information about the game
        Game.info().setName("Adven");
        Game.info().setSubTitle("");
        Game.info().setVersion("v0.0.2");
        // init the game infrastructure
        Game.init(args);
        // set the icon for the game (this has to be done after initialization because the ScreenManager will not be present otherwise)
        Game.graphics().setBaseRenderScale(2.701f);
        Game.window();
        // load data from the utiLITI game file
        Resources.load("game.litidata");
        PlayerInput.init();
        AdventureLogic.init();
        // add the screens that will help you organize the different states of your game
        Game.screens().add(new MenuScreen());
        Game.screens().add(new IngameScreen());
        // load the first level (resources for the map were implicitly loaded from the game file)

        Game.world().loadEnvironment("Menu");
        Camera cam = new Camera();
        cam.setFocus(Game.world().environment().getCenter());
        Game.world().setCamera(cam);
        Game.start();
    }
}