package com.sgusache.ft.Controller;

import de.gurkenlabs.litiengine.Game;

public class ScreenShakeEffect {
    private double v;
    private int  i;
    private int i1;

    public ScreenShakeEffect(double v, int i, int i1)
    {
        this.v = v;
        this.i = i;
        this.i1 = i1;
    }

    public void shake(){
        Game.world().camera().shake(this.v,this.i,this.i1);
    }
}
