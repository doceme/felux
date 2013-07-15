package com.lifecity.felux.lights;

import com.lifecity.felux.items.Item;

/**
 * Basic light class
 */
public abstract class Light extends Item {
    protected int value = 0;
    public static final int MIN_VALUE = 0;
    public static final int MAX_VALUE = 255;

    public Light() {
        super();
        value = 0;
    }

    public Light(Light light) {
        super(light);
        value = light.value;
    }

    public Light(String name) {
        super(name);
        value = 0;
    }

    public int getValue() {
        return this.value;
    }

    public static int getPercent(int value) {
        return (int)(((float)value / 255.0f) * 100);
    }

    public int getPercent() {
        return Light.getPercent(value);
    }

    public Light setValue(int value) {
        if (!(this instanceof DmxColorLight)) {
            if (value < MIN_VALUE) {
                value = MIN_VALUE;
            } else if (value > MAX_VALUE) {
                value = MAX_VALUE;
            }
        }

        this.value = value;
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object instanceof Light) {
            Light that = (Light)object;
            //return that.canEqual(this) && (that.getValue() == value) && super.equals(that);
            return that.canEqual(this) && super.equals(that);
        } else {
            return false;
        }
    }

    @Override public int hashCode() {
        final int prime = 17;
        int result = 1;
        //result = 31 * result + value;
        result = 31 * result + super.hashCode();
        return result;
    }

    @Override
    public boolean canEqual(Object object) {
        return (object instanceof Light);
    }

    @Override
    public void update(Item item) {
        if (item instanceof Light) {
            Light that = (Light)item;
            this.value = that.value;
            super.update(item);
        }
    }

    @Override
    public void updateProperties(Item item) {
        if (item instanceof Light) {
            Light that = (Light)item;
            super.updateProperties(item);
        }
    }
}
