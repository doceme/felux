package com.lifecity.felux.lights;

import com.lifecity.felux.items.Item;

/**
 * DMX switch light class
 */
public class DmxSwitchLight extends DmxLight {

    public DmxSwitchLight() {
        super();
    }

    public DmxSwitchLight(DmxSwitchLight light) {
        super(light);
    }

    public DmxSwitchLight(String name) {
        super(name);
    }

    public DmxSwitchLight(String name, int universe, int address) {
        super(name, universe, address);
    }

    public Item copy() {
        return new DmxSwitchLight(this);
    }

    public boolean isOn() {
        return value != DmxLight.MIN_VALUE;
    }

    public void setOn() {
        value = DmxLight.MAX_VALUE;
    }

    public void setOff() {
        value = DmxLight.MIN_VALUE;
    }

    public void set(boolean on) {
        value = (on ? DmxLight.MAX_VALUE : DmxLight.MIN_VALUE);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object instanceof DmxSwitchLight) {
            DmxSwitchLight that = (DmxSwitchLight)object;
            return that.canEqual(this) &&
                    super.equals(that);
        } else  {
            return false;
        }
    }

    @Override public int hashCode() {
        final int prime = 13;
        int result = 1;
        result = prime * result + super.hashCode();
        return result;
    }

    @Override
    public boolean canEqual(Object object) {
        return (object instanceof DmxSwitchLight);
    }

    @Override
    public void update(Item item) {
        if (canEqual(item)) {
            DmxSwitchLight that = (DmxSwitchLight)item;
            this.universe = that.universe;
            this.address = that.address;
            super.update(item);
        }
    }

    @Override
    public void updateProperties(Item item) {
        if (canEqual(item)) {
            super.updateProperties(item);
        }
    }
}
