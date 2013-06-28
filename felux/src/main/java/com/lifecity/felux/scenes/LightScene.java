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

    public LightScene(LightScene scene) {
        super(scene);
        lights = new ArrayList<Light>();
        for (Light light : scene.lights) {
            addLight(light);
        }
    }

    public LightScene(String name) {
        super(name);
    }

    public Item copy() {
        return new LightScene(this);
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

    public List<Light> getLights() {
        return lights;
    }

    public boolean hasLight(Light light) {
        for (Light checkLight: lights) {
            if (checkLight.equals(light)) {
                return true;
            }
        }

        return false;
    }
}
