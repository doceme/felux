package com.lifecity.felux.cues;

import com.lifecity.felux.items.Item;
import com.lifecity.felux.scenes.Scene;

import java.util.ArrayList;
import java.util.List;

/**
 * Cue class
 */
public class Cue extends Item {
    protected List<Scene> scenes = new ArrayList<Scene>();

    public Cue() {
        super();
    }

    public Cue(Cue cue) {
        super(cue);
        scenes = new ArrayList<Scene>(cue.getScenes().size());
        for (Scene scene : cue.getScenes()) {
            addScene(scene);
        }
    }

    public Cue(String name) {
        super(name);
    }

    @Override
    public Item copy() {
        return new Cue(this);
    }

    public Cue addScene(Scene scene) {
        scenes.add(scene);
        return this;
    }

    public Cue removeLight(Scene scene) {
        scenes.remove(scene);
        return this;
    }

    public Scene getSceneAt(int position) {
        return scenes.get(position);
    }

    public List<Scene> getScenes() {
        return scenes;
    }

    public void setLights(List<Scene> scenes) {
        this.scenes = scenes;
    }

    public boolean hasScene(Scene scene) {
        for (Scene checkLight: scenes) {
            if (checkLight.equals(scene)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void update(Item item) {
        if (item instanceof Cue) {
            Cue that = (Cue)item;
            this.scenes = that.getScenes();
            super.update(item);
        }
    }
}
