package com.lifecity.felux.items;

import java.util.UUID;

/**
 * Basic item class
 */
public abstract class Item {
    protected UUID uuid;
    protected String name;
    protected boolean checked;

    public Item() {
        uuid = UUID.randomUUID();
        name = "[Not set]";
    }

    public Item(Item item) {
        this.uuid = item.uuid;
        this.name = item.name;
        this.checked = item.checked;
    }

    public Item(String name) {
        uuid = UUID.randomUUID();
        this.name = name;
    }

    public Item(String name, UUID uuid) {
        this.name = name;
        uuid = this.uuid;
    }

    public abstract Item copy();

    public UUID getUuid() {
        return this.uuid;
    }

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
        if (this == object) {
            return true;
        }

        if (object instanceof Item) {
            Item that = (Item)object;
            return that.canEqual(this) && that.getName().equals(name);
        } else {
            return false;
        }
    }

    @Override public int hashCode() {
        final int prime = 17;
        int result = 1;
        result = 31 * result + (name == null ? 0 : name.hashCode());
        result = 31 * result + super.hashCode();
        return result;
    }

    public boolean canEqual(Object object) {
        return (object instanceof Item);
    }

    public void update(Item item) {
        this.name = item.name;
    }

    public void updateProperties(Item item) {
        this.name = item.name;
    }
}
