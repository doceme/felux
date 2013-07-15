package com.lifecity.felux;

public interface ItemDetailCallbacks<T> {
    /**
     * Callback for when an item has been selected.
     */
    public void onDetailItemStartUpdate(T item);
    public void onDetailItemEndUpdate(T item);
    public void onDetailItemUpdated(T item);
    public void onDetailItemAdded(T item);
}
