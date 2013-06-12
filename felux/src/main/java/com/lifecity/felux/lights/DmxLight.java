package com.lifecity.felux.lights;

import com.lifecity.felux.items.Item;

/**
 * DMX basic light class
 */
public class DmxLight extends Light {
    protected int address;

    public DmxLight() {
        super();
    }

    public DmxLight(DmxLight light) {
        super(light);
        address = light.address;
    }

    public DmxLight(String name) {
        super(name);
    }

    public DmxLight(String name, int address) {
        super(name);
        this.address = address;
    }

    public Item copy() {
        return new DmxLight(this);
    }

    public int getAddress() {
        return address;
    }

    public DmxLight setAddress(int address) {
        this.address = address;
        return this;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof DmxLight) {
            DmxLight light = (DmxLight)object;
            if (light.getAddress() != address) {
                return false;
            }
        } else {
            return false;
        }

        return super.equals(object);
    }
}
