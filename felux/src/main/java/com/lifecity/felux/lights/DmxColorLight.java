package com.lifecity.felux.lights;

import android.graphics.Color;

/**
 * DMX color light class
 */
public class DmxColorLight extends DmxLight {
    protected int color;

    public DmxColorLight(int address, String name) {
        super(address, name);
        this.color = Color.WHITE;
    }

    public DmxColorLight(int address, String name, int color) {
        super(address, name);
        this.color = color;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
