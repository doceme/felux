package com.lifecity.felux.lights;

import com.lifecity.felux.items.Item;

/**
 * Basic light class
 */
public class Light extends Item {
    protected byte value;

    public Light() {
        super();
    }

    public Light(String name) {
        super(name);
    }

    public byte getValue() {
        return this.value;
    }

    public Light setValue(byte value) {
        this.value = value;
        return this;
    }
}
