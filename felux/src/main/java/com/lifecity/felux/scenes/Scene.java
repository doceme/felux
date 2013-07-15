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
    public void update(Item item) {
        if (item instanceof Scene) {
            Scene that = (Scene)item;
            hold = that.getHold();
            super.update(item);
        }
    }

    @Override
    public void updateProperties(Item item) {
        if (item instanceof Scene) {
            Scene that = (Scene)item;
            super.updateProperties(item);
        }
    }
}
