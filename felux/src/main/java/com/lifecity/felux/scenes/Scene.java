package com.lifecity.felux.scenes;

import com.lifecity.felux.items.Item;

/**
 * Scene class
 */
public abstract class Scene extends Item {
    public Scene() {
        super();
    }

    public Scene(Scene scene) {
        super(scene);
    }

    public Scene(String name) {
        super(name);
    }

    @Override
    public void update(Item item) {
        if (item instanceof Scene) {
            super.update(item);
        }
    }
}
