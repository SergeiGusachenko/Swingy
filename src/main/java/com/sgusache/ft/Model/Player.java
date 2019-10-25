package com.sgusache.ft.Model;

import com.sgusache.ft.Controller.Attack;
import com.sgusache.ft.Controller.Jump;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.IUpdateable;
import de.gurkenlabs.litiengine.annotation.Action;
import de.gurkenlabs.litiengine.annotation.CollisionInfo;
import de.gurkenlabs.litiengine.annotation.EntityInfo;
import de.gurkenlabs.litiengine.annotation.MovementInfo;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.input.KeyboardEntityController;
import de.gurkenlabs.litiengine.input.PlatformingMovementController;
import de.gurkenlabs.litiengine.physics.Collision;

import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;



@EntityInfo(width = 16, height = 16)
@MovementInfo(velocity = 70)
@CollisionInfo(collisionBoxWidth = 16, collisionBoxHeight = 27, collision = true)
public class Player extends Creature implements IUpdateable {
    private String playerState;
    private int forceLevel;

    public static final int MAX_ADDITIONAL_JUMPS = 1;

    private static Player instance;

    private final Jump jump;

    private int consecutiveJumps;
    private Attack attack;
    private Player() {
        super("Adven");
        this.forceLevel = 3;
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
        Input.keyboard().onKeyPressed(KeyEvent.VK_N, e -> this.attack());
    }

    public void setState(String state)
    {
        this.playerState = state;
    }

    public String getPlayerState()
    {
        return this.playerState;
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
    }

    /**
     * Checks whether this instance can currently jump and then performs the <code>Jump</code> ability.
     * <p>
     * <i>Note that the name of this methods needs to be equal to {@link PlatformingMovementController#JUMP}} in order for the controller to be able to
     * use this method. <br>
     * Another option is to explicitly specify the <code>Action.name()</code> attribute on the annotation.</i>
     * </p>
     */
    @Action(description = "This performs the jump ability for the player's entity.", name = "jump")
    public void jump() {
        if (this.consecutiveJumps >= MAX_ADDITIONAL_JUMPS || !this.jump.canCast()) {
            return;
        }
        this.jump.cast();
        this.consecutiveJumps++;
    }
    @Action(description = "This performs the jump ability for the player's entity.", name = "attack")
    public void attack() {
        new Attack(this).attack();

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
            instance.die();

        return Game.physics().collides(groundCheck, Collision.STATIC);
    }
}
