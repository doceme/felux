package com.lifecity.felux.lights;

import android.graphics.Color;

/**
 * DMX color light class
 */
public class DmxColorLight extends DmxLight {
    public DmxColorLight() {
        super();
        this.value = Color.WHITE;
    }

    public DmxColorLight(String name) {
        super(name);
        this.value = Color.WHITE;
    }

    public DmxColorLight(String name, int address) {
        super(name, address);
        this.value = Color.WHITE;
    }

    public DmxColorLight(String name, int address, int color) {
        super(name, address);
        this.value = color;
    }

    public int getColor() {
        return this.value;
    }

    public DmxColorLight setColor(int color) {
        this.value = color;
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof DmxColorLight)) {
            return false;
        }

        return super.equals(object);
    }
}
