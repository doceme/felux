package com.lifecity.felux.lights;

import com.lifecity.felux.items.Item;

/**
 * DMX group light class
 */
public class DmxGroupLight extends DmxLight {
    protected int endAddress = 0;

    public DmxGroupLight() {
        super();
    }

    public DmxGroupLight(DmxGroupLight light) {
        super(light);
        endAddress = light.endAddress;
    }

    public DmxGroupLight(String name) {
        super(name);
    }

    public Item copy() {
        return new DmxGroupLight(this);
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

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object instanceof DmxGroupLight) {
            DmxGroupLight that = (DmxGroupLight)object;
            return that.canEqual(this) && (that.getEndAddress() == endAddress) && super.equals(that);
        } else {
            return false;
        }
    }

    @Override public int hashCode() {
        final int prime = 17;
        int result = 1;
        result = 31 * result + endAddress;
        result = 31 * result + super.hashCode();
        return result;
    }

    @Override
    public boolean canEqual(Object object) {
        return (object instanceof DmxGroupLight);
    }

    @Override
    public void update(Item item) {
        if (item instanceof DmxGroupLight) {
            DmxGroupLight that = (DmxGroupLight)item;
            this.endAddress = that.endAddress;
            super.update(item);
        }
    }
}
