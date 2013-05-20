package com.lifecity.felux;

import com.lifecity.felux.dmx.DmxLights;

public interface LightListCallbacks {
    /**
     * Callback for when an item has been selected.
     */
    public void onLightSelected(DmxLights.DmxLight light);
}
