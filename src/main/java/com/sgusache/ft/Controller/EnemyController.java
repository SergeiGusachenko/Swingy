package com.sgusache.ft.Controller;

import com.sgusache.ft.Model.Player;
import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.IEntity;
import de.gurkenlabs.litiengine.entities.ai.IBehaviorController;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.graphics.animation.IAnimationController;
import de.gurkenlabs.litiengine.resources.Resources;
import de.gurkenlabs.litiengine.util.Imaging;
import de.gurkenlabs.litiengine.util.MathUtilities;

import java.awt.image.BufferedImage;


public class EnemyController implements IBehaviorController {
    private final Creature enemy;
    private long directionChanged;
    private long nextDirectionChage;
    private Direction direction;

    public EnemyController(Creature enemy) {
        this.enemy = enemy;
    }

    @Override
    public IEntity getEntity() {
        return this.enemy;
    }

    @Override
    public void update() {
        if (this.enemy.isDead()) {

            return;
        }
        int i = 0;
        final long timeSinceDirectionChange = Game.time().since(this.directionChanged);
        if (timeSinceDirectionChange > this.nextDirectionChage) {
            direction = this.direction == Direction.LEFT ? Direction.RIGHT : Direction.LEFT;
            this.directionChanged = Game.time().now();
            this.nextDirectionChage = MathUtilities.randomInRange(1000, 2000);
            i++;
        }
        this.getEntity().setAngle(this.direction.toAngle());
        Game.physics().move(this.enemy, this.enemy.getTickVelocity());
        isCollides(Player.instance(),this.getEntity());

    }

    public void animation(String s)
    {
        IAnimationController controller = this.getEntity().getAnimationController();
        Spritesheet die = Resources.spritesheets().get(s);
        controller.add(new Animation(die, false));
        Spritesheet rightDieSprite;
        if (Player.instance().getFacingDirection() == Direction.RIGHT) {
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

    public void isCollides(Player player, IEntity enemy)
    {
        if(player.getPlayerState() != "ATTACK")
        {
            if(player.getCollisionBox().intersects(this.getEntity().getBoundingBox()))
                Player.instance().animation("adven-hurt-right");
        }
        if(player.getCollisionBox().intersects(this.getEntity().getBoundingBox()) && Player.instance().getPlayerState() == "ATTACK")
        {
            animation("slime-die-left");
            Player.instance().incrementKillFirst();
            this.getEntity().getAnimationController().detach();
            this.enemy.die();
        }
    }
}

