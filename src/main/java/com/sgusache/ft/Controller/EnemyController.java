package com.sgusache.ft.Controller;

import com.sgusache.ft.Model.Player;
import com.sgusache.ft.Model.Slime;
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

    public void isCollides(Player player, IEntity enemy)
    {
        if(player.getPlayerState() != "ATTACK")
            return;
        if(player.getCollisionBox().intersects(this.getEntity().getBoundingBox()) && Player.instance().getPlayerState() == "ATTACK")
            this.enemy.die();
    }
}

