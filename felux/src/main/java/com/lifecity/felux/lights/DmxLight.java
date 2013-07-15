package com.lifecity.felux.lights;

import com.lifecity.felux.items.Item;

/**
 * DMX basic light class
 */
public class DmxLight extends Light {
    protected int universe = 0;
    protected int address = 0;

    public DmxLight() {
        super();
    }

    public DmxLight(DmxLight light) {
        super(light);
        universe = light.universe;
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

    public int getUniverse() {
        return universe;
    }

    public DmxLight setUniverse(int universe) {
        this.universe = universe;
        return this;
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
        if (this == object) {
            return true;
        }

        if (object instanceof DmxLight) {
            DmxLight that = (DmxLight)object;
            return that.canEqual(this) &&
                    (that.getUniverse() == universe) &&
                    (that.getAddress() == address) &&
                    super.equals(that);
        } else  {
            return false;
        }
    }

    @Override public int hashCode() {
        final int prime = 17;
        int result = 1;
        result = 31 * result + universe;
        result = 31 * result + address;
        result = 31 * result + super.hashCode();
        return result;
    }

    @Override
    public boolean canEqual(Object object) {
        return (object instanceof DmxLight);
    }

    @Override
    public void update(Item item) {
        if (item instanceof DmxLight) {
            DmxLight that = (DmxLight)item;
            this.universe = that.universe;
            this.address = that.address;
            super.update(item);
        }
    }

    @Override
    public void updateProperties(Item item) {
        if (item instanceof DmxLight) {
            DmxLight that = (DmxLight)item;
            this.universe = that.universe;
            this.address = that.address;
            super.updateProperties(item);
        }
    }
}
