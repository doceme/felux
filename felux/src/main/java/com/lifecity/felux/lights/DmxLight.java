package com.lifecity.felux.lights;

/**
 * DMX basic light class
 */
public class DmxLight extends Light {
    protected int address;

    public DmxLight() {
        super();
    }

    public DmxLight(String name) {
        super(name);
    }

    public DmxLight(String name, int address) {
        super(name);
        this.address = address;
    }

    public int getAddress() {
        return address;
    }

    public DmxLight setAddress(int address) {
        this.address = address;
        return this;
    }
}
