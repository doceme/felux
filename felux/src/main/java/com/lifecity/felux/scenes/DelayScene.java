package com.lifecity.felux.scenes;

import com.lifecity.felux.items.Item;

/**
 * DelayScene class
 */
public class DelayScene extends Scene {
    int delay;

    public DelayScene() {
        super();
    }

    public DelayScene(String name) {
        super(name);
    }

    public int getDelay() {
        return delay;
    }

    public DelayScene setDelay(int delay) {
        this.delay = delay;
        return this;
    }
}
