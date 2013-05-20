package com.lifecity.felux;

import com.lifecity.felux.scene.Scenes;

public interface SceneListCallbacks {
    /**
     * Callback for when an item has been selected.
     */
    public void onSceneSelected(Scenes.Scene scene);
}
