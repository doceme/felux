package com.lifecity.felux.dmx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Color;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class DmxLights {

    /**
     * An array of DMX lights
     */
    public static List<DmxLight> LIGHTS = new ArrayList<DmxLight>();

    /**
     * A map of DMX lights, by address.
     */
    public static Map<Byte, DmxLight> LIGHT_MAP = new HashMap<Byte, DmxLight>();

    static {
        addItem(new DmxColorLight((byte)1, "Screen"));
        addItem(new DmxColorLight((byte)5, "Side"));
        addItem(new DmxColorLight((byte)9, "Ceiling"));
        addItem(new DmxLight((byte)13, "Stage"));
    }

    private static void addItem(DmxLight light) {
        LIGHTS.add(light);
        LIGHT_MAP.put(light.address, light);
    }

    /**
     * DMX basic light class
     */
    public static class DmxLight {
        public byte address;
        public String name;
        protected byte value;

        public DmxLight(byte address, String name) {
            this.address = address;
            this.name = name;
        }

        public byte getValue() {
            return this.value;
        }

        public void setValue(byte value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * DMX color light class
     */
    public static class DmxColorLight extends DmxLight {
        protected int color;

        public DmxColorLight(byte address, String name) {
            super(address, name);
            this.color = Color.WHITE;
        }

        public DmxColorLight(byte address, String name, int color) {
            super(address, name);
            this.color = color;
        }

        public int getColor() {
            return this.color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }
}
