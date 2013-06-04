package com.lifecity.felux.scenes;

import com.lifecity.felux.items.Item;
import com.lifecity.felux.lights.Light;

import java.util.ArrayList;
import java.util.List;

/**
 * LightScene class
 */
public class LightScene extends Scene {
    protected List<Light> lights = new ArrayList<Light>();

    public LightScene() {
        super();
    }

    public LightScene(String name) {
        super(name);
    }

    public LightScene addLight(Light light) {
        lights.add(light);
        return this;
    }

    public LightScene removeLight(Light light) {
        lights.remove(light);
        return this;
    }

    public Light getLightAt(int position) {
        return lights.get(position);
    }
}