package com.lifecity.felux.lights;

import com.lifecity.felux.items.Item;

/**
 * Basic light class
 */
public abstract class Light extends Item {
    protected int value;
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
        return (int)(((float)value / 255.0f) * 100);
    }

    public Light setValue(int value) {
        if (value < MIN_VALUE) {
            value = MIN_VALUE;
        } else if (value > MAX_VALUE) {
            value = MAX_VALUE;
        }

        this.value = value;
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Light) {
            Light light = (Light)object;
            if (light.getValue() != value) {
                return false;
            }
        } else {
            return false;
        }

        return super.equals(object);
    }
}
