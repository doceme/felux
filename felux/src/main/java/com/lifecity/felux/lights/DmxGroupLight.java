package com.lifecity.felux.lights;

import com.lifecity.felux.lights.DmxLight;

/**
 * DMX group light class
 */
public class DmxGroupLight extends DmxLight {
    protected int endAddress;

    public DmxGroupLight() {
        super();
    }

    public DmxGroupLight(String name) {
        super(name);
    }

    public DmxGroupLight(String name, int startAddress, int endAddress) {
        super(name);
        this.address = startAddress;
        this.endAddress = endAddress;
    }

    public int getStartAddress() {
        return address;
    }

    public DmxGroupLight setStartAddress(int address) {
        this.address = address;
        return this;
    }

    public int getEndAddress() {
        return endAddress;
    }

    public DmxGroupLight setEndAddress(int address) {
        this.endAddress = address;
        return this;
    }
}
