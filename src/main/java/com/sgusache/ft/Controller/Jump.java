package com.sgusache.ft.Controller;

import com.sgusache.ft.Model.Player;
import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.abilities.Ability;
import de.gurkenlabs.litiengine.abilities.AbilityOrigin;
import de.gurkenlabs.litiengine.abilities.effects.EffectApplication;
import de.gurkenlabs.litiengine.abilities.effects.EffectTarget;
import de.gurkenlabs.litiengine.abilities.effects.ForceEffect;
import de.gurkenlabs.litiengine.annotation.AbilityInfo;
import de.gurkenlabs.litiengine.entities.CollisionBox;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.ICombatEntity;
import de.gurkenlabs.litiengine.entities.IMobileEntity;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.graphics.animation.IAnimationController;
import de.gurkenlabs.litiengine.physics.Force;
import de.gurkenlabs.litiengine.physics.GravityForce;
import de.gurkenlabs.litiengine.resources.Resources;
import de.gurkenlabs.litiengine.util.Imaging;

import java.awt.image.BufferedImage;
import java.util.Optional;


@AbilityInfo(cooldown = 500, origin = AbilityOrigin.COLLISIONBOX_CENTER, duration = 300, value = 240, name = "jump")
public class Jump extends Ability {
    public Jump(Creature executor) {
        super(executor);
        this.addEffect(new JumpEffect(this));

    }

    private class JumpEffect extends ForceEffect {

        protected JumpEffect(Ability ability) {
            super(ability, ability.getAttributes().getValue().getCurrentValue().intValue(), EffectTarget.EXECUTINGENTITY);
        }

        @Override
        protected Force applyForce(IMobileEntity affectedEntity) {
            // create a new force and apply it to the player
            GravityForce force = new GravityForce(affectedEntity, this.getStrength(), Direction.UP);
            affectedEntity.getMovementController().apply(force);
            return force;
        }
        @Override
        public void apply(final ICombatEntity entity) {
            IAnimationController controller = Player.instance().getAnimationController();
            Spritesheet jump = Resources.spritesheets().get("Adven-jump-right");
            controller.add(new Animation(jump, false));
            Spritesheet rightJumpSprite;
            if(Player.instance().getFacingDirection() == Direction.LEFT) {
                BufferedImage rightJump = Imaging.flipSpritesHorizontally(jump);
                rightJumpSprite = Resources.spritesheets().load(rightJump, "Adven-jump-right", jump.getSpriteWidth(), jump.getSpriteHeight());
                controller.add(new Animation(rightJumpSprite, false));
                Player.instance().getAnimationController().playAnimation("Adven-jump-right");
            }
            else {
                rightJumpSprite = Resources.spritesheets().load(jump.getImage(), "Adven-jump-right", jump.getSpriteWidth(), jump.getSpriteHeight());
                controller.add(new Animation(rightJumpSprite, false));
                Player.instance().getAnimationController().playAnimation("Adven-jump-right");
            }
            super.apply(entity);
        }

        @Override
        protected boolean hasEnded(final EffectApplication appliance) {
            return super.hasEnded(appliance) || this.isTouchingCeiling();
        }

        /**
         * Make sure that the jump is cancelled when the entity touches a static collision box above it.
         *
         * @return True if the entity touches a static collision box above it.
         */
        private boolean isTouchingCeiling() {
            Optional<CollisionBox> opt = Game.world().environment().getCollisionBoxes().stream().filter(x -> x.getBoundingBox().intersects(this.getAbility().getExecutor().getBoundingBox())).findFirst();
            Game.world().environment().getCollisionBoxes().stream().filter(x -> x.getBoundingBox().intersects(this.getAbility().getExecutor().getBoundingBox())).findFirst();

//            Rectangle2D b = this.getAbility().getExecutor().getCollisionBox().getBounds2D();
//            System.out.println(b.getMinX());
//            System.out.println(b.getMinY());
//            if(b.getMinX()>= 632 && b.getMinX()<= 784 && b.getMinY()>= 721 && b.getMinY() <= 734)
//                exit(0);
            if (!opt.isPresent()) {
                return false;
            }
            CollisionBox box = opt.get();
            return box.getCollisionBox().getMaxY() <= this.getAbility().getExecutor().getCollisionBox().getMinY();
        }
    }
}