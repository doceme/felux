package com.lifecity.felux.scene;

import java.util.ArrayList;
import java.util.List;

import android.util.SparseArray;

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

    static {
        SCENES.add(new Scene("Scene 1"));
    }

    /**
     * DMX basic light class
     */
    public static class Scene {
        public String name;

        public Scene(String name) {
            this.name = name;
        }

        public Scene(int index, String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
