package com.lifecity.felux.scenes;

import com.lifecity.felux.items.Item;

/**
 * Scene class
 */
public abstract class Scene extends Item {
    protected float hold;

    public Scene() {
        super();
    }

    public Scene(Scene scene) {
        super(scene);
        this.hold = scene.hold;
    }

    public Scene(String name) {
        super(name);
    }

    public float getHold() {
        return this.hold;
    }

    public Scene setHold(float hold) {
        this.hold = hold;
        return this;
    }

    @Override
    public boolean canEqual(Object object) {
        return (object instanceof Scene);
    }

    @Override
    public void update(Item item) {
        if (canEqual(item)) {
            Scene that = (Scene)item;
            hold = that.getHold();
            super.update(item);
        }
    }

    @Override
    public void updateProperties(Item item) {
        if (canEqual(item)) {
            super.updateProperties(item);
        }
    }
}
