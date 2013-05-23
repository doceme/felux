package com.lifecity.felux.lights;

import android.graphics.Color;

/**
 * DMX color light class
 */
public class DmxColorLight extends DmxLight {
    protected int color;

    public DmxColorLight() {
        super();
        this.color = Color.WHITE;
    }

    public DmxColorLight(String name) {
        super(name);
        this.color = Color.WHITE;
    }

    public DmxColorLight(String name, int address) {
        super(name, address);
        this.color = Color.WHITE;
    }

    public DmxColorLight(String name, int address, int color) {
        super(name, address);
        this.color = color;
    }

    public int getColor() {
        return this.color;
    }

    public DmxColorLight setColor(int color) {
        this.color = color;
        return this;
    }
}
