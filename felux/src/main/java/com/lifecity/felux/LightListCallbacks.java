package com.lifecity.felux;

import com.lifecity.felux.light.Lights;

public interface LightListCallbacks {
    /**
     * Callback for when an item has been selected.
     */
    public void onLightSelected(Lights.Light light);
}
