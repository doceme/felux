package com.lifecity.felux;

public interface ItemDetailCallbacks<T> {
    /**
     * Callback for when an item has been selected.
     */
    public void onItemDetailUpdated(T item);
}
