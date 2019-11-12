package com.sgusache.ft.Model;

import com.sgusache.ft.Controller.EnemyController;
import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.annotation.AnimationInfo;
import de.gurkenlabs.litiengine.annotation.EntityInfo;
import de.gurkenlabs.litiengine.entities.Creature;

@EntityInfo(width = 18, height = 18)
@AnimationInfo(spritePrefix = "knight", deathAnimations = "knight-die-right")
public class Knight extends Creature {
    private int hp;
    public String name;

    public Knight()
    {
        this.name = "Knight";
        this.addController(new EnemyController(this));
        this.hp = 1;
        this.setName("Knight");


    }

    @Override
    public String getName() {
        return name;
    }
}
