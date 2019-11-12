package com.sgusache.ft.Controller;

import com.sgusache.ft.Model.Player;
import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.IEntity;
import de.gurkenlabs.litiengine.entities.ai.IBehaviorController;
import de.gurkenlabs.litiengine.graphics.Camera;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.graphics.animation.IAnimationController;
import de.gurkenlabs.litiengine.physics.GravityForce;
import de.gurkenlabs.litiengine.resources.Resources;
import de.gurkenlabs.litiengine.util.Imaging;
import de.gurkenlabs.litiengine.util.MathUtilities;

import java.awt.image.BufferedImage;


public class EnemyController implements IBehaviorController {
    private final Creature enemy;
    private long directionChanged;
    private long nextDirectionChage;
    private Direction direction;
    private int touch_counter;
    public GravityForce force;
    public EnemyController(Creature enemy) {

        this.force = null;
        this.enemy = enemy;
        this.touch_counter = 0;
    }

    @Override
    public IEntity getEntity() {
        return this.enemy;
    }

    @Override
    public void update() {
            int i = 0;
            final long timeSinceDirectionChange = Game.time().since(this.directionChanged);
            if (timeSinceDirectionChange > this.nextDirectionChage) {
                direction = this.direction == Direction.LEFT ? Direction.RIGHT : Direction.LEFT;
                this.directionChanged = Game.time().now();
                this.nextDirectionChage = MathUtilities.randomInRange(1000, 2000);
                i++;
            }
            Game.physics().move(this.enemy,this.direction,this.enemy.getTickVelocity());
            isCollides(Player.instance(),this.getEntity());
        if (this.enemy.isDead())
            return;
    }


    public void animation(String s)
    {
        IAnimationController controller = this.getEntity().getAnimationController();
        Spritesheet die = Resources.spritesheets().get(s);
        controller.add(new Animation(die, false));
        Spritesheet rightDieSprite;
        if (Player.instance().getFacingDirection() == Direction.LEFT) {
            BufferedImage dieRight = Imaging.flipSpritesHorizontally(die);
            rightDieSprite = Resources.spritesheets().load(dieRight, s, die.getSpriteWidth(), die.getSpriteHeight());
            controller.add(new Animation(rightDieSprite , false));
            this.getEntity().getAnimationController().playAnimation(s);
        } else {
            rightDieSprite = Resources.spritesheets().load(die.getImage(), s, die.getSpriteWidth(), die.getSpriteHeight());
            controller.add(new Animation(rightDieSprite , false));
            this.getEntity().getAnimationController().playAnimation(s);
        }
    }

    public int getTouch_counter() {
        return touch_counter;
    }

    public void knightAttack(String s )
    {
        IAnimationController controller = this.getEntity().getAnimationController();
        Spritesheet die = Resources.spritesheets().get(s);
        controller.add(new Animation(die, false));
        Spritesheet rightDieSprite;
        if (Player.instance().getFacingDirection() == this.enemy.getFacingDirection()) {
            BufferedImage dieRight = Imaging.flipSpritesHorizontally(die);
            rightDieSprite = Resources.spritesheets().load(dieRight, s, die.getSpriteWidth(), die.getSpriteHeight());
            controller.add(new Animation(rightDieSprite , false));
            this.getEntity().getAnimationController().playAnimation(s);
        } else {
            rightDieSprite = Resources.spritesheets().load(die.getImage(), s, die.getSpriteWidth(), die.getSpriteHeight());
            controller.add(new Animation(rightDieSprite , false));
            this.getEntity().getAnimationController().playAnimation(s);
        }
    }

    public void fatality()
    {
        knightAttack("knight-attack-right");
        Game.world().camera().shake(1, 1, 1);

    }

    public void isCollides(Player player, IEntity enemy)
    {
        if(player.getPlayerState() != "ATTACK")
        {
            if(player.getCollisionBox().intersects(this.getEntity().getBoundingBox()))
            {
                if(this.getEntity().getName() == "Knight")
                {
                   fatality();
                }

                Player.instance().animation("adven-hurt-right");

                this.touch_counter++;
                if(touch_counter % 3 == 0 && force != null)
                    this.force.end();
                if(this.touch_counter > 100)
                {
                    Camera cam = new Camera();
                    cam.setFocus(Game.world().environment().getCenter());
                    Game.world().loadEnvironment("gameover");
                    Player.instance().die();
                }

            }
        }
        if(player.getCollisionBox().intersects(this.getEntity().getBoundingBox()) && Player.instance().getPlayerState() == "ATTACK")
        {
            System.out.println(this.getEntity().getName());
            if(this.getEntity().getName() == "Slime")
            {
                animation("slime-die-left");
                this.enemy.die();
            }
            if(this.getEntity().getName() == "Knight")
            {
                animation("knight-die-right");
                Camera cam = new Camera();
                cam.setFocus(Game.world().environment().getCenter());
                Game.world().loadEnvironment("win");
                Game.graphics().setBaseRenderScale(1.501f);
                this.enemy.die();
            }

            Player.instance().incrementKillFirst();
            this.getEntity().getAnimationController().detach();
        }
    }
}

