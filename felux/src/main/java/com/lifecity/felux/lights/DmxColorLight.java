package com.lifecity.felux.lights;

import android.graphics.Color;
import com.lifecity.felux.items.Item;

/**
 * DMX color light class
 */
public class DmxColorLight extends DmxLight {
    public DmxColorLight() {
        super();
        this.value = Color.WHITE;
    }

    public DmxColorLight(DmxColorLight light) {
        super(light);
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

    public Item copy() {
        return new DmxColorLight(this);
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
