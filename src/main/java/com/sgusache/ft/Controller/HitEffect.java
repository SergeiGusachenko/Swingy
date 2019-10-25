package com.sgusache.ft.Controller;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.abilities.OffensiveAbility;
import de.gurkenlabs.litiengine.abilities.effects.Effect;
import de.gurkenlabs.litiengine.abilities.effects.EffectTarget;
import de.gurkenlabs.litiengine.abilities.effects.EntityHitArgument;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.entities.ICombatEntity;
import de.gurkenlabs.litiengine.physics.Collision;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HitEffect extends Effect {
    private final OffensiveAbility offensiveAbility;

    public HitEffect(final OffensiveAbility ability) {
        super(ability, EffectTarget.ENEMY);
        this.offensiveAbility = ability;
    }

    @Override
    public void apply(final ICombatEntity affectedEntity) {
        if (affectedEntity.isIndestructible()) {
            return;
        }

        super.apply(affectedEntity);
        final int damage = this.offensiveAbility.getAttackDamage();
        final boolean killed = affectedEntity.hit(damage, this.getAbility());
        final EntityHitArgument arg = new EntityHitArgument(this.getAbility().getExecutor(), affectedEntity, damage,
                killed);

        this.offensiveAbility.entityHit(arg);
    }

    @Override
    protected Collection<ICombatEntity> getEntitiesInImpactArea(final Shape impactArea) {
        final List<ICombatEntity> entities = new ArrayList<>();
        for (final ICombatEntity entity : Game.world().environment().findCombatEntities(impactArea, x -> x instanceof Creature)) {
            final Point2D collCenterExecutor = new Point2D.Double(
                    this.getAbility().getExecutor().getCollisionBox().getCenterX(),
                    this.getAbility().getExecutor().getCollisionBox().getCenterY());
            final Point2D collCenterEntity = new Point2D.Double(entity.getCollisionBox().getCenterX(),
                    entity.getCollisionBox().getCenterY());
            final Line2D rayCast = new Line2D.Double(collCenterExecutor, collCenterEntity);

            boolean collision = false;
            for (final Rectangle2D collisionBox : Game.physics().getCollisionBoxes(Collision.STATIC)) {
                if (collisionBox.intersectsLine(rayCast)) {
                    collision = true;
                    break;
                }
            }
            if (!collision) {
                entities.add(entity);
            }
        }
        return entities;
    }

    protected OffensiveAbility getOffensiveAbility() {
        return this.offensiveAbility;
    }

}
