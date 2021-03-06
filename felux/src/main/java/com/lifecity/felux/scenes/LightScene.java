package com.lifecity.felux.scenes;

import com.lifecity.felux.items.Item;
import com.lifecity.felux.lights.Light;

import java.util.ArrayList;
import java.util.List;

/**
 * LightScene class
 */
public class LightScene extends Scene {
    protected float fade = 0;
    protected List<Light> lights = new ArrayList<Light>();

    public LightScene() {
        super();
    }

    public LightScene(LightScene scene) {
        super(scene);
        this.fade = scene.getFade();
        lights = new ArrayList<Light>(scene.getLights().size());
        for (Light light : scene.getLights()) {
            addLight((Light)light.copy());
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

    public float getFade() {
        return this.fade;
    }

    public LightScene setFade(float fade) {
        this.fade = fade;
        return this;
    }

    @Override
    public void update(Item item) {
        if (item instanceof LightScene) {
            LightScene that = (LightScene)item;
            this.lights = that.getLights();
            this.fade = that.getFade();
            super.update(item);
        }
    }

    @Override
    public void updateProperties(Item item) {
        if (item instanceof LightScene) {
            LightScene that = (LightScene)item;
            lights.clear();
            for (Light thatLight: that.getLights()) {
                addLight((Light)thatLight.copy());
            }
            super.updateProperties(item);
        }
    }
}
