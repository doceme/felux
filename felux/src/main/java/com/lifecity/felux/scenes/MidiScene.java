package com.lifecity.felux.scenes;

import com.lifecity.felux.items.Item;

/**
 * MidiScene class
 */
public class MidiScene extends Scene {
    int channel = 0;
    int note = 0;
    int velocity = 0;
    boolean eventOn = true;

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

    public MidiScene(String name, int channel, int note, int velocity) {
        super(name);
        this.channel = channel;
        this.note = note;
        this.velocity = velocity;
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

    public boolean getEventOn() {
        return eventOn;
    }

    public MidiScene setEventOn(boolean eventOn) {
        this.eventOn = eventOn;
        return this;
    }

    @Override
    public boolean canEqual(Object object) {
        return (object instanceof MidiScene);
    }

    @Override
    public void update(Item item) {
        if (canEqual(item)) {
            MidiScene that = (MidiScene)item;
            this.channel = that.getChannel();
            this.note = that.getNote();
            this.velocity = that.getVelocity();
            super.update(item);
        }
    }

    @Override
    public void updateProperties(Item item) {
        if (canEqual(item)) {
            MidiScene that = (MidiScene)item;
            this.channel = that.getChannel();
            this.note = that.getNote();
            this.velocity = that.getVelocity();
            super.updateProperties(item);
        }
    }
}
