package com.lifecity.felux;

import com.lifecity.felux.lights.Light;
import com.lifecity.felux.scenes.Scene;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by scaudle on 6/7/13.
 */
public class FeluxManager {
    protected List<Light> lights = new ArrayList<Light>();
    protected List<Scene> scenes = new ArrayList<Scene>();

    public List<Light> getLights() {
        return lights;
    }

    public List<Scene> getScenes() {
        return scenes;
    }
}
