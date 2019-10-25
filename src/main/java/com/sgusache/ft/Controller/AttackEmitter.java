package com.sgusache.ft.Controller;

import de.gurkenlabs.litiengine.annotation.EmitterInfo;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.graphics.emitters.AnimationEmitter;

import java.awt.geom.Point2D;

@EmitterInfo(particleMinTTL = 100, particleMaxTTL = 100, emitterTTL = 150, maxParticles = 1)
public class AttackEmitter extends AnimationEmitter {

    public AttackEmitter(Spritesheet spriteSheet, Point2D origin) {
        super(spriteSheet, origin);
    }
}
