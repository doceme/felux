package com.lifecity.felux.items;

/**
 * Basic item class
 */
public abstract class Item {
    protected String name;

    public Item() {
        this.name = name;
    }

    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Item setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return name;
    }
}
