package com.lifecity.felux.scene;

import java.util.ArrayList;
import java.util.List;

import android.util.SparseArray;

import com.lifecity.felux.dmx.DmxLights.DmxLight;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class Scenes {

    /**
     * An array of DMX lights
     */
    public static List<Scene> SCENES = new ArrayList<Scene>();

    /**
     * A map of DMX lights, by address.
     */
    public static SparseArray<Scene> SCENE_MAP = new SparseArray<Scene>();

    static {
        addItem(new Scene("Song 1"));
    }

    private static void addItem(Scene scene) {
        SCENES.add(scene);
        SCENE_MAP.put(scene.index, scene);
    }

    /**
     * DMX basic light class
     */
    public static class Scene {
        public int index;
        public String name;
        public List<DmxLight> lights = new ArrayList<DmxLight>();

        public Scene(String name) {
            this.index = SCENES.size();
            this.name = name;
        }
        
        public Scene(int index, String name) {
            this.index = index;
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
