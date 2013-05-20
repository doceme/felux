package com.lifecity.felux.light;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.util.SparseArray;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class Lights {

    /**
     * An array of DMX lights
     */
    public static List<Light> LIGHTS = new ArrayList<Light>();

    /**
     * A map of DMX lights, by address.
     */
    //public static SparseArray<Light> LIGHT_MAP = new SparseArray<Light>();

    static {
        LIGHTS.add(new DmxColorLight(1, "Screen"));
        LIGHTS.add(new DmxColorLight(5, "Side"));
        LIGHTS.add(new DmxColorLight(9, "Ceiling"));
        LIGHTS.add(new DmxLight(13, "Stage"));
    }

    /**
     * Basic light class
     */
    public static class Light {
        public String name;
        protected byte value;

        public Light(String name) {
            this.name = name;
        }

        public Light(String name, int position) {
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
     * DMX basic light class
     */
    public static class DmxLight extends Light {
        public int address;

        public DmxLight(int address, String name) {
            super(name);
            this.address = address;
        }
    }

    /**
     * DMX color light class
     */
    public static class DmxColorLight extends DmxLight {
        protected int color;

        public DmxColorLight(int address, String name) {
            super(address, name);
            this.color = Color.WHITE;
        }

        public int getColor() {
            return this.color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }
}
