package com.lifecity.felux;

/**
 * Basic light class
 */
public class Light extends Item {
    protected byte value;

    public Light(String name) {
        super(name);
    }

    public byte getValue() {
        return this.value;
    }

    public void setValue(byte value) {
        this.value = value;
    }
}
