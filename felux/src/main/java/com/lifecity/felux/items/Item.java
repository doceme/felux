package com.lifecity.felux.items;

import java.text.DateFormatSymbols;

/**
 * Basic item class
 */
public abstract class Item {
    protected String name;
    protected boolean checked;

    public Item() {
    }

    public Item(Item item) {
        this.name = item.name;
        this.checked = item.checked;
    }

    public Item(String name) {
        this.name = name;
    }

    public abstract Item copy();

    public String getName() {
        return this.name;
    }

    public Item setName(String name) {
        this.name = name;
        return this;
    }

    public boolean getChecked() {
        return checked;
    }

    public Item setChecked(boolean checked) {
        this.checked = checked;
        return this;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Item) {
            Item item = (Item)object;
            if (item.getName() != name) {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }
}
