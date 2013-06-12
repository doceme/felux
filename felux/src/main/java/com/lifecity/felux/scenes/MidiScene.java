package com.lifecity.felux.scenes;

import com.lifecity.felux.items.Item;

/**
 * MidiScene class
 */
public class MidiScene extends Scene {
    int note;
    int velocity;
    boolean useVelocity;

    public MidiScene() {
        super();
    }

    public MidiScene(MidiScene scene) {
        super(scene);
        this.note = scene.note;
        this.velocity = scene.velocity;
        this.useVelocity = scene.useVelocity;
    }

    public MidiScene(String name) {
        super(name);
    }

    public Item copy() {
        return new MidiScene(this);
    }

    public int getNode() {
        return note;
    }

    public MidiScene setNote(int note) {
        this.note = note;
        return this;
    }

    public int getVelocity() {
        return velocity;
    }

    public MidiScene setVelocity(int velocity) {
        this.velocity = velocity;
        return this;
    }

    public MidiScene useVelocity(boolean useVelocity) {
        this.useVelocity = useVelocity;
        return this;
    }
}
