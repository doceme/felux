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
        lights = new ArrayList<Light>(scene.getLights().size());
        for (Light light : scene.getLights()) {
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

    public void setLights(List<Light> lights) {
        this.lights = lights;
    }

    public boolean hasLight(Light light) {
        for (Light checkLight: lights) {
            if (checkLight.equals(light)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void update(Item item) {
        if (item instanceof LightScene) {
            LightScene that = (LightScene)item;
            this.lights = that.getLights();
            super.update(item);
        }
    }
}
