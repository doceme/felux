package com.lifecity.felux;

/**
 * DMX basic light class
 */
public class DmxLight extends Light {
    public int address;

    public DmxLight(int address, String name) {
        super(name);
        this.address = address;
    }
}
