package com.sgusache.ft.Controller;
import com.sgusache.ft.Model.Player;
import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.abilities.AbilityOrigin;
import de.gurkenlabs.litiengine.abilities.OffensiveAbility;
import de.gurkenlabs.litiengine.annotation.AbilityInfo;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.graphics.animation.Animation;
import de.gurkenlabs.litiengine.graphics.animation.IAnimationController;
import de.gurkenlabs.litiengine.resources.Resources;
import de.gurkenlabs.litiengine.util.Imaging;
import java.awt.image.BufferedImage;

@AbilityInfo(name = "Strike", cooldown = 700, range = 0, impact = 15, impactAngle = 360, value = 1, duration = 400, multiTarget = true, origin = AbilityOrigin.DIMENSION_CENTER)
public class Attack extends OffensiveAbility {

    private final HitEffect hitEffect;
    public int forceLevel;
    public Attack(Creature executor) {
        super(executor);
        this.forceLevel = 3;
        this.hitEffect = new HitEffect(this);
        this.addEffect(this.hitEffect);
    }
    public void attack()
    {
        Player.instance().setState("ATTACK");
        IAnimationController controller = Player.instance().getAnimationController();
        Spritesheet jump = Resources.spritesheets().get("adven-attack-right");
        controller.add(new Animation(jump, false));
        Spritesheet rightAttackSprite;
        if (Player.instance().getFacingDirection() == Direction.LEFT) {
            BufferedImage rightJump = Imaging.flipSpritesHorizontally(jump);
            rightAttackSprite = Resources.spritesheets().load(rightJump, "adven-attack-right", jump.getSpriteWidth(), jump.getSpriteHeight());
            controller.add(new Animation(rightAttackSprite, false));
            Player.instance().getAnimationController().playAnimation("adven-attack-right");
        } else {
            rightAttackSprite = Resources.spritesheets().load(jump.getImage(), "adven-attack-right", jump.getSpriteWidth(), jump.getSpriteHeight());
            controller.add(new Animation(rightAttackSprite, false));
            Player.instance().getAnimationController().playAnimation("adven-attack-right");
        }
        Player.instance().setState("IDLE");
    }
    public HitEffect getHitEffect() { return this.hitEffect; }
}

/*
@AbilityInfo(cooldown = 500, origin = AbilityOrigin.COLLISIONBOX_CENTER, duration = 300, value = 240, name = "attack")
public class Attack extends Ability {
    public int forceLevel;
    public Attack(Creature executor) {
        super(executor);
        this.forceLevel = 3;
        this.addEffect(new AttackEffect(this));
    }

    private class AttackEffect extends ForceEffect {

        protected AttackEffect(Ability ability) {
            super(ability, ability.getAttributes().getValue().getCurrentValue().intValue(), EffectTarget.EXECUTINGENTITY);

        }

        @Override
        protected Force applyForce(IMobileEntity affectedEntity) {
            // create a new force and apply it to the player
            GravityForce force = new GravityForce(affectedEntity, this.getStrength()/forceLevel, Player.instance().getFacingDirection());
            affectedEntity.getMovementController().apply(force);
            return force;
        }

        @Override
        public void apply(final ICombatEntity entity) {
            IAnimationController controller = Player.instance().getAnimationController();
            Spritesheet jump = Resources.spritesheets().get("Adven-attack-right");
            controller.add(new Animation(jump, false));
            Spritesheet rightAttackSprite;
            if (Player.instance().getFacingDirection() == Direction.LEFT) {
                BufferedImage rightJump = Imaging.flipSpritesHorizontally(jump);
                rightAttackSprite = Resources.spritesheets().load(rightJump, "Adven-attack-right", jump.getSpriteWidth(), jump.getSpriteHeight());
            } else {
                rightAttackSprite = Resources.spritesheets().load(jump.getImage(), "Adven-attack-right", jump.getSpriteWidth(), jump.getSpriteHeight());

            }
            controller.add(new Animation(rightAttackSprite, false));
            Player.instance().getAnimationController().playAnimation("Adven-attack-right");
            super.apply(entity);
        }

        @Override
        protected boolean hasEnded(final EffectApplication appliance) {
            return super.hasEnded(appliance);
        }
    }
}
*/