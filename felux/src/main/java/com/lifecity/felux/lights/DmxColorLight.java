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

    public DmxColorLight(String name, int universe, int address) {
        super(name, universe, address);
        this.value = Color.WHITE;
    }

    public DmxColorLight(String name, int universe, int address, int color) {
        super(name, universe, address);
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
        if (this == object) {
            return true;
        }

        if (object instanceof DmxColorLight) {
            DmxColorLight that = (DmxColorLight)object;
            return that.canEqual(this) && super.equals(that);
        } else {
            return false;
        }
    }

    @Override public int hashCode() {
        final int prime = 7;
        int result = 1;
        result = prime * result + super.hashCode();
        return result;
    }

    @Override
    public boolean canEqual(Object object) {
        return (object instanceof DmxColorLight);
    }

    @Override
    public void update(Item item) {
        if (canEqual(item)) {
            super.update(item);
        }
    }

    @Override
    public void updateProperties(Item item) {
        if (canEqual(item)) {
            super.updateProperties(item);
        }
    }
}
