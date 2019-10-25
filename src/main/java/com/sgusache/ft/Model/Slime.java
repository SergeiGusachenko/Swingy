package com.sgusache.ft.Model;


import com.sgusache.ft.Controller.EnemyController;
import de.gurkenlabs.litiengine.annotation.AnimationInfo;
import de.gurkenlabs.litiengine.annotation.EntityInfo;
import de.gurkenlabs.litiengine.entities.Creature;

@EntityInfo(width = 18, height = 18)
@AnimationInfo(spritePrefix = "slime")
public class Slime extends Creature{
    private int hp;
    public Slime()
    {
        this.addController(new EnemyController(this));
        this.hp = 100;
    }
}
