package com.sgusache.ft.Model;

import com.sgusache.ft.Controller.Attack;
import com.sgusache.ft.Controller.Jump;
import com.sun.media.jfxmedia.events.PlayerStateEvent;
import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.annotation.Action;
import de.gurkenlabs.litiengine.annotation.CollisionInfo;
import de.gurkenlabs.litiengine.annotation.EntityInfo;
import de.gurkenlabs.litiengine.annotation.MovementInfo;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.graphics.Camera;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.graphics.animation.IAnimationController;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.input.KeyboardEntityController;
import de.gurkenlabs.litiengine.input.PlatformingMovementController;
import de.gurkenlabs.litiengine.physics.Collision;
import de.gurkenlabs.litiengine.resources.Resources;
import de.gurkenlabs.litiengine.util.Imaging;

import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@EntityInfo(width = 16, height = 16)
@MovementInfo(velocity = 70)
@CollisionInfo(collisionBoxWidth = 16, collisionBoxHeight = 27, collision = true)
public class Player extends Creature implements IUpdateable {
    private String playerState;
    private int forceLevel;
    private String name;
    private static final Map<String, Runnable> startups = new ConcurrentHashMap<>();

    public static final int MAX_ADDITIONAL_JUMPS = 1;

    private static Player instance;

    private final Jump jump;
    private int killFirst;
    private int consecutiveJumps;
    private Attack attack;
    private Player() {
        super("Adven");

        this.name = name;
        this.forceLevel = 3;
        this.killFirst = 0;
        this.playerState = "IDLE";
        // setup movement controller
        this.addController(new PlatformingMovementController<>(this));
        // setup the player's abilities
        this.attack = new Attack(this);
        this.jump = new Jump(this);
        KeyboardEntityController<Player> movementController = new KeyboardEntityController<>(this);
        movementController.addUpKey(KeyEvent.VK_UP);
        movementController.addDownKey(KeyEvent.VK_DOWN);
        movementController.addLeftKey(KeyEvent.VK_LEFT);
        movementController.addRightKey(KeyEvent.VK_RIGHT);

        /// Attack
            Input.keyboard().onKeyPressed(KeyEvent.VK_N, e -> this.attack("adven-attack-right"));
            Input.keyboard().onKeyPressed(KeyEvent.VK_M, e -> this.attack("adven-attack2-right"));
    }

    public void incrementKillFirst()
    {
        this.killFirst++;
    }
    public void setState(String state)
    {
        this.playerState = state;
    }

    public String getPlayerState()
    {
        return this.playerState;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public static Player instance() {
        if (instance == null) {
            instance = new Player();
        }
        return instance;
    }
    @Override
    public void update() {
        // reset the number of consecutive jumps when touching the ground
        if (this.isTouchingGround()) {
            this.consecutiveJumps = 0;
        }
        //Game.world().loadEnvironment("map");
    }

    @Action(description = "This performs the jump ability for the player's entity.", name = "jump")
    public void jump() {
        if (this.consecutiveJumps >= MAX_ADDITIONAL_JUMPS || !this.jump.canCast()) {
            return;
        }
        this.jump.cast();
        this.consecutiveJumps++;
    }
    @Action(description = "This performs the jump ability for the player's entity.", name = "attack")
    public void attack(String s) {
        new Attack(this).attack(s);
    }

    public void animation(String s)
    {
        IAnimationController controller = Player.instance().getAnimationController();
        Spritesheet jump = Resources.spritesheets().get(s);
        controller.add(new Animation(jump, false));
        Spritesheet rightAttackSprite;
        if (Player.instance().getFacingDirection() == Direction.LEFT) {
            BufferedImage rightJump = Imaging.flipSpritesHorizontally(jump);
            rightAttackSprite = Resources.spritesheets().load(rightJump, s, jump.getSpriteWidth(), jump.getSpriteHeight());
            controller.add(new Animation(rightAttackSprite, false));
            Player.instance().getAnimationController().playAnimation(s);
        } else {
            rightAttackSprite = Resources.spritesheets().load(jump.getImage(), s, jump.getSpriteWidth(), jump.getSpriteHeight());
            controller.add(new Animation(rightAttackSprite, false));
            Player.instance().getAnimationController().playAnimation(s);
        }
    }
    public int getForceLevel()
    {
        return this.forceLevel;
    }
    private boolean isTouchingGround() {
        // the idea of this ground check is to extend the current collision box by
        // one pixel and see if
        // a) it collides with any static collision box
        Rectangle2D groundCheck = new Rectangle2D.Double(this.getCollisionBox().getX(), this.getCollisionBox().getY(), this.getCollisionBoxWidth(), this.getCollisionBoxHeight() + 1);

        // b) it collides with the map's boundaries
        if (groundCheck.getMaxY() > Game.physics().getBounds().getMaxY()) {
            return true;
        }
        Rectangle2D b =instance.getBoundingBox().getBounds2D();
        //c) if touch die collision boxd
        if(b.getMinX()>= 632 && b.getMinX()<= 784 && b.getMinY()>= 721 && b.getMinY() <= 734)
        {
            Camera cam = new Camera();
            cam.setFocus(Game.world().environment().getCenter());
            System.out.println("GAme OVer");
            Game.world().loadEnvironment("gameover");
            instance.die();
        }

        return Game.physics().collides(groundCheck, Collision.STATIC);
    }
}
