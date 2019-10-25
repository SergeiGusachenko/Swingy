package com.sgusache.ft.Model;

import com.sgusache.ft.Controller.EnemyController;
import de.gurkenlabs.litiengine.annotation.AnimationInfo;
import de.gurkenlabs.litiengine.annotation.EntityInfo;
import de.gurkenlabs.litiengine.entities.Creature;

@EntityInfo(width = 16, height = 16)
@AnimationInfo(spritePrefix = "Adven")
public class Adven extends Creature {

    public Adven() { this.addController(new EnemyController(this));}
}
