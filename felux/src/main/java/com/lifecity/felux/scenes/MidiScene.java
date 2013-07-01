package com.lifecity.felux.scenes;

import com.lifecity.felux.items.Item;

/**
 * MidiScene class
 */
public class MidiScene extends Scene {
    int channel = 0;
    int note = 0;
    int velocity = 0;

    public MidiScene() {
        super();
    }

    public MidiScene(MidiScene scene) {
        super(scene);
        this.channel = scene.channel;
        this.note = scene.note;
        this.velocity = scene.velocity;
    }

    public MidiScene(String name) {
        super(name);
    }

    public Item copy() {
        return new MidiScene(this);
    }

    public int getChannel() {
        return channel;
    }

    public MidiScene setChannel(int channel) {
        this.channel = channel;
        return this;
    }

    public int getNote() {
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

    @Override
    public void update(Item item) {
        if (item instanceof MidiScene) {
            MidiScene that = (MidiScene)item;
            this.channel = that.getChannel();
            this.note = that.getNote();
            this.velocity = that.getVelocity();
            super.update(item);
        }
    }
}
